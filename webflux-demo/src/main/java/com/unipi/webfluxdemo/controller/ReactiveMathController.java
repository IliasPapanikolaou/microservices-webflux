package com.unipi.webfluxdemo.controller;

import com.unipi.webfluxdemo.dto.MultiplyRequestDto;
import com.unipi.webfluxdemo.dto.Response;
import com.unipi.webfluxdemo.service.ReactiveMathService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/reactive-math")
public class ReactiveMathController {

    ReactiveMathService reactiveMathService;

    public ReactiveMathController(ReactiveMathService reactiveMathService) {
        this.reactiveMathService = reactiveMathService;
    }

    // Reactive
    @GetMapping("square/{input}")
    public Mono<Response> findSquareReactive(@PathVariable int input) {
        return this.reactiveMathService.findSquareReactive(input);
    }

    // Reactive - Sends all the data as list when they are ready (something like Mono<List<Response>>)
    @GetMapping("table/{input}")
    public Flux<Response> findMultiplicationTableReactive(@PathVariable int input) {
        return this.reactiveMathService.findMultiplicationTableReactive(input);
    }

    // Reactive - Streams data one by one
    @GetMapping(value = "table/{input}/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Response> findMultiplicationTableReactiveStream(@PathVariable int input) {
        return this.reactiveMathService.findMultiplicationTableReactive(input);
    }

    // Read body in reactive way as Mono
    @PostMapping("multiply")
    public Mono<Response> multiply(@RequestBody Mono<MultiplyRequestDto> requestDtoMono,
                                   @RequestHeader Map<String, String> headers) {
        System.out.println(headers);
        return this.reactiveMathService.multiply(requestDtoMono);
    }
}
