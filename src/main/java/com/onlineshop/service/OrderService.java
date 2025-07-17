package com.onlineshop.service;

import com.onlineshop.dto.PlaceOrderRequest;
import com.onlineshop.dto.OrderResponse;
import java.util.List;

public interface OrderService {
    OrderResponse placeOrder(Long userId, PlaceOrderRequest request);
    List<OrderResponse> getOrdersByUserId(Long userId);
    OrderResponse getOrderByIdAndUserId(Long orderId, Long userId);
} 