package com.dukaniledger.service;

import com.dukaniledger.dto.CreateProductRequest;
import com.dukaniledger.dto.ProductResponse;
import com.dukaniledger.entity.Product;
import com.dukaniledger.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CurrentUserService currentUserService;

    public ProductResponse createProduct(CreateProductRequest request){
        if (productRepository.existsByName(request.getName())) {
            throw new RuntimeException("Product already exists.");
        }

        Product product = Product.builder()
                .name(request.getName())
                .category(request.getCategory())
                .sellingPrice(request.getSellingPrice())
                .quantity(request.getQuantity())
                .createdBy(currentUserService.getCurrentUser())
                .build();

        Product savedProduct = productRepository.save(product);

        return ProductResponse.builder()
                .id(savedProduct.getId())
                .name(savedProduct.getName())
                .category(savedProduct.getCategory())
                .sellingPrice(savedProduct.getSellingPrice())
                .quantity(savedProduct.getQuantity())
                .build();
    }

    public List<ProductResponse> getAllProducts(){
        return productRepository.findAll()
                .stream()
                .map(product -> ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .category(product.getCategory())
                        .sellingPrice(product.getSellingPrice())
                        .quantity(product.getQuantity())
                        .createdByName(product.getCreatedBy() != null
                        ? product.getCreatedBy().getEmail()
                                : "Unknown"
                        )
                        .build()
                )
                .collect(Collectors.toList());
    }

    public ProductResponse getProductById(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .sellingPrice(product.getSellingPrice())
                .quantity(product.getQuantity())
                .build();
    }

    public ProductResponse updateProduct(
            Long id,
            CreateProductRequest request
    ) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Product not found")
                );


        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setSellingPrice(request.getSellingPrice());
        product.setQuantity(request.getQuantity());


        Product updatedProduct =
                productRepository.save(product);


        return ProductResponse.builder()
                .id(updatedProduct.getId())
                .name(updatedProduct.getName())
                .category(updatedProduct.getCategory())
                .sellingPrice(updatedProduct.getSellingPrice())
                .quantity(updatedProduct.getQuantity())
                .build();
    }

    public void deleteProduct(Long id){

        if(!productRepository.existsById(id)){
            throw new RuntimeException("Product not found");
        }


        productRepository.deleteById(id);
    }

    @Cacheable(
            value = "products",
            key = "#page + '-' + #size"
    )
    public Page<ProductResponse> getProducts(
            int page,
            int size
    ){

        Pageable pageable =
                PageRequest.of(page, size);


        return productRepository
                .findAll(pageable)
                .map(this::mapToResponse);

    }

    private ProductResponse mapToResponse(
            Product product
    ){

        return ProductResponse.builder()

                .id(product.getId())

                .name(product.getName())

                .category(product.getCategory())

                .sellingPrice(product.getSellingPrice())

                .quantity(product.getQuantity())

                .createdByName(
                        product.getCreatedBy() != null
                                ?
                                product.getCreatedBy().getEmail()
                                :
                                "Unknown"
                )

                .build();
    }
}
