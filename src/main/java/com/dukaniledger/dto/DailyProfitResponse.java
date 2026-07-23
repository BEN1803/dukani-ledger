package com.dukaniledger.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
public class DailyProfitResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDate date;
    private BigDecimal totalProfit;
}