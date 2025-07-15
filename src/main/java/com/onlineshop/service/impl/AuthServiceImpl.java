package com.onlineshop.service.impl;

import com.onlineshop.dto.AuthRequest;
import com.onlineshop.dto.AuthResponse;
import com.onlineshop.dto.LoginRequest;
import com.onlineshop.dto.UserDto;
import com.onlineshop.entity.User;
import com.onlineshop.repository.UserRepository;
import com.onlineshop.security.JwtTokenProvider;
import com.onlineshop.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * Authentication service implementation
 */
@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // In a production environment, you would use Redis or a database to store invalidated tokens
    private final Set<String> invalidatedTokens = new HashSet<>();

    @Override
    public AuthResponse register(AuthRequest authRequest) {
        // Check if username or email already exists
        if (userRepository.existsByUsername(authRequest.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        if (userRepository.existsByEmail(authRequest.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        // Create new user
        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setEmail(authRequest.getEmail());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setFirstName(authRequest.getFirstName());
        user.setLastName(authRequest.getLastName());
        user.setRole(User.UserRole.CUSTOMER);

        User savedUser = userRepository.save(user);

        // Convert to DTO
        UserDto userDto = convertToDto(savedUser);

        // Generate JWT token
        String token = jwtTokenProvider.generateTokenFromUsername(savedUser.getUsername());

        return new AuthResponse(userDto, token);
    }

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsernameOrEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Get user details
        User user = userRepository.findByUsernameOrEmail(loginRequest.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDto userDto = convertToDto(user);

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(authentication);

        return new AuthResponse(userDto, token);
    }

    private UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setRole(user.getRole());
        userDto.setCreatedAt(user.getCreatedAt());
        userDto.setUpdatedAt(user.getUpdatedAt());
        return userDto;
    }
} 