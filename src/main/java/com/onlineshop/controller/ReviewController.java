package com.onlineshop.controller;

import com.onlineshop.common.ApiResponse;
import com.onlineshop.dto.ReviewDto;
import com.onlineshop.dto.CreateReviewRequest;
import com.onlineshop.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewDto>> createReview(@RequestBody CreateReviewRequest request) {
        try {
            Long userId = getCurrentUserId();
            ReviewDto review = reviewService.createReview(userId, request);
            return ResponseEntity.ok(ApiResponse.success(review, "Review created successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/product/{productId}")
    public ResponseEntity<ApiResponse<List<ReviewDto>>> getReviewsByProduct(@PathVariable Long productId) {
        try {
            List<ReviewDto> reviews = reviewService.getReviewsByProductId(productId);
            return ResponseEntity.ok(ApiResponse.success(reviews, "Reviews retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewDto>> getReviewById(@PathVariable Long reviewId) {
        try {
            ReviewDto review = reviewService.getReviewById(reviewId);
            if (review == null) {
                return ResponseEntity.badRequest().body(ApiResponse.error("Review not found"));
            }
            return ResponseEntity.ok(ApiResponse.success(review, "Review retrieved successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @PutMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<ReviewDto>> updateReview(
            @PathVariable Long reviewId,
            @RequestBody CreateReviewRequest request) {
        try {
            Long userId = getCurrentUserId();
            ReviewDto review = reviewService.updateReview(reviewId, userId, request);
            return ResponseEntity.ok(ApiResponse.success(review, "Review updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Void>> deleteReview(@PathVariable Long reviewId) {
        try {
            Long userId = getCurrentUserId();
            reviewService.deleteReview(reviewId, userId);
            return ResponseEntity.ok(ApiResponse.success(null, "Review deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    @GetMapping("/product/{productId}/user-review")
    public ResponseEntity<ApiResponse<Boolean>> hasUserReviewedProduct(@PathVariable Long productId) {
        try {
            Long userId = getCurrentUserId();
            boolean hasReviewed = reviewService.hasUserReviewedProduct(userId, productId);
            return ResponseEntity.ok(ApiResponse.success(hasReviewed, "User review status retrieved"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }
    
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof com.onlineshop.security.CustomUserDetails customUserDetails) {
            return customUserDetails.getId();
        }
        throw new RuntimeException("Invalid authentication principal");
    }
} 