package com.example.backend.dto;

import java.util.List;

import lombok.Data;

@Data
public class UserClassResponse {
    private List<UserClassDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
