package gc.garcol.accountbalancekafkastreamstateful.core;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

/**
 * @author thaivc
 * @since 2024
 */
@Data
public class EBalance {
    private UUID id;
    private BigDecimal amount;
    private int precision;

    public static EBalance init() {
        EBalance balance = new EBalance();
        balance.setId(UUID.randomUUID());
        balance.setAmount(BigDecimal.ZERO);
        balance.setPrecision(BalanceConstant.PRECISION);
        return balance;
    }

    public void deposit(BigDecimal amount) {
        this.amount = this.amount.add(amount).setScale(precision, RoundingMode.HALF_EVEN);
        this.round();
    }

    public void withdraw(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }

        if (this.amount.compareTo(round(amount)) < 0) {
            throw new IllegalArgumentException("Not enough balance");
        }

        this.amount = this.amount.subtract(amount).setScale(precision, RoundingMode.HALF_EVEN);
        this.round();
    }

    private void round() {
        this.amount = round(this.amount);
    }

    private BigDecimal round(BigDecimal amount) {
        return amount.setScale(precision, RoundingMode.HALF_EVEN);
    }
}
