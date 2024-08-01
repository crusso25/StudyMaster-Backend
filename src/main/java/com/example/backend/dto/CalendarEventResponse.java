package com.example.backend.dto;

import lombok.Data;

import java.util.List;

@Data
public class CalendarEventResponse {
    private List<CalendarEventDto> content;
    private int pageNo;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean last;
}
