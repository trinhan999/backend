package com.onlineshop.service.impl;

import com.onlineshop.dto.CartDto;
import com.onlineshop.dto.CartItemDto;
import com.onlineshop.dto.AddToCartRequest;
import com.onlineshop.dto.UpdateCartItemRequest;
import com.onlineshop.entity.Cart;
import com.onlineshop.entity.CartItem;
import com.onlineshop.entity.Product;
import com.onlineshop.entity.User;
import com.onlineshop.repository.CartRepository;
import com.onlineshop.repository.CartItemRepository;
import com.onlineshop.repository.ProductRepository;
import com.onlineshop.repository.UserRepository;
import com.onlineshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartServiceImpl implements CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    @Override
    public CartDto getCartByUserId(Long userId) {
        Cart cart = getOrCreateCart(userId);
        return toDto(cart);
    }
    
    @Override
    public CartDto addItemToCart(Long userId, AddToCartRequest request) {
        Cart cart = getOrCreateCart(userId);
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        // Check if item already exists in cart
        CartItem existingItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), request.getProductId())
                .orElse(null);
        
        if (existingItem != null) {
            // Update quantity
            existingItem.setQuantity(existingItem.getQuantity() + request.getQuantity());
            cartItemRepository.save(existingItem);
        } else {
            // Create new cart item
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItemRepository.save(cartItem);
        }
        
        // Refresh cart
        cart = cartRepository.findById(cart.getId()).orElseThrow();
        return toDto(cart);
    }
    
    @Override
    public CartDto updateCartItem(Long userId, Long productId, UpdateCartItemRequest request) {
        Cart cart = getOrCreateCart(userId);
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        
        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);
        
        // Refresh cart
        cart = cartRepository.findById(cart.getId()).orElseThrow();
        return toDto(cart);
    }
    
    @Override
    public CartDto removeItemFromCart(Long userId, Long productId) {
        Cart cart = getOrCreateCart(userId);
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cart.getId(), productId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartItemRepository.delete(cartItem);
        // Force refresh to avoid stale items list
        entityManager.flush();
        entityManager.clear();
        cart = cartRepository.findById(cart.getId()).orElseThrow();
        return toDto(cart);
    }
    
    @Override
    public CartDto clearCart(Long userId) {
        Cart cart = getOrCreateCart(userId);
        cartItemRepository.deleteByCartId(cart.getId());

        // Force refresh to avoid stale items list
        entityManager.flush();
        entityManager.clear();
        cart = cartRepository.findById(cart.getId()).orElseThrow();
        return toDto(cart);
    }
    
    private Cart getOrCreateCart(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("User not found"));
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }
    
    private CartDto toDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUser().getId());
        dto.setItems(cart.getItems().stream().map(this::toItemDto).collect(Collectors.toList()));
        dto.setTotal(cart.getTotal());
        dto.setItemCount(cart.getItemCount());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setUpdatedAt(cart.getUpdatedAt());
        return dto;
    }
    
    private CartItemDto toItemDto(CartItem item) {
        CartItemDto dto = new CartItemDto();
        dto.setId(item.getId());
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setProductImageUrl(item.getProduct().getImageUrl());
        dto.setProductPrice(item.getProduct().getPrice());
        dto.setQuantity(item.getQuantity());
        dto.setSubtotal(item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
        dto.setCreatedAt(item.getCreatedAt());
        dto.setUpdatedAt(item.getUpdatedAt());
        return dto;
    }
} 