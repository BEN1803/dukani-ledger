package com.dukaniledger.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class StockResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long productId;
    private String productName;
    private String productCode;
    private Integer quantityAvailable;
    private LocalDateTime updatedAt;
}