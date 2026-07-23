package com.dukaniledger.repository;

import java.math.BigDecimal;

public interface ProductProfitProjection {
    Long getProductId();
    String getProductName();
    Long getQuantitySold();
    BigDecimal getTotalRevenue();
    BigDecimal getTotalCost();
    BigDecimal getTotalProfit();
}