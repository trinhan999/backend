package com.onlineshop.service.impl;

import com.onlineshop.dto.ReviewDto;
import com.onlineshop.dto.CreateReviewRequest;
import com.onlineshop.entity.Review;
import com.onlineshop.entity.Product;
import com.onlineshop.entity.User;
import com.onlineshop.repository.ReviewRepository;
import com.onlineshop.repository.ProductRepository;
import com.onlineshop.repository.UserRepository;
import com.onlineshop.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewServiceImpl implements ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    @Transactional
    public ReviewDto createReview(Long userId, CreateReviewRequest request) {
        // Check if user has already reviewed this product
        if (hasUserReviewedProduct(userId, request.getProductId())) {
            throw new RuntimeException("You have already reviewed this product");
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        Review review = new Review();
        review.setUser(user);
        review.setProduct(product);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        
        Review savedReview = reviewRepository.save(review);
        
        // Update product's average rating and review count
        updateProductRating(product.getId());
        
        return toDto(savedReview);
    }
    
    @Override
    public List<ReviewDto> getReviewsByProductId(Long productId) {
        List<Review> reviews = reviewRepository.findByProductIdOrderByCreatedAtDesc(productId);
        return reviews.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public ReviewDto getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .map(this::toDto)
                .orElse(null);
    }
    
    @Override
    @Transactional
    public ReviewDto updateReview(Long reviewId, Long userId, CreateReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        
        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only update your own reviews");
        }
        
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        
        Review updatedReview = reviewRepository.save(review);
        
        // Update product's average rating
        updateProductRating(review.getProduct().getId());
        
        return toDto(updatedReview);
    }
    
    @Override
    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        
        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("You can only delete your own reviews");
        }
        
        Long productId = review.getProduct().getId();
        reviewRepository.delete(review);
        
        // Update product's average rating
        updateProductRating(productId);
    }
    
    @Override
    public boolean hasUserReviewedProduct(Long userId, Long productId) {
        return reviewRepository.findByUserIdAndProductId(userId, productId).isPresent();
    }
    
    /**
     * Update the product's average rating and review count
     * 
     * @param productId The product ID
     */
    private void updateProductRating(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        Double averageRating = reviewRepository.getAverageRatingByProductId(productId);
        long reviewCount = reviewRepository.countByProductId(productId);
        
        if (averageRating != null) {
            product.setAverageRating(BigDecimal.valueOf(averageRating).setScale(2, BigDecimal.ROUND_HALF_UP));
        } else {
            product.setAverageRating(BigDecimal.ZERO);
        }
        
        product.setReviewCount((int) reviewCount);
        productRepository.save(product);
    }
    
    private ReviewDto toDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setId(review.getId());
        dto.setUserId(review.getUser().getId());
        dto.setUsername(review.getUser().getUsername());
        dto.setProductId(review.getProduct().getId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());
        return dto;
    }
} 