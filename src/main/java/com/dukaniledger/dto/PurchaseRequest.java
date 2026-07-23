package com.dukaniledger.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseRequest {

    @NotBlank(message = "Product name is required")
    private String productName;

    // Optional: if the category doesn't exist yet for this shop, it is created.
    @NotBlank(message = "Category is required")
    private String categoryName;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Cost price is required")
    @DecimalMin(value = "0.01", message = "Cost price must be greater than 0")
    private BigDecimal costPrice;
}