package com.dukaniledger.controller;

import com.dukaniledger.dto.SaleRequest;
import com.dukaniledger.dto.SaleResponse;
import com.dukaniledger.service.SaleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SaleResponse createSale(
            @Valid @RequestBody SaleRequest request
    ){
        return saleService.createSale(request);
    }

    @GetMapping
    public Page<SaleResponse> getSales(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return saleService.getSales(page, size);
    }

    @GetMapping("/product/{productId}")
    public Page<SaleResponse> getSalesForProduct(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return saleService.getSalesForProduct(productId, page, size);
    }
}