package com.onlineshop.service;

import com.onlineshop.dto.PlaceOrderRequest;
import com.onlineshop.dto.OrderResponse;

public interface OrderService {
    OrderResponse placeOrder(Long userId, PlaceOrderRequest request);
} 