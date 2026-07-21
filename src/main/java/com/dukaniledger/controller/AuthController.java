package com.dukaniledger.controller;


import com.dukaniledger.dto.LoginRequest;
import com.dukaniledger.dto.LoginResponse;
import com.dukaniledger.dto.RegisterRequest;
import com.dukaniledger.dto.UserResponse;
import com.dukaniledger.entity.User;
import com.dukaniledger.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public UserResponse register(
            @RequestBody RegisterRequest request
    ){
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request
    ){
        return authService.login(request);
    }
}
