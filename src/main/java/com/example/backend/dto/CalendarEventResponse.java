package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CalendarEventResponse {
    private List<CalendarEventDto> content;
}
