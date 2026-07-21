package com.dukaniledger.controller;

import com.dukaniledger.dto.BusinessRequest;
import com.dukaniledger.entity.Business;
import com.dukaniledger.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
}
