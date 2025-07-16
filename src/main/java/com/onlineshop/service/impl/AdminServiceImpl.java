package com.onlineshop.service.impl;

import com.onlineshop.dto.CreateAdminRequest;
import com.onlineshop.dto.OrderResponse;
import com.onlineshop.dto.ProductDto;
import com.onlineshop.dto.UserDto;
import com.onlineshop.entity.Order;
import com.onlineshop.entity.Product;
import com.onlineshop.entity.User;
import com.onlineshop.repository.OrderRepository;
import com.onlineshop.repository.ProductRepository;
import com.onlineshop.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.onlineshop.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Total sales
        BigDecimal totalSales = orderRepository.findAll().stream()
                .map(Order::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        stats.put("totalSales", totalSales);
        
        // Total orders
        long totalOrders = orderRepository.count();
        stats.put("totalOrders", totalOrders);
        
        // Orders today
        long ordersToday = orderRepository.findByCreatedAtAfter(LocalDate.now().atStartOfDay()).size();
        stats.put("ordersToday", ordersToday);
        
        // Total customers
        long totalCustomers = userRepository.countByRole(User.UserRole.CUSTOMER);
        stats.put("totalCustomers", totalCustomers);
        
        // Total products
        long totalProducts = productRepository.count();
        stats.put("totalProducts", totalProducts);
        
        // Low stock products (less than 10 items)
        long lowStockProducts = productRepository.findByStockQuantityLessThan(10).size();
        stats.put("lowStockProducts", lowStockProducts);
        
        // Recent orders (last 5)
        List<OrderResponse> recentOrders = orderRepository.findTop5ByOrderByCreatedAtDesc()
                .stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
        stats.put("recentOrders", recentOrders);
        
        return stats;
    }

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponse updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        Order savedOrder = orderRepository.save(order);
        return convertToOrderResponse(savedOrder);
    }

    @Override
    public List<ProductDto> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToProductDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto createProduct(ProductDto productDto) {
        Product product = convertToProduct(productDto);
        Product savedProduct = productRepository.save(product);
        return convertToProductDto(savedProduct);
    }

    @Override
    public ProductDto updateProduct(Long id, ProductDto productDto) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        existingProduct.setName(productDto.getName());
        existingProduct.setDescription(productDto.getDescription());
        existingProduct.setPrice(productDto.getPrice());
        existingProduct.setStockQuantity(productDto.getStockQuantity());
        existingProduct.setCategory(productDto.getCategory());
        existingProduct.setBrand(productDto.getBrand());
        existingProduct.setImageUrl(productDto.getImageUrl());
        
        Product savedProduct = productRepository.save(existingProduct);
        return convertToProductDto(savedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUserRole(Long id, String role) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(User.UserRole.valueOf(role.toUpperCase()));
        User savedUser = userRepository.save(user);
        return convertToUserDto(savedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDto createAdminUser(CreateAdminRequest request) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        // Create new admin user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRole(User.UserRole.ADMIN);

        User savedUser = userRepository.save(user);
        return convertToUserDto(savedUser);
    }

    private OrderResponse convertToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setOrderId(order.getId());
        response.setStatus(order.getStatus());
        response.setTotal(order.getTotal());
        response.setCreatedAt(order.getCreatedAt().toString());
        response.setShippingName(order.getShippingName());
        response.setShippingAddress(order.getShippingAddress());
        response.setShippingCity(order.getShippingCity());
        response.setShippingZip(order.getShippingZip());
        response.setShippingCountry(order.getShippingCountry());
        response.setShippingPhone(order.getShippingPhone());
        response.setPaymentMethod(order.getPaymentMethod());
        response.setPaymentStatus(order.getPaymentStatus());
        response.setItems(order.getItems().stream()
                .map(item -> {
                    OrderResponse.OrderItemDto itemDto = new OrderResponse.OrderItemDto();
                    itemDto.setProductId(item.getProduct().getId());
                    itemDto.setProductName(item.getProduct().getName());
                    itemDto.setQuantity(item.getQuantity());
                    itemDto.setPrice(item.getPrice());
                    return itemDto;
                })
                .collect(Collectors.toList()));
        return response;
    }

    private ProductDto convertToProductDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockQuantity(product.getStockQuantity());
        dto.setCategory(product.getCategory());
        dto.setBrand(product.getBrand());
        dto.setImageUrl(product.getImageUrl());
        dto.setAverageRating(product.getAverageRating());
        dto.setReviewCount(product.getReviewCount());
        dto.setStatus(product.getStatus().name());
        return dto;
    }

    private Product convertToProduct(ProductDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        product.setCategory(dto.getCategory());
        product.setBrand(dto.getBrand());
        product.setImageUrl(dto.getImageUrl());
        if (dto.getAverageRating() != null) {
            product.setAverageRating(dto.getAverageRating());
        }
        if (dto.getReviewCount() != null) {
            product.setReviewCount(dto.getReviewCount());
        }
        return product;
    }

    private UserDto convertToUserDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
} 