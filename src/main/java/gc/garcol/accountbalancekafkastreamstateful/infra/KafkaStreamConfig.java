package gc.garcol.accountbalancekafkastreamstateful.infra;

import gc.garcol.accountbalancekafkastreamstateful.core.BalanceConstant;
import gc.garcol.accountbalancekafkastreamstateful.core.SAccountTopology;
import gc.garcol.accountbalancekafkastreamstateful.core.SAccountTopologyTrait;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;

/**
 * @author thaivc
 * @since 2024
 */
@Configuration
@EnableKafka
@EnableKafkaStreams
public class KafkaStreamConfig {

    @Bean
    NewTopic transactionTopic() {
        return new NewTopic(BalanceConstant.BALANCE_TRANSACTION_TOPIC, 1, (short) 1);
    }

    @Bean
    NewTopic balanceTopic() {
        return new NewTopic(BalanceConstant.BALANCE_TOPIC, 1, (short) 1);
    }

    @Bean
    SAccountTopologyTrait accountTopology(StreamsBuilder streamsBuilder) {
        SAccountTopologyTrait topology =  new SAccountTopology();
        topology.build(streamsBuilder);
        return topology;
    }
}
