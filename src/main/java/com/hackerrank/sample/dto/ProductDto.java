package com.hackerrank.sample.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ProductDto {
    private String name;
    private String image;
    private String description;
    private Double price;
    private Double rating;
    private String specifications;
}
