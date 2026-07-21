package com.dukaniledger.dto;

import com.dukaniledger.entity.Gender;
import lombok.Data;

@Data
public class WorkerRequest {
    private String fullname;
    private String email;
    private String password;
    private String phone;
    private String address;
    private Gender gender;
}
