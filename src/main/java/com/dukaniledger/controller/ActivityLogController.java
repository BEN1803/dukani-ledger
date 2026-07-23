package com.dukaniledger.controller;

import com.dukaniledger.dto.ActivityLogResponse;
import com.dukaniledger.service.ActivityLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activity-logs")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityLogService activityLogService;

    @GetMapping
    public Page<ActivityLogResponse> getAllLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        return activityLogService.getAllLogs(page, size);
    }

    @GetMapping("/user/{userId}")
    public Page<ActivityLogResponse> getLogsForUser(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ){
        return activityLogService.getLogsForUser(userId, page, size);
    }
}