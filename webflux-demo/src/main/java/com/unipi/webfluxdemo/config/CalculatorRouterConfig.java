package com.unipi.webfluxdemo.config;

import com.unipi.webfluxdemo.exception.InputValidationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

// Assignment Calculator
// Path: calculator/5/4, Operation based on Header Value OP(+,-,*,/)
@Configuration
public class CalculatorRouterConfig {

    private final CalculatorHandler calculatorHandler;

    public CalculatorRouterConfig(CalculatorHandler calculatorHandler) {
        this.calculatorHandler = calculatorHandler;
    }

    // With Request Predicates
    @Bean
    public RouterFunction<ServerResponse> highLevelRouterCalculator() {
        return RouterFunctions.route()
                .path("/calculator", this::serverResponseRouterFunction)
                .build();
    }

    public RouterFunction<ServerResponse> serverResponseRouterFunction() {
        return RouterFunctions.route()
                .GET("{a}/{b}", isOperation("+"), calculatorHandler::additionHandler)
                .GET("{a}/{b}", isOperation("-"),calculatorHandler::subtractionHandler)
                .GET("{a}/{b}", isOperation("*"),calculatorHandler::multiplicationHandler)
                .GET("{a}/{b}", isOperation("/"),calculatorHandler::divisionHandler)
                .GET("{a}/{b}", req -> ServerResponse.badRequest().bodyValue("OP should be + - * /"))
                .build();

    }

    private RequestPredicate isOperation(String operation) {
        return RequestPredicates.headers(headers -> operation.equals(headers.asHttpHeaders()
                .toSingleValueMap().get("OP")));
    }
}
