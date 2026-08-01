package com.dukaniledger.service;

import com.dukaniledger.dto.SaleRequest;
import com.dukaniledger.dto.SaleResponse;
import com.dukaniledger.entity.Product;
import com.dukaniledger.entity.Sale;
import com.dukaniledger.entity.User;
import com.dukaniledger.repository.ProductRepository;
import com.dukaniledger.repository.SaleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleRepository saleRepository;
    private final ProductRepository productRepository;
    private final StockService stockService;
    private final BusinessContextService businessContextService;
    private final CurrentUserService currentUserService;
    private final ActivityLogService activityLogService;

    @CacheEvict(value = "profits", allEntries = true)
    @Transactional
    public SaleResponse createSale(SaleRequest request){
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Will throw if there isn't enough stock, before anything is persisted.
        stockService.decreaseStock(product, request.getQuantity());

        User currentUser = currentUserService.getCurrentUser();

        BigDecimal totalPrice = request.getSellingPrice()
                .multiply(BigDecimal.valueOf(request.getQuantity()));

        Sale sale = Sale.builder()
                .product(product)
                .quantity(request.getQuantity())
                .sellingPrice(request.getSellingPrice())
                .buyingPrice(product.getCostPrice())
                .totalPrice(totalPrice)
                .soldBy(currentUser)
                .build();

        Sale saved = saleRepository.save(sale);

        activityLogService.log("SELL_PRODUCT", "Sale#" + saved.getId());

        return mapToResponse(saved);
    }

    public Page<SaleResponse> getSales(int page, int size){
        User owner = businessContextService.getOwnerForCurrentUser();
        return saleRepository
                .findByProduct_Category_Owner_Id(owner.getId(), PageRequest.of(page, size))
                .map(this::mapToResponse);
    }

    public Page<SaleResponse> getSalesForProduct(Long productId, int page, int size){
        return saleRepository
                .findByProductId(productId, PageRequest.of(page, size))
                .map(this::mapToResponse);
    }

    private SaleResponse mapToResponse(Sale sale){
        return SaleResponse.builder()
                .id(sale.getId())
                .productId(sale.getProduct().getId())
                .productName(sale.getProduct().getName())
                .sellingPrice(sale.getSellingPrice())
                .quantity(sale.getQuantity())
                .totalPrice(sale.getTotalPrice())
                .soldByName(sale.getSoldBy() != null ? sale.getSoldBy().getEmail() : "Unknown")
                .soldAt(sale.getSoldAt())
                .build();
    }
}