package com.example.backend.dto;

import lombok.Data;

@Data
public class CalendarEventDto {
    private int id;
    private String className;
    private String classContent;
    private String endDate;
    private String startDate;
    private String title;
    private String type;
}
