package com.dukaniledger.repository;

import com.dukaniledger.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByProductId(String productId);


    Optional<Product> findByNameIgnoreCaseAndCategory_Owner_Id(String name, Long ownerId);

    List<Product> findByCategory_Owner_Id(Long ownerId);

    List<Product> findByCategoryId(Long categoryId);
}