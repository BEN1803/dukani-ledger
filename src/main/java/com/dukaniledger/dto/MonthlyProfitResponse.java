package com.dukaniledger.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
public class MonthlyProfitResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer year;
    private Integer month;
    private BigDecimal totalProfit;
}