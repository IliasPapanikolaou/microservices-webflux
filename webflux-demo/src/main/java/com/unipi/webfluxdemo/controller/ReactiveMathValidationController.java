package com.unipi.webfluxdemo.controller;

import com.unipi.webfluxdemo.dto.Response;
import com.unipi.webfluxdemo.exception.InputValidationException;
import com.unipi.webfluxdemo.service.ReactiveMathService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("reactive-math")
public class ReactiveMathValidationController {

    private ReactiveMathService mathService;

    public ReactiveMathValidationController(ReactiveMathService mathService) {
        this.mathService = mathService;
    }

    @GetMapping("square/{input}/throw")
    public Mono<Response> findSquare(@PathVariable int input) {
        if (input < 10 || input > 20) {
            throw new InputValidationException(input);
        }
        return this.mathService.findSquareReactive(input);
    }

    // Handle error using pipeline
    @GetMapping("square/{input}/mono-error")
    public Mono<Response> monoError(@PathVariable int input) {
        return Mono.just(input)
                .handle((integer, sink) -> {
                    if (integer >= 10 && integer <= 20) {
                        sink.next(integer);
                    }
                    else
                        sink.error(new InputValidationException(integer));
                })
                .cast(Integer.class)
                .flatMap(i -> this.mathService.findSquareReactive(i));
    }

    // Assignment: Input range is 10-20 -> send response else 400 bad request
    @GetMapping("square/{input}/assignment")
    public Mono<ResponseEntity<Response>> assignment(@PathVariable int input) {
        return Mono.just(input)
                .filter(i -> i >= 10 && i <= 20)
                .flatMap(i -> this.mathService.findSquareReactive(i))
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}
