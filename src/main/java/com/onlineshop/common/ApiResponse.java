package com.onlineshop.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard API response wrapper for all REST endpoints
 * 
 * @param <T> Type of data being returned
 * @author PC Component Store Team
 * @version 1.0.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String result;    // SUCCESS or ERROR
    private String message;   // success or error message
    private T data;           // return object from service class, if successful

    /**
     * Creates a success response with data
     * 
     * @param data The data to be returned
     * @param <T> Type of data
     * @return ApiResponse with success result
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("SUCCESS", "Operation completed successfully", data);
    }

    /**
     * Creates a success response with custom message
     * 
     * @param data The data to be returned
     * @param message Custom success message
     * @param <T> Type of data
     * @return ApiResponse with success result
     */
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>("SUCCESS", message, data);
    }

    /**
     * Creates an error response
     * 
     * @param message Error message
     * @param <T> Type of data
     * @return ApiResponse with error result
     */
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>("ERROR", message, null);
    }
} 