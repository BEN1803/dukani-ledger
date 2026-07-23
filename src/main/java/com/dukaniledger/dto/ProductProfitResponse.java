package com.dukaniledger.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class ProductProfitResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long productId;
    private String productName;
    private Long quantitySold;
    private BigDecimal totalRevenue;
    private BigDecimal totalCost;
    private BigDecimal totalProfit;
}