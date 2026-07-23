package com.dukaniledger.service;

import com.dukaniledger.dto.ActivityLogResponse;
import com.dukaniledger.entity.ActivityLog;
import com.dukaniledger.entity.User;
import com.dukaniledger.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityLogService {

    private final ActivityLogRepository activityLogRepository;
    private final CurrentUserService currentUserService;

    // Called from every other service after a meaningful create/update/delete.
    // Deliberately not cached: logs are write-heavy and need to stay exact.
    public void log(String action, String entity){
        User currentUser = currentUserService.getCurrentUser();

        ActivityLog activityLog = ActivityLog.builder()
                .user(currentUser)
                .action(action)
                .entity(entity)
                .build();

        activityLogRepository.save(activityLog);
    }

    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public Page<ActivityLogResponse> getLogsForUser(Long userId, int page, int size){
        return activityLogRepository
                .findByUserIdOrderByTimeStampDesc(userId, PageRequest.of(page, size))
                .map(this::mapToResponse);
    }

    @PreAuthorize("hasRole('OWNER') or hasRole('ADMIN')")
    public Page<ActivityLogResponse> getAllLogs(int page, int size){
        return activityLogRepository
                .findAllByOrderByTimeStampDesc(PageRequest.of(page, size))
                .map(this::mapToResponse);
    }

    private ActivityLogResponse mapToResponse(ActivityLog log){
        return ActivityLogResponse.builder()
                .id(log.getId())
                .userName(log.getUser() != null ? log.getUser().getEmail() : "Unknown")
                .action(log.getAction())
                .entity(log.getEntity())
                .timeStamp(log.getTimeStamp())
                .build();
    }
}