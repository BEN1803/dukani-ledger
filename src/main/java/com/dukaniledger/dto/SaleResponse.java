package com.dukaniledger.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class SaleResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal sellingPrice;
    private Integer quantity;
    private BigDecimal totalPrice;
    private String soldByName;
    private LocalDateTime soldAt;
}