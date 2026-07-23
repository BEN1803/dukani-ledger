package com.dukaniledger.service;

import com.dukaniledger.dto.ChangePasswordRequest;
import com.dukaniledger.dto.LoginRequest;
import com.dukaniledger.dto.LoginResponse;
import com.dukaniledger.dto.RegisterRequest;
import com.dukaniledger.dto.UserResponse;
import com.dukaniledger.entity.PasswordHistory;
import com.dukaniledger.entity.User;
import com.dukaniledger.repository.PasswordHistoryRepository;
import com.dukaniledger.repository.UserRepository;
import com.dukaniledger.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final ActivityLogService activityLogService;

    public UserResponse register(RegisterRequest request){
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(
                        request.getPassword()
                ))
                .role(request.getRole())

                .build();

        User savedUser = userRepository.save(user);

        recordPasswordHistory(savedUser);

        return UserResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();
    }

    public LoginResponse login(LoginRequest request){
        User user = userRepository
                .findByEmail(request.getEmail())
                .orElseThrow(
                        () -> new RuntimeException("User not found")
                );

        if(!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )){
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(
                user.getEmail()
        );

        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .build();
    }

    public void changePassword(String email, ChangePasswordRequest request){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        recordPasswordHistory(user);
        activityLogService.log("CHANGE_PASSWORD", "User#" + user.getId());
    }


    private void recordPasswordHistory(User user){
        passwordHistoryRepository.save(
                PasswordHistory.builder()
                        .user(user)
                        .passwordHash(user.getPassword())
                        .build()
        );
    }
}