package com.onlineshop.service.impl;

import com.onlineshop.dto.ProductDto;
import com.onlineshop.entity.Product;
import com.onlineshop.repository.ProductRepository;
import com.onlineshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Override
    public Page<ProductDto> getProducts(String category, String brand, BigDecimal minPrice, BigDecimal maxPrice, Float minRating, Pageable pageable) {
        Specification<Product> spec = Specification.where(null);
        if (category != null && !category.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("category"), category));
        }
        if (brand != null && !brand.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("brand"), brand));
        }
        if (minPrice != null) {
            spec = spec.and((root, query, cb) -> cb.ge(root.get("price"), minPrice));
        }
        if (maxPrice != null) {
            spec = spec.and((root, query, cb) -> cb.le(root.get("price"), maxPrice));
        }
        if (minRating != null && minRating > 0) {
            spec = spec.and((root, query, cb) -> cb.ge(root.get("averageRating"), BigDecimal.valueOf(minRating)));
        }
        return productRepository.findAll(spec, pageable).map(this::toDto);
    }

    @Override
    public ProductDto getProductById(Long id) {
        return productRepository.findById(id).map(this::toDto).orElse(null);
    }

    @Override
    public ProductDto getProductByName(String name) {
        return productRepository.findByNameIgnoreCase(name).map(this::toDto).orElse(null);
    }

    @Override
    public List<String> getAllCategories() {
        return productRepository.findAll().stream()
                .map(Product::getCategory)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllBrands() {
        return productRepository.findAll().stream()
                .map(Product::getBrand)
                .filter(b -> b != null && !b.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    private ProductDto toDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setCategory(product.getCategory());
        dto.setBrand(product.getBrand());
        dto.setModel(product.getModel());
        dto.setImageUrl(product.getImageUrl());
        dto.setSpecifications(product.getSpecifications());
        dto.setAverageRating(product.getAverageRating());
        dto.setReviewCount(product.getReviewCount());
        dto.setStatus(product.getStatus().name());
        return dto;
    }
} 