package com.dukaniledger.service;

import com.dukaniledger.dto.StockResponse;
import com.dukaniledger.entity.Product;
import com.dukaniledger.entity.Stock;
import com.dukaniledger.entity.User;
import com.dukaniledger.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final BusinessContextService businessContextService;

    // Called by PurchaseService after a purchase is recorded: adds the
    // purchased quantity onto whatever is already in stock for that product.
    @CacheEvict(value = "stock", allEntries = true)
    public void increaseStock(Product product, Integer quantity){
        Stock stock = stockRepository.findByProductId(product.getId())
                .orElse(Stock.builder()
                        .product(product)
                        .quantityAvailable(0)
                        .build()
                );

        stock.setQuantityAvailable(stock.getQuantityAvailable() + quantity);
        stockRepository.save(stock);
    }

    // Called by SaleService after a sale is recorded. Throws if there isn't
    // enough stock so a sale can never push a product negative.
    @CacheEvict(value = "stock", allEntries = true)
    public void decreaseStock(Product product, Integer quantity){
        Stock stock = stockRepository.findByProductId(product.getId())
                .orElseThrow(() -> new RuntimeException("No stock recorded for this product"));

        if (stock.getQuantityAvailable() < quantity) {
            throw new RuntimeException("Not enough stock available for this sale");
        }

        stock.setQuantityAvailable(stock.getQuantityAvailable() - quantity);
        stockRepository.save(stock);
    }

    // Fast-moving data, so this cache is given a short TTL in CacheConfig.
    @Cacheable(value = "stock", key = "#root.target.currentOwnerId()")
    public List<StockResponse> getStock(){
        User owner = businessContextService.getOwnerForCurrentUser();
        return stockRepository.findByProduct_Category_Owner_Id(owner.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Long currentOwnerId(){
        return businessContextService.getOwnerForCurrentUser().getId();
    }

    private StockResponse mapToResponse(Stock stock){
        return StockResponse.builder()
                .id(stock.getId())
                .productId(stock.getProduct().getId())
                .productName(stock.getProduct().getName())
                .productCode(stock.getProduct().getProductId())
                .quantityAvailable(stock.getQuantityAvailable())
                .updatedAt(stock.getUpdatedAt())
                .build();
    }
}