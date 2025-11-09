package com.hackerrank.sample.model;

import lombok.Data;

import java.util.Map;

@Data
public class Product {
    private Long id;
    private String name;
    private String image;
    private String description;
    private Double price;
    private Double rating;
    private String specifications;
}
