package com.unipi.orderservice.service;

import com.unipi.orderservice.dto.PurchaseOrderResponseDto;
import com.unipi.orderservice.repository.PurchaseOrderRepository;
import com.unipi.orderservice.util.EntityDtoUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Service
public class OrderQueryService {

    private final PurchaseOrderRepository orderRepository;

    public OrderQueryService(PurchaseOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Flux<PurchaseOrderResponseDto> getProductsByUserId(int userId) {
        return Flux.fromStream(() -> this.orderRepository.findByUserId(userId).stream()) // this is blocking
                .map(EntityDtoUtil::getPurchaseOrderResponseDto)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
