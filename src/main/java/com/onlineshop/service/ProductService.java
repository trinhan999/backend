package com.onlineshop.service;

import com.onlineshop.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    Page<ProductDto> getProducts(String category, String brand, BigDecimal minPrice, BigDecimal maxPrice, Integer minRating, Pageable pageable);
    ProductDto getProductById(Long id);
    List<String> getAllCategories();
    List<String> getAllBrands();
    ProductDto getProductByName(String name);
} 