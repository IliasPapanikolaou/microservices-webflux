package com.unipi.webfluxdemo;

import com.unipi.webfluxdemo.config.WebClientConfig;
import com.unipi.webfluxdemo.dto.MultiplyRequestDto;
import com.unipi.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * @see WebClientConfig
 * */
public class AttributesTest extends BaseTest{

    @Autowired
    private WebClient webClient;

    // Basic Auth Test
    @Test
    public void headersBasicAuthTest() {
        Mono<Response> responseMono = this.webClient.post()
                .uri("reactive-math/multiply")
                .bodyValue(buildRequestDto(5, 2))
                // Check Attributes
                .attribute("auth", "basic")
                .retrieve() // Send the request and receive the response
                .bodyToMono(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseMono)
                .expectNextCount(1)
                .verifyComplete();
    }

    // OAuth Test
    @Test
    public void headersOAuthTest() {
        Mono<Response> responseMono = this.webClient.post()
                .uri("reactive-math/multiply")
                .bodyValue(buildRequestDto(5, 2))
                // Check Attributes
                .attribute("auth", "oauth")
                .retrieve() // Send the request and receive the response
                .bodyToMono(Response.class)
                .doOnNext(System.out::println);

        StepVerifier.create(responseMono)
                .expectNextCount(1)
                .verifyComplete();
    }

    // Create a dto (request body)
    private MultiplyRequestDto buildRequestDto(int a, int b) {
        MultiplyRequestDto dto = new MultiplyRequestDto();
        dto.setFirst(a);
        dto.setSecond(b);
        return dto;
    }
}
