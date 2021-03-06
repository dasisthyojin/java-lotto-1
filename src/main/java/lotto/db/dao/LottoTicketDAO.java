package lotto.db.dao;

import lotto.domain.LottoNumber;
import lotto.domain.LottoTicket;
import lotto.domain.factory.LottoTicketFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static lotto.db.DBConnection.getConnection;

public class LottoTicketDAO {
    private static final Connection conn = getConnection();

    public static int addLotto(int lottoType) throws SQLException {
        // 일반로또의 로또 타입은 0, 당첨 로또 타입은 1
        String query = "INSERT INTO lotto.lotto (type) VALUES (" + lottoType + ")";
        PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        pstmt.executeUpdate();

        ResultSet rs = pstmt.getGeneratedKeys();
        return (rs.next()) ? rs.getInt(1) : 0;
    }

    public static void addLottoNumbers(int autoInsertedKey, List<LottoNumber> numbers) throws SQLException {
        String query = "INSERT INTO lotto.lottonumber (lotto_id, number) VALUES (?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);

        for (LottoNumber number : numbers) {
            pstmt.setInt(1, autoInsertedKey);
            pstmt.setInt(2, number.getNumber());
            pstmt.addBatch();
            pstmt.clearParameters();
        }
        pstmt.executeBatch();
    }

    public static void addLottoGame(int lotto_id) throws SQLException {
        String query = "INSERT INTO lotto.lottogame (lotto_id) VALUES (?);";
        PreparedStatement pstmt = conn.prepareStatement(query);

        pstmt.setInt(1, lotto_id);
        pstmt.executeUpdate();
    }

    public static List<LottoTicket> findLottosByLottoId(int lottoId) throws SQLException {
        String query = "SELECT GROUP_CONCAT(ln.number ORDER BY ln.number SEPARATOR  ',') as numbers " +
                "FROM lotto.lotto as l " +
                "JOIN lotto.lottonumber as ln ON l.id = ln.lotto_id " +
                "JOIN lotto.lottogame as lg ON l.id = lg.lotto_id " +
                "WHERE lg.winning_id = ? GROUP BY l.id;";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, lottoId);
        ResultSet rs = pstmt.executeQuery();

        List<LottoTicket> lottoTickets = new ArrayList<>();
        while (rs.next()) {
            lottoTickets.add(LottoTicketFactory.getInstance().create(rs.getString("numbers")));
        }

        return lottoTickets;
    }
}
