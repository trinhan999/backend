package com.onlineshop.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderResponse {
    private Long orderId;
    private String status;
    private BigDecimal total;
    private String createdAt;
    private String shippingName;
    private String shippingAddress;
    private String shippingCity;
    private String shippingZip;
    private String shippingCountry;
    private String shippingPhone;
    private String paymentMethod;
    private String paymentStatus;
    private List<OrderItemDto> items;

    @Data
    public static class OrderItemDto {
        private Long productId;
        private String productName;
        private Integer quantity;
        private BigDecimal price;
    }
} 