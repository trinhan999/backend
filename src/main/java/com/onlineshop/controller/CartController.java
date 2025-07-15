package com.onlineshop.controller;

import com.onlineshop.common.ApiResponse;
import com.onlineshop.dto.CartDto;
import com.onlineshop.dto.AddToCartRequest;
import com.onlineshop.dto.UpdateCartItemRequest;
import com.onlineshop.service.CartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for Cart operations
 * 
 * @author PC Component Store Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "*")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    /**
     * Get user's cart
     * 
     * @return ResponseEntity with cart data
     */
    @GetMapping
    public ResponseEntity<ApiResponse<CartDto>> getCart() {
        try {
            Long userId = getCurrentUserId();
            CartDto cart = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(ApiResponse.success(cart));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Add item to cart
     * 
     * @param request The add to cart request
     * @return ResponseEntity with updated cart data
     */
    @PostMapping("/add")
    public ResponseEntity<ApiResponse<CartDto>> addToCart(@Valid @RequestBody AddToCartRequest request) {
        try {
            Long userId = getCurrentUserId();
            CartDto cart = cartService.addItemToCart(userId, request);
            return ResponseEntity.ok(ApiResponse.success(cart, "Item added to cart successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Update cart item quantity
     * 
     * @param productId The product ID
     * @param request The update request
     * @return ResponseEntity with updated cart data
     */
    @PutMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<CartDto>> updateCartItem(
            @PathVariable Long productId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        try {
            Long userId = getCurrentUserId();
            CartDto cart = cartService.updateCartItem(userId, productId, request);
            return ResponseEntity.ok(ApiResponse.success(cart, "Cart item updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Remove item from cart
     * 
     * @param productId The product ID
     * @return ResponseEntity with updated cart data
     */
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<CartDto>> removeFromCart(@PathVariable Long productId) {
        try {
            Long userId = getCurrentUserId();
            CartDto cart = cartService.removeItemFromCart(userId, productId);
            return ResponseEntity.ok(ApiResponse.success(cart, "Item removed from cart successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    /**
     * Clear cart
     * 
     * @return ResponseEntity with empty cart data
     */
    @DeleteMapping
    public ResponseEntity<ApiResponse<CartDto>> clearCart() {
        try {
            Long userId = getCurrentUserId();
            CartDto cart = cartService.clearCart(userId);
            return ResponseEntity.ok(ApiResponse.success(cart, "Cart cleared successfully"));
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