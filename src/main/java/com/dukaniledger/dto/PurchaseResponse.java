package com.dukaniledger.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PurchaseResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String productName;
    private String category;
    private Integer quantity;
    private BigDecimal costPrice;
    private LocalDateTime purchasedAt;
    private String purchasedByName;

    // The product this purchase resolved to / created.
    private Long productRecordId;
    private String productCode;
}