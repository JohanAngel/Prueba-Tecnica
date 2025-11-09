package com.hackerrank.sample.mapper;

import com.hackerrank.sample.dto.ProductDto;
import com.hackerrank.sample.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public Product toEntity(ProductDto productDto){
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setRating(productDto.getRating());
        product.setImage(productDto.getImage());
        product.setSpecifications(productDto.getSpecifications());
        return product;
    }

    public ProductDto toDto(Product product){
        ProductDto productDto = new ProductDto();
        productDto.setName(product.getName());
        productDto.setDescription(product.getDescription());
        productDto.setPrice(product.getPrice());
        productDto.setRating(product.getRating());
        productDto.setImage(product.getImage());
        productDto.setSpecifications(product.getSpecifications());
        return productDto;
    }
}
