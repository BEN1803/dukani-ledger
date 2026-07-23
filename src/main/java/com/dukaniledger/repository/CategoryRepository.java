package com.dukaniledger.repository;

import com.dukaniledger.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByNameIgnoreCaseAndOwnerId(String name, Long ownerId);

    List<Category> findByOwnerId(Long ownerId);
}