package com.dukaniledger.repository;

import com.dukaniledger.entity.Worker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WorkerRepository extends JpaRepository<Worker, Long> {
    List<Worker> findByBusinessId(Long businessId);
}
