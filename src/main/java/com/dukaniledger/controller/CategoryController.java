package com.dukaniledger.controller;

import com.dukaniledger.dto.CategoryRequest;
import com.dukaniledger.dto.CategoryResponse;
import com.dukaniledger.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse createCategory(
            @Valid @RequestBody CategoryRequest request
    ){
        return categoryService.createCategory(request);
    }

    @GetMapping
    public List<CategoryResponse> getCategories(){
        return categoryService.getCategories();
    }
}