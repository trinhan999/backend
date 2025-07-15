package com.onlineshop.service;

import com.onlineshop.dto.AuthRequest;
import com.onlineshop.dto.AuthResponse;
import com.onlineshop.dto.LoginRequest;

/**
 * Authentication service interface
 */
public interface AuthService {
    
    /**
     * Register a new user
     * 
     * @param authRequest The registration request
     * @return AuthResponse with user info
     */
    AuthResponse register(AuthRequest authRequest);
    
    /**
     * Login user
     * 
     * @param loginRequest The login request
     * @return AuthResponse with user info
     */
    AuthResponse login(LoginRequest loginRequest);
} 