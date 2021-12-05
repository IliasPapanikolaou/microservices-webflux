package com.unipi.webfluxdemo;

import com.unipi.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class GetMultiResponseTest extends BaseTest{

    @Autowired
    private WebClient webClient;

    @Test
    public void fluxTest() {
        Flux<Response> responseFlux = this.webClient.get()
                .uri("reactive-math/table/{input}", 5)
                .retrieve()
                .bodyToFlux(Response.class) // Flux<Response>
                .doOnNext(System.out::println);

        StepVerifier.create(responseFlux)
                .expectNextCount(10) // Expect 10 items
                .verifyComplete();
    }

    // Same as above with Stream
    @Test
    public void fluxStreamTest() {
        Flux<Response> responseFlux = this.webClient.get()
                .uri("reactive-math/table/{input}/stream", 5)
                .retrieve()
                .bodyToFlux(Response.class) // Flux<Response>
                .doOnNext(System.out::println);

        StepVerifier.create(responseFlux)
                .expectNextCount(10) // Expect 10 items
                .verifyComplete();
    }
 }
