package com.dukaniledger.controller;

import com.dukaniledger.dto.BusinessRequest;
import com.dukaniledger.dto.OwnerResponse;
import com.dukaniledger.entity.Business;
import com.dukaniledger.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/business")
@RequiredArgsConstructor
public class BusinessController {
    private final BusinessService businessService;

    @PostMapping("/registration")
    public Business createBusiness(
            @RequestBody BusinessRequest request,
            Authentication authentication
            ){
        return businessService.createBusiness(request);
    }

    @GetMapping("/owner")
    public OwnerResponse getOwnerInfo() {
        return businessService.getOwnerInfo();
    }
}
