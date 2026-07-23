package com.dukaniledger.repository;

import com.dukaniledger.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    Page<Sale> findByProduct_Category_Owner_Id(Long ownerId, Pageable pageable);

    Page<Sale> findByProductId(Long productId, Pageable pageable);
}