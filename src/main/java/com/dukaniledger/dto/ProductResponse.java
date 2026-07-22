package com.dukaniledger.dto;

import com.dukaniledger.entity.User;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductResponse {
    private Long id;

    private String name;

    private String category;

    private BigDecimal sellingPrice;

    private Integer quantity;

    private String  createdByName;
}
