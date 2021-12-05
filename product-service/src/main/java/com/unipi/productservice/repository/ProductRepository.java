package com.unipi.productservice.repository;

import com.unipi.productservice.model.Product;
import org.springframework.data.domain.Range;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ProductRepository extends ReactiveMongoRepository<Product, String> {

    // > min && < max
    // Flux<Product> findByPriceBetween(int min, int max);

    // >= min && <= max
    Flux<Product> findByPriceBetween(Range<Double> range);

}
