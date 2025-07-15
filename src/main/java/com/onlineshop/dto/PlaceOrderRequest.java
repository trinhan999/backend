package com.onlineshop.dto;

import lombok.Data;
import java.util.List;

@Data
public class PlaceOrderRequest {
    // Shipping address
    private String shippingName;
    private String shippingAddress;
    private String shippingCity;
    private String shippingZip;
    private String shippingCountry;
    private String shippingPhone;

    // Payment info
    private String paymentMethod;
    private String cardNumber; // For mock payment
    private String cardExpiry;
    private String cardCvc;

    // Cart items
    private List<OrderItemDto> items;

    @Data
    public static class OrderItemDto {
        private Long productId;
        private Integer quantity;
    }
} 