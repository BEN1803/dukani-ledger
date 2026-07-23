package com.dukaniledger.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
public class CategoryResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String ownerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}