package com.dukaniledger.service;

import com.dukaniledger.entity.Role;
import com.dukaniledger.entity.User;
import com.dukaniledger.entity.Worker;
import com.dukaniledger.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessContextService {
    private final CurrentUserService currentUserService;
    private final WorkerRepository workerRepository;

    public User getOwnerForCurrentUser() {
        User current = currentUserService.getCurrentUser();

        if (current.getRole() == Role.OWNER) {
            return current;
        }

        Worker worker = workerRepository.findByUserId(current.getId())
                .orElseThrow(
                        () -> new RuntimeException("Worker is not linked to any business")
                );

        return worker.getBusiness().getOwner();
    }
}
