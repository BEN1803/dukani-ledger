package com.dukaniledger.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateProductRequest {
    @NotBlank(message = "Product name is required")
    private String name;

    @NotBlank(message = "Category is required")
    private String category;

    @DecimalMin(value = "0.01", message = "Sellong price must be greater than 0")
    private BigDecimal sellingPrice;

    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;
}
