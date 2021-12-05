package com.unipi.orderservice;

import com.unipi.orderservice.client.ProductClient;
import com.unipi.orderservice.client.UserClient;
import com.unipi.orderservice.dto.ProductDto;
import com.unipi.orderservice.dto.PurchaseOrderRequestDto;
import com.unipi.orderservice.dto.PurchaseOrderResponseDto;
import com.unipi.orderservice.dto.UserDto;
import com.unipi.orderservice.service.OrderFulfillmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class OrderServiceApplicationTests {

    @Autowired
    private UserClient userClient;

    @Autowired
    private ProductClient productClient;

    @Autowired
    private OrderFulfillmentService fulfillmentService;

    @Test
    void contextLoads() {

        Flux<PurchaseOrderResponseDto> dtoFlux = Flux.zip(userClient.getAllUsers(), productClient.getAllProducts())
                .map(tuple -> buildDto(tuple.getT1(), tuple.getT2()))
                .flatMap(dto -> this.fulfillmentService.processOrder(Mono.just(dto)))
                .doOnNext(System.out::println);

        StepVerifier.create(dtoFlux)
                .expectNextCount(6)
                .verifyComplete();
    }

    private PurchaseOrderRequestDto buildDto(UserDto userDto, ProductDto productDto) {
        PurchaseOrderRequestDto dto = new PurchaseOrderRequestDto();
        dto.setUserId(userDto.getId());
        dto.setProductId(productDto.getId());
        return dto;
    }

}
