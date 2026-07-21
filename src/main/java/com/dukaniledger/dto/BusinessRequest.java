package com.dukaniledger.dto;

import lombok.Data;

@Data
public class BusinessRequest {
    private String shopName;

    private String location;

    private String phone;
    private String email;
    private String password;

    private String fullname;
}
