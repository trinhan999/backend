package com.onlineshop.service;

import com.onlineshop.dto.CreateAdminRequest;
import com.onlineshop.dto.OrderResponse;
import com.onlineshop.dto.ProductDto;
import com.onlineshop.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface AdminService {
    
    Map<String, Object> getDashboardStats();
    
    List<OrderResponse> getAllOrders();
    
    OrderResponse updateOrderStatus(Long orderId, String status);
    
    List<ProductDto> getAllProducts();
    
    ProductDto createProduct(ProductDto productDto);
    
    ProductDto updateProduct(Long id, ProductDto productDto);
    
    void deleteProduct(Long id);
    
    List<UserDto> getAllUsers();
    
    UserDto updateUserRole(Long id, String role);
    
    void deleteUser(Long id);
    
    UserDto createAdminUser(CreateAdminRequest request);
} 