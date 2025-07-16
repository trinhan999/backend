package com.onlineshop.repository;

import com.onlineshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserId(Long userId);
    
    List<Order> findByCreatedAtAfter(LocalDateTime dateTime);
    
    @Query("SELECT o FROM Order o ORDER BY o.createdAt DESC LIMIT 5")
    List<Order> findTop5ByOrderByCreatedAtDesc();
} 