package com.onlineshop.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * DTO for login requests
 */
@Data
public class LoginRequest {
    
    @NotEmpty(message = "Username or email is required")
    private String usernameOrEmail;
    
    @NotEmpty(message = "Password is required")
    private String password;
} 