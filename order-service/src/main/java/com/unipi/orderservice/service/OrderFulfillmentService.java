package com.unipi.orderservice.service;

import com.unipi.orderservice.client.ProductClient;
import com.unipi.orderservice.client.UserClient;
import com.unipi.orderservice.dto.PurchaseOrderRequestDto;
import com.unipi.orderservice.dto.PurchaseOrderResponseDto;
import com.unipi.orderservice.dto.RequestContext;
import com.unipi.orderservice.repository.PurchaseOrderRepository;
import com.unipi.orderservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class OrderFulfillmentService {

    private PurchaseOrderRepository orderRepository;
    private final ProductClient productClient;
    private final UserClient userClient;

    @Autowired
    public OrderFulfillmentService(PurchaseOrderRepository orderRepository,
                                   ProductClient productClient,
                                   UserClient userClient) {
        this.orderRepository = orderRepository;
        this.productClient = productClient;
        this.userClient = userClient;
    }

    public Mono<PurchaseOrderResponseDto> processOrder(Mono<PurchaseOrderRequestDto> requestDtoMono) {
        // requestDtoMono.map(dto -> new RequestContext(dto))
        // or
        return requestDtoMono.map(RequestContext::new)
                .flatMap(this::productRequestResponse)
                .doOnNext(EntityDtoUtil::setTransactionRequestDto)
                .flatMap(this::userRequestResponse)
                .map(EntityDtoUtil::getPurchaseOrder)
                .map(this.orderRepository::save) // This is a blocking step
                .map(EntityDtoUtil::getPurchaseOrderResponseDto)
                // Important
                // Even if we have blocking operation, it will not be affected using the below command.
                // TODO: Research about the below command
                .subscribeOn(Schedulers.boundedElastic());


    }

    private Mono<RequestContext> productRequestResponse(RequestContext requestContext) {
        return this.productClient.getProductById(requestContext.getPurchaseOrderRequestDto().getProductId())
                .doOnNext(requestContext::setProductDto)
                // TODO: Random Intended Exceptions on User Service - UserController
                // Resilience - Retry Pattern - number of retries: 5
                //.retry(5)
                // Resilience - Retry with delay pattern - number of retries: 5 and 1 sec delay
                .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(1)))
                .thenReturn(requestContext);
    }

    private Mono<RequestContext> userRequestResponse(RequestContext requestContext) {
        return this.userClient.authorizeTransaction(requestContext.getTransactionRequestDto())
                .doOnNext(requestContext::setTransactionResponseDto)
                .thenReturn(requestContext);
    }
}
