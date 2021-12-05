package com.unipi.webfluxdemo.config;

import com.unipi.webfluxdemo.dto.InputFailedValidationResponse;
import com.unipi.webfluxdemo.exception.InputValidationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

/**
 * --------------------
 * Functional Endpoints
 * --------------------
 * An alternate method, other than REST Controller to expose endpoints
 * */

@Configuration
public class RouterConfig {

    private final RequestHandler requestHandler;

    public RouterConfig(RequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }

//    @Bean
//    public RouterFunction<ServerResponse> serverResponseRouterFunction() {
//        return RouterFunctions.route()
//                .GET("router/square/{input}", requestHandler::squareHandler)
//                .GET("router/table/{input}", requestHandler::tableHandler)
//                .GET("router/table/{input}/stream", requestHandler::tableHandlerStream)
//                .POST("router/multiply", requestHandler::multiplyHandler)
//                .GET("router/square/{input}/validation", requestHandler::squareHandlerWithValidation)
//                .onError(InputValidationException.class, exceptionHandler())
//                .build();
//
//    }
//
//    // We can have multiple Router Functions
//    @Bean
//    public RouterFunction<ServerResponse> serverResponseRouterFunctionAlternate() {
//        return RouterFunctions.route()
//                .GET("router/square/{input}", requestHandler::squareHandler)
//                .GET("router/table/{input}", requestHandler::tableHandler)
//                .GET("router/table/{input}/stream", requestHandler::tableHandlerStream)
//                .POST("router/multiply", requestHandler::multiplyHandler)
//                .GET("router/square/{input}/validation", requestHandler::squareHandlerWithValidation)
//                .onError(InputValidationException.class, exceptionHandler())
//                .build();
//
//    }

    // With High Level Router
//    @Bean
//    public RouterFunction<ServerResponse> highLevelRouter() {
//        return RouterFunctions.route()
//                .path("/router", this::serverResponseRouterFunction)
//                .build();
//    }

    public RouterFunction<ServerResponse> serverResponseRouterFunction() {
        return RouterFunctions.route()
                .GET("square/{input}", requestHandler::squareHandler)
                .GET("table/{input}", requestHandler::tableHandler)
                .GET("table/{input}/stream", requestHandler::tableHandlerStream)
                .POST("multiply", requestHandler::multiplyHandler)
                .GET("square/{input}/validation", requestHandler::squareHandlerWithValidation)
                .onError(InputValidationException.class, exceptionHandler())
                .build();

    }

    // With Request Predicates
    @Bean
    public RouterFunction<ServerResponse> highLevelRouter() {
        return RouterFunctions.route()
                .path("/router", this::serverResponseRouterFunctionRequestPredicates)
                .build();
    }

    public RouterFunction<ServerResponse> serverResponseRouterFunctionRequestPredicates() {
        return RouterFunctions.route()
                // Only 10 - 19 or 30-39 allowed
                .GET("square/{input}", RequestPredicates.path("*/1?")
                        .or(RequestPredicates.path("*/3?")), requestHandler::squareHandler)
                .GET("square/{input}", req -> ServerResponse.badRequest()
                        .bodyValue("Only 10-19 or 30-39 allowed"))

                .GET("table/{input}", requestHandler::tableHandler)
                .GET("table/{input}/stream", requestHandler::tableHandlerStream)
                .POST("multiply", requestHandler::multiplyHandler)
                .GET("square/{input}/validation", requestHandler::squareHandlerWithValidation)
                .onError(InputValidationException.class, exceptionHandler())
                .build();

    }

    // Exception Handler
    private BiFunction<Throwable, ServerRequest, Mono<ServerResponse>> exceptionHandler() {
        return (err, req) -> {
            InputValidationException ex = (InputValidationException) err;
            InputFailedValidationResponse response = new InputFailedValidationResponse();
            response.setInput(ex.getInput());
            response.setMessage(ex.getMessage());
            response.setErrorCode(ex.getErrorCode());
            return ServerResponse.badRequest().bodyValue(response);
        };
    }

}
