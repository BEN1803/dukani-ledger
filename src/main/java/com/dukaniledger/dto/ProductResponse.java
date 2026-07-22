package com.dukaniledger.dto;

import com.dukaniledger.entity.User;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class ProductResponse implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;

    private String name;

    private String category;

    private BigDecimal sellingPrice;

    private BigDecimal buyingPrice;

    private Integer quantity;

    private String  createdByName;
}
