package com.onlineshop.service;

import com.onlineshop.dto.CartDto;
import com.onlineshop.dto.AddToCartRequest;
import com.onlineshop.dto.UpdateCartItemRequest;

/**
 * Service interface for cart operations
 * 
 * @author PC Component Store Team
 * @version 1.0.0
 */
public interface CartService {
    CartDto getCartByUserId(Long userId);
    CartDto addItemToCart(Long userId, AddToCartRequest request);
    CartDto updateCartItem(Long userId, Long productId, UpdateCartItemRequest request);
    CartDto removeItemFromCart(Long userId, Long productId);
    CartDto clearCart(Long userId);
} 