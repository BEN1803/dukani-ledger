package com.dukaniledger.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;



@Entity
@Table(name = "stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_id",
            referencedColumnName = "product_id",
            unique = true
    )
    private Product product;

    @Column(name = "quantity_available")
    private Integer quantityAvailable;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist(){

        updatedAt = LocalDateTime.now();

    }

    @PreUpdate
    public void preUpdate(){

        updatedAt = LocalDateTime.now();

    }

}