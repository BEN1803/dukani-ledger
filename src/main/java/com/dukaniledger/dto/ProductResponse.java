package com.dukaniledger.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String productId;
    private String name;
    private String category;

    // Synced automatically from purchases.
    private BigDecimal costPrice;

    // The only field set manually here.
    private BigDecimal sellingPrice;

    private String addedByName;
    private String updatedByName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}