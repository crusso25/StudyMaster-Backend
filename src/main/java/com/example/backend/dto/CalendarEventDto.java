package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CalendarEventDto {
    private int id;
    private String className;
    private String content;
    private String endDate;
    private String startDate;
    private String title;
    private String type;
    private Boolean contentGenerated;
    private String practiceProblems;
    private String examFor;
}
