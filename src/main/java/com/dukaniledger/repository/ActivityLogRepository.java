package com.dukaniledger.repository;

import com.dukaniledger.entity.ActivityLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {

    Page<ActivityLog> findByUserIdOrderByTimeStampDesc(Long userId, Pageable pageable);

    Page<ActivityLog> findAllByOrderByTimeStampDesc(Pageable pageable);
}