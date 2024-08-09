package gc.garcol.accountbalancekafkastreamstateful.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author thaivc
 * @since 2024
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BalanceConstant {

    public static final int PRECISION = 2;

    public static final String BALANCE_STATE_STORE = "balance-state-store";
    public static final String BALANCE_CALCULATOR = "balance-calculator";
    public static final String BALANCE_TRANSACTION_TOPIC = "balance-transaction";
    public static final String BALANCE_TOPIC = "balance";
}
