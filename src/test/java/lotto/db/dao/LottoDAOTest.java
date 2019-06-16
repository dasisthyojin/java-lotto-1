package lotto.db.dao;

import lotto.domain.LottoTickets;
import lotto.domain.Money;
import lotto.domain.factory.LottoTicketsFactory;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LottoDAOTest {
    private LottoDAO lottoDAO = new LottoDAO();

    @Test
    void 일반로또_삽입_테스트() throws SQLException {
        LottoTickets lottoTickets = LottoTicketsFactory.getInstance().create(new Money(5000), Arrays.asList("1,2,3,4,5,6", "10,20,30,40,41,42"));
        lottoDAO.addLottoTicket(lottoTickets);
    }
}