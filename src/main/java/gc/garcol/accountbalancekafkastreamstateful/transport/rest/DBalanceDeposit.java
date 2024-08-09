package gc.garcol.accountbalancekafkastreamstateful.transport.rest;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author thaivc
 * @since 2024
 */
public record DBalanceDeposit(
        UUID balanceId,
        BigDecimal amount
) {
}
