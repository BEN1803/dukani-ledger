package com.dukaniledger.dto;

import com.dukaniledger.entity.Role;
import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private Role role;

}
