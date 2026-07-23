package com.dukaniledger.controller;


import com.dukaniledger.dto.*;
import com.dukaniledger.entity.User;
import com.dukaniledger.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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

    @PostMapping("/change-password")
    public void changePassword(
            @Valid @RequestBody ChangePasswordRequest request,
            Authentication authentication
    ){
        authService.changePassword(authentication.getName(), request);
    }
}
