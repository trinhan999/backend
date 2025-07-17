package com.onlineshop.service;

import com.onlineshop.dto.ReviewDto;
import com.onlineshop.dto.CreateReviewRequest;

import java.util.List;

public interface ReviewService {
    
    /**
     * Create a new review for a product
     * 
     * @param userId The user ID creating the review
     * @param request The review request data
     * @return The created review DTO
     */
    ReviewDto createReview(Long userId, CreateReviewRequest request);
    
    /**
     * Get all reviews for a specific product
     * 
     * @param productId The product ID
     * @return List of review DTOs
     */
    List<ReviewDto> getReviewsByProductId(Long productId);
    
    /**
     * Get a specific review by ID
     * 
     * @param reviewId The review ID
     * @return The review DTO or null if not found
     */
    ReviewDto getReviewById(Long reviewId);
    
    /**
     * Update an existing review
     * 
     * @param reviewId The review ID
     * @param userId The user ID (for authorization)
     * @param request The updated review data
     * @return The updated review DTO
     */
    ReviewDto updateReview(Long reviewId, Long userId, CreateReviewRequest request);
    
    /**
     * Delete a review
     * 
     * @param reviewId The review ID
     * @param userId The user ID (for authorization)
     */
    void deleteReview(Long reviewId, Long userId);
    
    /**
     * Check if a user has already reviewed a product
     * 
     * @param userId The user ID
     * @param productId The product ID
     * @return True if user has already reviewed the product
     */
    boolean hasUserReviewedProduct(Long userId, Long productId);
} 