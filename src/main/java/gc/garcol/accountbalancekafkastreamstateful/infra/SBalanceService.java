package gc.garcol.accountbalancekafkastreamstateful.infra;

import gc.garcol.accountbalancekafkastreamstateful.core.*;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

/**
 * @author thaivc
 * @since 2024
 */
@Service
@RequiredArgsConstructor
public class SBalanceService implements SBalanceServiceTrait {

    private final KafkaTemplate<Object, Object> kafkaTemplate;
    private final StreamsBuilderFactoryBean streamsBuilderFactoryBean;

    @Override
    public EBalance retrieve(UUID balanceId) {
        ReadOnlyKeyValueStore<UUID, EBalance> balanceStore = balanceReadonlyStore();
        return balanceStore.get(balanceId);
    }

    @Override
    public List<EBalance> allBalances() {
        ReadOnlyKeyValueStore<UUID, EBalance> balanceStore = balanceReadonlyStore();
        var balances = new ArrayList<EBalance>();
        try (var iterator = balanceStore.all()) {
            while (iterator.hasNext()) {
                balances.add(iterator.next().value);
            }
        }
        return balances;
    }

    @Override
    public void created() {
        kafkaTemplate.send(
                new ProducerRecord<>(
                        BalanceConstant.BALANCE_TRANSACTION_TOPIC,
                        UUID.randomUUID(),
                        ETransaction.builder()
                                .id(UUID.randomUUID())
                                .type(ETransactionType.CREATE)
                                .build()
                )
        ).join();
    }

    @Override
    public void deposit(UUID balanceId, BigDecimal amount) {
        kafkaTemplate.send(
                new ProducerRecord<>(
                        BalanceConstant.BALANCE_TRANSACTION_TOPIC,
                        UUID.randomUUID(),
                        ETransaction.builder()
                                .id(UUID.randomUUID())
                                .balanceId(balanceId)
                                .type(ETransactionType.DEPOSIT)
                                .amount(amount)
                                .build()
                )
        ).join();
    }

    @Override
    public void withdraw(UUID balanceId, BigDecimal amount) {
        kafkaTemplate.send(
                new ProducerRecord<>(
                        BalanceConstant.BALANCE_TRANSACTION_TOPIC,
                        UUID.randomUUID(),
                        ETransaction.builder()
                                .id(UUID.randomUUID())
                                .balanceId(balanceId)
                                .type(ETransactionType.WITHDRAW)
                                .amount(amount)
                                .build()
                )
        ).join();
    }

    private ReadOnlyKeyValueStore<UUID, EBalance> balanceReadonlyStore() {
        return Objects.requireNonNull(streamsBuilderFactoryBean.getKafkaStreams())
                .store(StoreQueryParameters.fromNameAndType(
                        BalanceConstant.BALANCE_STATE_STORE,
                        QueryableStoreTypes.keyValueStore()));
    }
}
