package com.unipi.webfluxdemo;

import com.unipi.webfluxdemo.dto.Response;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class GetSingleResponseTest extends BaseTest{

    @Autowired
    private WebClient webClient;

    @Test
    public void blockTest() {
        Response response = this.webClient.get()
                .uri("reactive-math/square/{input}", 5)
                .retrieve()
                .bodyToMono(Response.class) // Mono<Response>
                .block(); // We use block just for the testing purpose

        System.out.println(response);
    }

    // Same test as above implemented with the Step Verifier (Better practice)
    @Test
    public void stepVerifierTest() {
        Mono<Response> responseMono = this.webClient.get()
                .uri("reactive-math/square/{input}", 5)
                .retrieve()
                .bodyToMono(Response.class); // Mono<Response>

        StepVerifier.create(responseMono)
                .expectNextMatches(r -> r.getOutput() == 25)
                .verifyComplete();
    }
}
