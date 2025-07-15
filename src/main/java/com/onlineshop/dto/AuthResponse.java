package com.onlineshop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for authentication responses
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private UserDto user;
    private String token;
    private String tokenType = "Bearer";
    
    public AuthResponse(UserDto user, String token) {
        this.user = user;
        this.token = token;
        this.tokenType = "Bearer";
    }
} 