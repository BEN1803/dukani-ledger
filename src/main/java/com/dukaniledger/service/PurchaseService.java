package com.dukaniledger.service;

import com.dukaniledger.dto.PurchaseRequest;
import com.dukaniledger.dto.PurchaseResponse;
import com.dukaniledger.entity.Category;
import com.dukaniledger.entity.Product;
import com.dukaniledger.entity.Purchase;
import com.dukaniledger.entity.User;
import com.dukaniledger.repository.PurchaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final StockService stockService;
    private final BusinessContextService businessContextService;
    private final CurrentUserService currentUserService;
    private final ActivityLogService activityLogService;


    @Transactional
    public PurchaseResponse createPurchase(PurchaseRequest request){
        User owner = businessContextService.getOwnerForCurrentUser();
        User currentUser = currentUserService.getCurrentUser();

        Category category = categoryService.resolveOrCreateCategory(request.getCategoryName(), owner);

        Product product = productService.resolveOrCreateProduct(
                request.getProductName(),
                category,
                request.getCostPrice(),
                owner
        );

        Purchase purchase = Purchase.builder()
                .product(request.getProductName())
                .category(request.getCategoryName())
                .quantity(request.getQuantity())
                .costPrice(request.getCostPrice())
                .purchasedBy(currentUser)
                .productReference(product)
                .build();

        Purchase saved = purchaseRepository.save(purchase);

        stockService.increaseStock(product, request.getQuantity());

        activityLogService.log("CREATE_PURCHASE", "Purchase#" + saved.getId());

        return mapToResponse(saved);
    }

    public Page<PurchaseResponse> getPurchases(int page, int size){
        User owner = businessContextService.getOwnerForCurrentUser();
        return purchaseRepository
                .findByProductReference_Category_Owner_Id(owner.getId(), PageRequest.of(page, size))
                .map(this::mapToResponse);
    }

    public PurchaseResponse getPurchaseById(Long id){
        Purchase purchase = purchaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Purchase not found"));
        return mapToResponse(purchase);
    }

    private PurchaseResponse mapToResponse(Purchase purchase){
        return PurchaseResponse.builder()
                .id(purchase.getId())
                .productName(purchase.getProduct())
                .category(purchase.getCategory())
                .quantity(purchase.getQuantity())
                .costPrice(purchase.getCostPrice())
                .purchasedAt(purchase.getPurchasedAt())
                .purchasedByName(purchase.getPurchasedBy() != null ? purchase.getPurchasedBy().getEmail() : "Unknown")
                .productRecordId(purchase.getProductReference() != null ? purchase.getProductReference().getId() : null)
                .productCode(purchase.getProductReference() != null ? purchase.getProductReference().getProductId() : null)
                .build();
    }
}