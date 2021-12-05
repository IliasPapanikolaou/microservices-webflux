package com.unipi.orderservice.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class ProductDto {

    private String id;
    private String description;
    private Double price;

    public ProductDto(String description, Double price) {
        this.description = description;
        this.price = price;
    }
}
