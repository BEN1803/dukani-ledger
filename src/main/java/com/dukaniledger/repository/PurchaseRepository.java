package com.dukaniledger.repository;

import com.dukaniledger.entity.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {

    List<Purchase> findByProductReferenceId(Long productId);

    Page<Purchase> findByProductReference_Category_Owner_Id(Long ownerId, Pageable pageable);
}