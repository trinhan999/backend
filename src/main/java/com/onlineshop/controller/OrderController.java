package com.onlineshop.controller;

import com.onlineshop.dto.PlaceOrderRequest;
import com.onlineshop.dto.OrderResponse;
import com.onlineshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody PlaceOrderRequest request) {
        // TODO: Replace with actual user ID from authentication context
        Long userId = 1L;
        OrderResponse response = orderService.placeOrder(userId, request);
        return ResponseEntity.ok(response);
    }
} 