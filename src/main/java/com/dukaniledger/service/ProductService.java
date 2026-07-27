package com.dukaniledger.service;

import com.dukaniledger.dto.ProductResponse;
import com.dukaniledger.dto.UpdateProductRequest;
import com.dukaniledger.entity.Category;
import com.dukaniledger.entity.Product;
import com.dukaniledger.entity.User;
import com.dukaniledger.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final BusinessContextService businessContextService;
    private final CurrentUserService currentUserService;
    private final ActivityLogService activityLogService;


    @CacheEvict(value = "products", allEntries = true)
    public Product resolveOrCreateProduct(
            String name,
            Category category,
            BigDecimal costPriceFromPurchase,
            User owner
    ){
        Product product = productRepository
                .findByNameIgnoreCaseAndCategory_Owner_Id(name, owner.getId())
                .orElse(null);

        User currentUser = currentUserService.getCurrentUser();

        if (product == null) {
            product = Product.builder()
                    .name(name)
                    .category(category)
                    .costPrice(costPriceFromPurchase)
                    .productId(java.util.UUID.randomUUID().toString())
                    .addedBy(currentUser)
                    .updatedBy(currentUser)
                    .build();

            Product saved = productRepository.save(product);
            saved.setProductId("PROD-" + String.format("%04d", saved.getId()));
            return productRepository.save(saved);
        }


        product.setCostPrice(costPriceFromPurchase);
        product.setCategory(category);
        product.setUpdatedBy(currentUser);
        return productRepository.save(product);
    }

    @CacheEvict(value = "products", allEntries = true)
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public ProductResponse updateProduct(Long id, UpdateProductRequest request){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (request.getSellingPrice() != null) {
            product.setSellingPrice(request.getSellingPrice());
        }

        if (request.getCategoryName() != null && !request.getCategoryName().isBlank()) {
            User owner = businessContextService.getOwnerForCurrentUser();
            Category category = categoryService.resolveOrCreateCategory(request.getCategoryName(), owner);
            product.setCategory(category);
        }

        product.setUpdatedBy(currentUserService.getCurrentUser());

        Product updated = productRepository.save(product);
        activityLogService.log("UPDATE_PRODUCT", "Product#" + updated.getId());

        return mapToResponse(updated);
    }

    public ProductResponse getProductById(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapToResponse(product);
    }

    @Cacheable(value = "products", key = "#root.target.currentOwnerId()")
    public List<ProductResponse> getProducts(){
        User owner = businessContextService.getOwnerForCurrentUser();
        return productRepository.findByCategory_Owner_Id(owner.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    public Long currentOwnerId(){
        return businessContextService.getOwnerForCurrentUser().getId();
    }

    private ProductResponse mapToResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .productId(product.getProductId())
                .name(product.getName())
                .category(product.getCategory() != null ? product.getCategory().getName() : null)
                .costPrice(product.getCostPrice())
                .sellingPrice(product.getSellingPrice())
                .addedByName(product.getAddedBy() != null ? product.getAddedBy().getEmail() : "Unknown")
                .updatedByName(product.getUpdatedBy() != null ? product.getUpdatedBy().getEmail() : "Unknown")
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}