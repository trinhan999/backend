package com.onlineshop.repository;

import com.onlineshop.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    /**
     * Find all reviews for a specific product
     * 
     * @param productId The product ID
     * @return List of reviews for the product
     */
    List<Review> findByProductIdOrderByCreatedAtDesc(Long productId);
    
    /**
     * Find a review by user and product (to check if user already reviewed)
     * 
     * @param userId The user ID
     * @param productId The product ID
     * @return Optional review if exists
     */
    Optional<Review> findByUserIdAndProductId(Long userId, Long productId);
    
    /**
     * Count reviews for a specific product
     * 
     * @param productId The product ID
     * @return Number of reviews
     */
    long countByProductId(Long productId);
    
    /**
     * Calculate average rating for a product
     * 
     * @param productId The product ID
     * @return Average rating or null if no reviews
     */
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.product.id = :productId")
    Double getAverageRatingByProductId(@Param("productId") Long productId);
} 