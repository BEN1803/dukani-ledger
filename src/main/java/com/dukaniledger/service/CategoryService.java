package com.dukaniledger.service;

import com.dukaniledger.dto.CategoryRequest;
import com.dukaniledger.dto.CategoryResponse;
import com.dukaniledger.entity.Category;
import com.dukaniledger.entity.User;
import com.dukaniledger.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final BusinessContextService businessContextService;

    @CacheEvict(value = "categories", allEntries = true)
    public Category resolveOrCreateCategory(String name, User owner){
        return categoryRepository
                .findByNameIgnoreCaseAndOwnerId(name, owner.getId())
                .orElseGet(
                        () -> categoryRepository.save(
                                Category.builder()
                                        .name(name)
                                        .owner(owner)
                                        .build()
                        )
                );
    }

    @CacheEvict(value = "categories", allEntries = true)
    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public CategoryResponse createCategory(CategoryRequest request){
        User owner = businessContextService.getOwnerForCurrentUser();
        Category category = new Category();
        return mapToResponse(category);
    }

    @Cacheable(value = "categories", key = "#root.target.currentOwnerId()")
    public List<CategoryResponse> getCategories(){
        User owner = businessContextService.getOwnerForCurrentUser();
        return categoryRepository.findByOwnerId(owner.getId())
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public Long currentOwnerId(){
        return businessContextService.getOwnerForCurrentUser().getId();
    }

    private CategoryResponse mapToResponse(Category category){
        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .ownerName(category.getOwner() != null ? category.getOwner().getEmail() : "Unknown")
                .createdAt(category.getCreatedAt())
                .updatedAt(category.getUpdatedAt())
                .build();
    }

}
