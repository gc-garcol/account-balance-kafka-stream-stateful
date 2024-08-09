package gc.garcol.accountbalancekafkastreamstateful.core;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Named;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.StoreBuilder;
import org.apache.kafka.streams.state.Stores;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.UUID;

/**
 * @author thaivc
 * @since 2024
 */
public class SAccountTopology implements SAccountTopologyTrait {

    @Override
    public void build(StreamsBuilder builder) {
        builder.addStateStore(balanceStateStore());

        KStream<UUID, ETransaction> sourceProcessor = builder.stream(
                BalanceConstant.BALANCE_TRANSACTION_TOPIC,
                Consumed.with(Serdes.UUID(), new JsonSerde<>(ETransaction.class))
        );

        sourceProcessor.process(SBalanceCalculator::new, Named.as(BalanceConstant.BALANCE_CALCULATOR), BalanceConstant.BALANCE_STATE_STORE)
                .selectKey((key, balance) -> balance.getId())
                .to(BalanceConstant.BALANCE_TOPIC, Produced.with(Serdes.UUID(), new JsonSerde<>(EBalance.class)));
    }

    private StoreBuilder<KeyValueStore<UUID, EBalance>> balanceStateStore() {
        return Stores.keyValueStoreBuilder(
                Stores.persistentKeyValueStore(BalanceConstant.BALANCE_STATE_STORE),
                Serdes.UUID(),
                new JsonSerde<>(EBalance.class)
        );
    }
}
