package com.onlineshop.service;

import com.onlineshop.dto.UserDto;
import com.onlineshop.dto.UserInfoDto;

import java.util.List;

/**
 * Service interface for User operations
 * 
 * @author PC Component Store Team
 * @version 1.0.0
 */
public interface UserService {
    
    /**
     * Create a new user
     * 
     * @param userDto The user data to create
     * @return Created user DTO
     */
    UserDto createUser(UserDto userDto);
    
    /**
     * Get user by ID
     * 
     * @param id The user ID
     * @return User DTO if found
     */
    UserDto getUserById(Long id);
    
    /**
     * Get user by username
     * 
     * @param username The username
     * @return User DTO if found
     */
    UserDto getUserByUsername(String username);
    
    /**
     * Get all users
     * 
     * @return List of all user DTOs
     */
    List<UserDto> getAllUsers();
    
    /**
     * Update user
     * 
     * @param id The user ID
     * @param userDto The updated user data
     * @return Updated user DTO
     */
    UserDto updateUser(Long id, UserInfoDto userDto);
    
    /**
     * Delete user
     * 
     * @param id The user ID
     */
    void deleteUser(Long id);

    void changePassword(Long id, String currentPassword, String newPassword);
} 