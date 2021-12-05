package com.unipi.webfluxdemo.service;

import com.unipi.webfluxdemo.dto.MultiplyRequestDto;
import com.unipi.webfluxdemo.dto.Response;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class ReactiveMathService {

    public Mono<Response> findSquareReactive(int input) {

        // Not recommended (Only if we want to return the input as it is, without any calculations):
        // return Mono.just(new Response(input * input));

        // Recommended (Building a pipeline, so all the calculations are happening in reactive state)
        return Mono.fromSupplier(() -> input * input)
                .map(Response::new);
    }

    public Flux<Response> findMultiplicationTableReactive(int input) {
        return Flux.range(1, 10)
                .delayElements(Duration.ofSeconds(1)) // non-blocking sleep
                // .doOnNext(i -> SleepUtil.sleepSeconds(1))
                .doOnNext(i -> System.out.println("Math-Service processing: " + i))
                .map(i -> new Response(i * input));
    }

    public Mono<Response> multiply(Mono<MultiplyRequestDto> dtoMono) {
        return dtoMono
                .map(dto -> dto.getFirst() * dto.getSecond())
                .map(Response::new);
    }
}
