package com.dukaniledger.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",  nullable = false)
    private Product product;

    private Integer quantity;

    @Column(name = "selling_price")
    private BigDecimal sellingPrice;

    @Column(name = "buying_price")
    private BigDecimal buyingPrice;

    private BigDecimal totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sold_by", nullable = false)
    private User soldBy;

    @CreationTimestamp
    private LocalDateTime soldAt;
}
