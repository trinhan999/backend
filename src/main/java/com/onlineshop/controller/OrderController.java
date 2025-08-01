package com.onlineshop.controller;

import com.onlineshop.common.ApiResponse;
import com.onlineshop.dto.PlaceOrderRequest;
import com.onlineshop.dto.OrderResponse;
import com.onlineshop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> placeOrder(@RequestBody PlaceOrderRequest request) {
        try {
            Long userId = getCurrentUserId();
            OrderResponse response = orderService.placeOrder(userId, request);
            return ResponseEntity.ok(ApiResponse.success(response, "Order placed successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getOrdersForUser() {
        try {
            Long userId = getCurrentUserId();
            List<OrderResponse> orders = orderService.getOrdersByUserId(userId);
            return ResponseEntity.ok(ApiResponse.success(orders, "Orders fetched successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderById(@PathVariable Long orderId) {
        try {
            Long userId = getCurrentUserId();
            OrderResponse order = orderService.getOrderByIdAndUserId(orderId, userId);
            return ResponseEntity.ok(ApiResponse.success(order, "Order fetched successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof com.onlineshop.security.CustomUserDetails customUserDetails) {
            return customUserDetails.getId();
        }
        throw new RuntimeException("Invalid authentication principal");
    }
} 