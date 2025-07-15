package com.onlineshop.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String category;
    private String brand;
    private String model;
    private String imageUrl;
    private String specifications;
    private BigDecimal averageRating;
    private Integer reviewCount;
    private String status;
} 