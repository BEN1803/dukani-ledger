package com.dukaniledger.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // Example: PROD-001
    @Column(name = "product_id", unique = true, nullable = false)
    private String productId;

    @Column(nullable = false)
    private String name;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;


    @Column(name = "cost_price")
    private BigDecimal costPrice;


    @Column(name = "selling_price")
    private BigDecimal sellingPrice;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by")
    private User addedBy;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    private User updatedBy;


    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @PrePersist
    public void prePersist(){

        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

    }


    @PreUpdate
    public void preUpdate(){

        updatedAt = LocalDateTime.now();

    }

}