package com.unipi.productservice.service;

import com.unipi.productservice.dto.ProductDto;
import com.unipi.productservice.repository.ProductRepository;
import com.unipi.productservice.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Range;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class ProductService {

    private ProductRepository repository;

    // Whenever we add a product, we broadcast the new product to frontend
    // We use it in insertProduct() method
    /** @see com.unipi.productservice.config.SinkConfig */
    private Sinks.Many<ProductDto> sink;

    @Autowired
    public ProductService(ProductRepository repository, Sinks.Many<ProductDto> sink) {
        this.repository = repository;
        this.sink = sink;
    }

    public Flux<ProductDto> getAll() {
        return this.repository.findAll()
                .map(EntityDtoUtil::productToDto);
    }

    public Mono<ProductDto> getProductById(String id) {
        return this.repository.findById(id)
                .map(EntityDtoUtil::productToDto);
    }

    public Mono<ProductDto> insertProduct(Mono<ProductDto> productDtoMono) {
        return productDtoMono
                .map(EntityDtoUtil::dtoToProduct)
                .flatMap(this.repository::insert) // Returns the updated entity with an id
                .map(EntityDtoUtil::productToDto)
                .doOnNext(this.sink::tryEmitNext); // Emit the item (broadcast)
    }

    public Mono<ProductDto> updateProduct(String id, Mono<ProductDto> productDtoMono) {
        return this.repository.findById(id)
                .flatMap(p -> productDtoMono
                        .map(EntityDtoUtil::dtoToProduct)
                        .doOnNext(e -> e.setId(id)))
                .flatMap(this.repository::save)
                .map(EntityDtoUtil::productToDto);
    }

    public Flux<ProductDto> getProductByPriceRange(double min, double max) {
        return this.repository.findByPriceBetween(Range.closed(min, max))
                .map(EntityDtoUtil::productToDto);
    }

    // In reactive programming, we have to subscribe first in order to work,
    // so we should return a Mono<Void>, otherwise, we only create the pipeline without a subscriber.
    public Mono<Void> deleteProduct(String id) {
        return this.repository.deleteById(id);
    }
}
