package com.dukaniledger.controller;

import com.dukaniledger.dto.WorkerRequest;
import com.dukaniledger.entity.Worker;
import com.dukaniledger.service.WorkerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/workers")
@RequiredArgsConstructor
public class WorkerController {
    private final WorkerService workerService;

    @PostMapping
    public Worker addWorker(
            @RequestBody WorkerRequest request,
            Authentication authentication
            ){
        return workerService.addWorker(
                request,
                authentication.getName()
        );
    }
}
