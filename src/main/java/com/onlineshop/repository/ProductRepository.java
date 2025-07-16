package com.onlineshop.repository;

import com.onlineshop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    // Custom queries will be handled via specifications
    Optional<Product> findByNameIgnoreCase(String name);
    
    /**
     * Find products with stock less than specified quantity
     * 
     * @param stockQuantity The stock quantity threshold
     * @return List of products with low stock
     */
    List<Product> findByStockQuantityLessThan(Integer stockQuantity);
} 