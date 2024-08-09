package gc.garcol.accountbalancekafkastreamstateful.core;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * @author thaivc
 * @since 2024
 */
public interface SBalanceServiceTrait {

    EBalance retrieve(UUID balanceId);

    List<EBalance> allBalances();

    void created();

    void deposit(UUID balanceId, BigDecimal amount);

    void withdraw(UUID balanceId, BigDecimal amount);

}
