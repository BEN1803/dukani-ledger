package com.dukaniledger.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface DailyProfitProjection {
    LocalDate getSaleDate();
    BigDecimal getTotalProfit();
}