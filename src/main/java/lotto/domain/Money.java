package lotto.domain;

import lotto.exception.ExceptionMessage;
import lotto.exception.UnexpectedInputRangeException;

import java.util.Objects;

public class Money {
    private static final int DIVISION_OFFSET = 1000;

    private final int money;

    public Money(final int money) {
        this.money = money;

        validateMinimumMoneyInput(this.money);
    }

    public int getMoney() {
        return money;
    }

    public int getTicketCount() {
        return money / DIVISION_OFFSET;
    }

    private void validateMinimumMoneyInput(int money) {
        if (money < DIVISION_OFFSET) {
            throw new UnexpectedInputRangeException(ExceptionMessage.MINIMUM_PURCHASE_AMOUNT_MESSAGE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return this.money == money.money;
    }

    @Override
    public int hashCode() {
        return Objects.hash(money);
    }
}
