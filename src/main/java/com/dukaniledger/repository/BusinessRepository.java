package com.dukaniledger.repository;

import com.dukaniledger.entity.Business;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusinessRepository extends JpaRepository<Business, Long> {

    Optional<Business> findByOwnerId(Long ownerId);
}
