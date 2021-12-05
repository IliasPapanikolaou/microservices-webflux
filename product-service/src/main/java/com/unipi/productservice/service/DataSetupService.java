package com.unipi.productservice.service;

import com.unipi.productservice.dto.ProductDto;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Mocked MongoDB Data
 * */

@Service
public class DataSetupService implements CommandLineRunner {

    private final ProductService service;

    public DataSetupService(ProductService service) {
        this.service = service;
    }

    @Override
    public void run(String... args) throws Exception {
//        ProductDto p1 = new ProductDto("4k-TV", 1000);
//        ProductDto p2 = new ProductDto("PlayStation5", 550);
//        ProductDto p3 = new ProductDto("Mavic-Air-2", 1100);
//        ProductDto p4 = new ProductDto("iPhone-13-Pro-Max", 1500);
//
//        Flux.just(p1, p2, p3, p4)
//                .flatMap(p -> this.service.insertProduct(Mono.just(p)))
//                .subscribe(System.out::println);

        newProducts()
                .flatMap(p -> this.service.insertProduct(Mono.just(p)))
                .subscribe(System.out::println);

    }

    // Add new random products automatically
    private Flux<ProductDto> newProducts() {
        return Flux.range(1, 1000)
                .delayElements(Duration.ofSeconds(5))
                .map(i -> new ProductDto("product" + i,
                        ThreadLocalRandom.current().nextDouble(10, 100)));
    }
}
