package com.dukaniledger.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OwnerResponse {
    private Long ownerId;
    private String fullname;
    private String email;
    private String phone;
    private String shopName;
    private String location;
    private LocalDateTime createdAt;
}