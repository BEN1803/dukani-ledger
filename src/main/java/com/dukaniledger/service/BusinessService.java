package com.dukaniledger.service;

import com.dukaniledger.dto.BusinessRequest;
import com.dukaniledger.dto.OwnerResponse;
import com.dukaniledger.entity.Business;
import com.dukaniledger.entity.Role;
import com.dukaniledger.entity.User;
import com.dukaniledger.repository.BusinessRepository;
import com.dukaniledger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessService {
    private final BusinessRepository businessRepository;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final BusinessContextService businessContextService;

    public Business createBusiness(
            BusinessRequest request
    ){
        User user  = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(
                        request.getPassword()
                        )
                )
                .role(Role.OWNER)
                .build();

        User savedUser = userRepository.save(user);


        Business business = Business.builder()
                .shopName(request.getShopName())
                .fullname(request.getFullname())
                .location(request.getLocation())
                .phone(request.getPhone())
                .email(request.getEmail())
                .owner(savedUser)
                .build();

        return businessRepository.save(business);
    }

    public OwnerResponse getOwnerInfo() {
        // Works whether the caller is the OWNER themselves or a WORKER
        // linked to that owner's business.
        User owner = businessContextService.getOwnerForCurrentUser();

        Business business = businessRepository.findByOwnerId(owner.getId())
                .orElseThrow(() -> new RuntimeException("Business not found for this owner"));

        return OwnerResponse.builder()
                .ownerId(owner.getId())
                .fullname(business.getFullname())
                .email(owner.getEmail())
                .phone(business.getPhone())
                .shopName(business.getShopName())
                .location(business.getLocation())
                .createdAt(business.getCreatedAt())
                .build();
    }
}
