package com.example.backend.service;

import com.example.backend.dto.*;;

public interface CalendarEventService {
    CalendarEventResponse getAllCalendarEvents(int userId, int pageNo, int pageSize);
    CalendarEventDto getCalendarEventById(int userId, int id);
    CalendarEventDto createCalendarEvent(int userId, CalendarEventDto calendarEventDto);
    CalendarEventDto updateCalendarEvent(int userId, CalendarEventDto calendarEventDto, int id);
    void deleteCalendarEvent(int userId, int id);
}
