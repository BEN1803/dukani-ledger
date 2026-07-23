package com.dukaniledger.controller;

import com.dukaniledger.dto.PurchaseRequest;
import com.dukaniledger.dto.PurchaseResponse;
import com.dukaniledger.service.PurchaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/purchases")
@RequiredArgsConstructor
public class PurchaseController {

    private final PurchaseService purchaseService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PurchaseResponse createPurchase(
            @Valid @RequestBody PurchaseRequest request
    ){
        return purchaseService.createPurchase(request);
    }

    @GetMapping("/{id}")
    public PurchaseResponse getPurchase(
            @PathVariable Long id
    ){
        return purchaseService.getPurchaseById(id);
    }

    @GetMapping
    public Page<PurchaseResponse> getPurchases(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return purchaseService.getPurchases(page, size);
    }
}