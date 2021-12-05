package com.unipi.productservice.config;

import com.unipi.productservice.dto.ProductDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * SSE: Server Sent Events
 **/

@Configuration
public class SinkConfig {

    // Sink: Multiple threads can can push items
    @Bean
    public Sinks.Many<ProductDto> sink() {
        // If we emmit one item, we use one(), else, for many items, we use many()
        // Replay is capable of replaying the previously emitted items.
        // Limit the items: all(), latest().
        return Sinks.many().replay().limit(1);
    }

    // Broadcast items that created from sink, so subscribers can observe them.
    @Bean
    public Flux<ProductDto> productBroadcast(Sinks.Many<ProductDto> sink) {
        return sink.asFlux();
    }

}
