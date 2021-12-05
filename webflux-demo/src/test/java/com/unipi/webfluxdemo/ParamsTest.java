package com.unipi.webfluxdemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.net.URI;
import java.util.Map;

public class ParamsTest extends BaseTest{

    @Autowired
    private WebClient webClient;

    @Test
    public void queryParamsTest() {

        // Build a query string
        String queryString = "http://localhost:8080/jobs/search?count={count}&page={page}";
        URI uri = UriComponentsBuilder.fromUriString(queryString)
                .build(10, 20);

        Flux<Integer> integerFlux = this.webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToFlux(Integer.class)
                .doOnNext(System.out::println);

        StepVerifier.create(integerFlux)
                .expectNextCount(2)
                .verifyComplete();
    }

    // Same as above but building the query inside the test
    @Test
    public void queryParamsSecondTest() {

        Flux<Integer> integerFlux = this.webClient.get()
                .uri(b -> b.path("jobs/search").query("count={count}&page={page}").build(10, 20))
                .retrieve()
                .bodyToFlux(Integer.class)
                .doOnNext(System.out::println);

        StepVerifier.create(integerFlux)
                .expectNextCount(2)
                .verifyComplete();
    }

    // Same as above using Map.of
    @Test
    public void queryParamsThirdTest() {

        Map<String, Integer> map = Map.of(
                "count", 10,
                "page", 20
        );

        Flux<Integer> integerFlux = this.webClient.get()
                .uri(b -> b.path("jobs/search").query("count={count}&page={page}").build(map))
                .retrieve()
                .bodyToFlux(Integer.class)
                .doOnNext(System.out::println);

        StepVerifier.create(integerFlux)
                .expectNextCount(2)
                .verifyComplete();
    }
}
