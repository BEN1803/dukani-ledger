package com.dukaniledger.controller;

import com.dukaniledger.dto.ProductResponse;
import com.dukaniledger.dto.UpdateProductRequest;
import com.dukaniledger.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    // Products are created only as a side effect of a purchase (see
    // PurchaseController) - there's no direct "create product" endpoint.

    @GetMapping("/{id}")
    public ProductResponse getProduct(
            @PathVariable Long id
    ){
        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    public ProductResponse updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request
    ){
        return productService.updateProduct(id, request);
    }

    @GetMapping
    public List<ProductResponse> getProducts(){
        return productService.getProducts();
    }
}