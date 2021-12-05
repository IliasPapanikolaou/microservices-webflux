package com.unipi.webfluxdemo;

import com.unipi.webfluxdemo.dto.InputFailedValidationResponse;
import com.unipi.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class BadRequestExchangeTest extends BaseTest{

    @Autowired
    private WebClient webClient;

    // The exchange == retrieve + additional info (http status code)
    @Test
    public void badRequestWithExchangeTest() {
        Mono<Object> responseMono = this.webClient.get()
                .uri("reactive-math/square/{input}/throw", 5)
                .exchangeToMono(this::exchange)
                .doOnNext(System.out::println) // If receive an object, then do
                .doOnError(err -> System.out.println(err.getMessage()));

        StepVerifier.create(responseMono)
                .expectNextCount(1)
                .verifyComplete();
    }

    private Mono<Object> exchange(ClientResponse clientResponse) {
        if (clientResponse.rawStatusCode() == 400) {
            return clientResponse.bodyToMono(InputFailedValidationResponse.class);
        }
        else
            return clientResponse.bodyToMono(Response.class);
    }
}
