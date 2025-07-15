package com.onlineshop.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for Cart data
 * 
 * @author PC Component Store Team
 * @version 1.0.0
 */
@Data
public class CartDto {
    private Long id;
    private Long userId;
    private List<CartItemDto> items;
    private BigDecimal total;
    private Integer itemCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 