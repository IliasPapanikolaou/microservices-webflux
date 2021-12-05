package com.unipi.productservice.util;

import com.unipi.productservice.dto.ProductDto;
import com.unipi.productservice.model.Product;
import org.springframework.beans.BeanUtils;

public class EntityDtoUtil {

    public static ProductDto productToDto(Product product) {
        ProductDto dto = new ProductDto();
        // Copy all fields instead of using setters for each field
        // (BeanUtils is not so efficient tho)
        BeanUtils.copyProperties(product, dto);
        return dto;
    }

    public static Product dtoToProduct(ProductDto dto) {
        Product product = new Product();
        BeanUtils.copyProperties(dto, product);
        return product;
    }
}
