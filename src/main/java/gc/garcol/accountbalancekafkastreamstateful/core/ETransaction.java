package gc.garcol.accountbalancekafkastreamstateful.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * @author thaivc
 * @since 2024
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ETransaction {
    private UUID id;
    private UUID balanceId;
    private ETransactionType type;
    private BigDecimal amount;
}
