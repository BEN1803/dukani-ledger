package com.dukaniledger.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class ActivityLogResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String userName;
    private String action;
    private String entity;
    private LocalDateTime timeStamp;
}