package gc.garcol.accountbalancekafkastreamstateful.core;

import org.apache.kafka.streams.StreamsBuilder;

/**
 * @author thaivc
 * @since 2024
 */
public interface SAccountTopologyTrait {
    void build(StreamsBuilder builder);
}
