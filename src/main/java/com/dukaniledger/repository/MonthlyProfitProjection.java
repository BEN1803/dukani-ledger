package com.dukaniledger.repository;

import java.math.BigDecimal;

public interface MonthlyProfitProjection {
    Integer getSaleYear();
    Integer getSaleMonth();
    BigDecimal getTotalProfit();
}