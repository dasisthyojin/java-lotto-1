package lotto.domain.factory;

import lotto.domain.LottoNumber;
import lotto.domain.LottoTicket;
import lotto.exception.DuplicatedInputException;
import lotto.exception.ExceptionMessage;
import lotto.exception.UnexpectedInputRangeException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LottoTicketFactory {
    private static LottoTicketFactory FACTORY_INSTANCE;

    private LottoTicketFactory() {
    }

    public static LottoTicketFactory getInstance() {
        if (FACTORY_INSTANCE == null) {
            FACTORY_INSTANCE = new LottoTicketFactory();
        }

        return FACTORY_INSTANCE;
    }

    public LottoTicket create(String input) {
        return new CustomLottoFactory(input).makeLotto();
    }

    public LottoTicket create() {
        return new AutoLottoFactory().makeLotto();
    }

    private class AutoLottoFactory implements LottoTicketFactoryStrategy {
        @Override
        public LottoTicket makeLotto() {
            return new LottoTicket(LottoNumber.getRandomNumbers());
        }
    }

    private class CustomLottoFactory implements LottoTicketFactoryStrategy {
        private static final String NUMERIC_BUT_OUT_OF_BOUNDS = "0";

        private final String input;

        CustomLottoFactory(final String input) {
            this.input = input;
        }

        @Override
        public LottoTicket makeLotto() {
            List<LottoNumber> lottoNumbers = new ArrayList<>();
            String[] inputNumbers = input.split(",");

            for (String inputNumber : inputNumbers) {
                validateNumeric(inputNumber);
                lottoNumbers.add(LottoNumber.getInstance(Integer.parseInt(inputNumber)));
            }
            validateDistinctNumber(lottoNumbers);

            return new LottoTicket(lottoNumbers);
        }

        private void validateNumeric(String number) {
            if (!number.matches("(\\d+)?")) {
                throw new ArithmeticException(ExceptionMessage.ILLEGAL_LOTTO_NUMBER_EXCEPTION);
            }
            if (number.equals(NUMERIC_BUT_OUT_OF_BOUNDS)) {
                throw new UnexpectedInputRangeException(ExceptionMessage.ILLEGAL_LOTTO_NUMBER_EXCEPTION);
            }
        }

        private void validateDistinctNumber(List<LottoNumber> lottoNumbers) {
            boolean isDistinct = lottoNumbers.stream().distinct().collect(Collectors.toList()).size() != lottoNumbers.size();
            if (isDistinct) {
                throw new DuplicatedInputException(ExceptionMessage.DUPLICATED_NUMBER_EXCEPTION);
            }
        }
    }

}
