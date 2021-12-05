package com.unipi.webfluxdemo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("http://localhost:8080")
                // Passing Auth headers
                // .defaultHeaders(h -> h.setBasicAuth("username", "password"))
                // Passing Auth Tokens
                .filter(this::sessionToken)
                // or
                // .filter((clientRequest, exchangeFunction) -> sessionToken(clientRequest, exchangeFunction))
                .build();
    }

    // Select authentication method based on the requested auth method (Attributes)
    private Mono<ClientResponse> sessionToken(ClientRequest request, ExchangeFunction ex) {
        // auth --> basic or auth
        ClientRequest clientRequest = request.attribute("auth")
                .map(v -> v.equals("basic") ? withBasicAuth(request) : withOAuth(request))
                .orElse(request);
        return ex.exchange(clientRequest);
    }

    // Basic Authentication
    private ClientRequest withBasicAuth(ClientRequest request) {
        return ClientRequest.from(request)
                .headers(h -> h.setBasicAuth("username", "password")).build();
    }

    // OAuth Authentication (JWT)
    private ClientRequest withOAuth(ClientRequest request) {
        return ClientRequest.from(request)
                .headers(h -> h.setBearerAuth("some-lengthy-JWT")).build();
    }

    // Mock token generator
//    private Mono<ClientResponse> sessionToken(ClientRequest request, ExchangeFunction ex) {
//        System.out.println("Generating session token");
//        ClientRequest clientRequest = ClientRequest.from(request)
//                .headers(h -> h.setBearerAuth("some-lengthy-JWT"))
//                .build();
//        return ex.exchange(clientRequest);
//    }

}
