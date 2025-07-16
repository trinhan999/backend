package com.onlineshop.controller;

import com.onlineshop.common.ApiResponse;
import com.onlineshop.dto.CreateAdminRequest;
import com.onlineshop.dto.OrderResponse;
import com.onlineshop.dto.ProductDto;
import com.onlineshop.dto.UserDto;
import com.onlineshop.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/dashboard/stats")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDashboardStats() {
        try {
            Map<String, Object> stats = adminService.getDashboardStats();
            return ResponseEntity.ok(ApiResponse.success(stats, "Dashboard statistics retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAllOrders() {
        try {
            List<OrderResponse> orders = adminService.getAllOrders();
            return ResponseEntity.ok(ApiResponse.success(orders, "All orders retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/orders/{orderId}/status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam String status) {
        try {
            OrderResponse order = adminService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok(ApiResponse.success(order, "Order status updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        try {
            List<ProductDto> products = adminService.getAllProducts();
            return ResponseEntity.ok(ApiResponse.success(products, "All products retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/products")
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@RequestBody ProductDto productDto) {
        try {
            ProductDto createdProduct = adminService.createProduct(productDto);
            return ResponseEntity.ok(ApiResponse.success(createdProduct, "Product created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDto productDto) {
        try {
            ProductDto updatedProduct = adminService.updateProduct(id, productDto);
            return ResponseEntity.ok(ApiResponse.success(updatedProduct, "Product updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<ApiResponse<String>> deleteProduct(@PathVariable Long id) {
        try {
            adminService.deleteProduct(id);
            return ResponseEntity.ok(ApiResponse.success("Product deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        try {
            List<UserDto> users = adminService.getAllUsers();
            return ResponseEntity.ok(ApiResponse.success(users, "All users retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PutMapping("/users/{id}/role")
    public ResponseEntity<ApiResponse<UserDto>> updateUserRole(
            @PathVariable Long id,
            @RequestParam String role) {
        try {
            UserDto updatedUser = adminService.updateUserRole(id, role);
            return ResponseEntity.ok(ApiResponse.success(updatedUser, "User role updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<String>> deleteUser(@PathVariable Long id) {
        try {
            adminService.deleteUser(id);
            return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/create-admin")
    public ResponseEntity<ApiResponse<UserDto>> createAdminUser(@RequestBody CreateAdminRequest request) {
        try {
            UserDto createdAdmin = adminService.createAdminUser(request);
            return ResponseEntity.ok(ApiResponse.success(createdAdmin, "Admin user created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
} 