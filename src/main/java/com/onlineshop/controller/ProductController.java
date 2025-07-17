package com.onlineshop.controller;

import com.onlineshop.common.ApiResponse;
import com.onlineshop.dto.ProductDto;
import com.onlineshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping
    public ApiResponse<Page<ProductDto>> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Float minRating,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ProductDto> products = productService.getProducts(category, brand, minPrice, maxPrice, minRating, pageable);
        return ApiResponse.success(products);
    }

    @GetMapping("/{id}")
    public ApiResponse<ProductDto> getProductById(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        if (product == null) {
            return ApiResponse.error("Product not found");
        }
        return ApiResponse.success(product);
    }

    @GetMapping("/name/{name}")
    public ApiResponse<ProductDto> getProductByName(@PathVariable String name) {
        ProductDto product = productService.getProductByName(name);
        if (product == null) {
            return ApiResponse.error("Product not found");
        }
        return ApiResponse.success(product);
    }

    @GetMapping("/categories")
    public ApiResponse<List<String>> getCategories() {
        return ApiResponse.success(productService.getAllCategories());
    }

    @GetMapping("/brands")
    public ApiResponse<List<String>> getBrands() {
        return ApiResponse.success(productService.getAllBrands());
    }
} 