package com.onlineshop.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    // Shipping address fields
    @Column(name = "shipping_name", nullable = false)
    private String shippingName;
    @Column(name = "shipping_address", nullable = false)
    private String shippingAddress;
    @Column(name = "shipping_city", nullable = false)
    private String shippingCity;
    @Column(name = "shipping_zip", nullable = false)
    private String shippingZip;
    @Column(name = "shipping_country", nullable = false)
    private String shippingCountry;
    @Column(name = "shipping_phone")
    private String shippingPhone;

    // Payment info fields
    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;
    @Column(name = "payment_status", nullable = false)
    private String paymentStatus;
    @Column(name = "payment_transaction_id")
    private String paymentTransactionId;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 