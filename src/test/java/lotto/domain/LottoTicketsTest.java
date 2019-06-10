package lotto.domain;

import lotto.domain.Factory.LottoTicketFactory;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class LottoTicketsTest {
    @Test
    void create() {
        List<LottoTicket> lottoTickets = Arrays.asList(
                LottoTicketFactory.getInstance().create("1,22,23,31,39,45"), LottoTicketFactory.getInstance().create("1,2,23,39,42,45"));

        assertThat(new LottoTickets(lottoTickets)).isEqualTo(new LottoTickets(lottoTickets));
    }
}
