package com.unipi.webfluxdemo.config;

import com.unipi.webfluxdemo.dto.MultiplyRequestDto;
import com.unipi.webfluxdemo.dto.Response;
import com.unipi.webfluxdemo.exception.InputValidationException;
import com.unipi.webfluxdemo.service.ReactiveMathService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RequestHandler {

    private final ReactiveMathService mathService;

    public RequestHandler(ReactiveMathService mathService) {
        this.mathService = mathService;
    }

    // GET Square (Mono)
    public Mono<ServerResponse> squareHandler(ServerRequest serverRequest) {
        int input = Integer.parseInt(serverRequest.pathVariable("input"));
        Mono<Response> responseMono = this.mathService.findSquareReactive(input);
        return ServerResponse.ok().body(responseMono, Response.class);
    }

    // GET Table (Flux)
    public Mono<ServerResponse> tableHandler(ServerRequest serverRequest) {
        int input = Integer.parseInt(serverRequest.pathVariable("input"));
        Flux<Response> responseFlux = this.mathService.findMultiplicationTableReactive(input);
        return ServerResponse.ok().body(responseFlux, Response.class);
    }

    // GET Table Stream (Flux)
    public Mono<ServerResponse> tableHandlerStream(ServerRequest serverRequest) {
        int input = Integer.parseInt(serverRequest.pathVariable("input"));
        Flux<Response> responseFlux = this.mathService.findMultiplicationTableReactive(input);
        return ServerResponse.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .body(responseFlux, Response.class);
    }

    // POST multiply (Mono)
    public Mono<ServerResponse> multiplyHandler(ServerRequest serverRequest) {
        Mono<MultiplyRequestDto> requestDtoMono = serverRequest.bodyToMono(MultiplyRequestDto.class);
        Mono<Response> responseMono = this.mathService.multiply(requestDtoMono);
        return ServerResponse.ok()
                .body(responseMono, Response.class);
    }

    // GET Square with Validation (Mono)
    public Mono<ServerResponse> squareHandlerWithValidation(ServerRequest serverRequest) {
        int input = Integer.parseInt(serverRequest.pathVariable("input"));
        if (input < 10 || input > 20) {
            return Mono.error(new InputValidationException(input));
        }
        Mono<Response> responseMono = this.mathService.findSquareReactive(input);
        return ServerResponse.ok().body(responseMono, Response.class);
    }
}
