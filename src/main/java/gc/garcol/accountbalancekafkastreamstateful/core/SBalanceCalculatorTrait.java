package gc.garcol.accountbalancekafkastreamstateful.core;

import org.apache.kafka.streams.processor.api.Processor;

import java.util.UUID;

/**
 * @author thaivc
 * @since 2024
 */
public interface SBalanceCalculatorTrait extends Processor<UUID, ETransaction, UUID, EBalance> {
}
