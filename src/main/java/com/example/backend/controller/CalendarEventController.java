package com.example.backend.controller;

import com.example.backend.dto.CalendarEventDto;
import com.example.backend.dto.CalendarEventResponse;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.CalendarEventService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/")
public class CalendarEventController {

    @Autowired
    private CalendarEventService calendarEventService;

    @Autowired
    private UserRepository userRepository;

    public CalendarEventController(CalendarEventService calendarEventService, UserRepository userRepository) {
        this.calendarEventService = calendarEventService;
        this.userRepository = userRepository;
    }

    @GetMapping("calendarevents")
    public ResponseEntity<CalendarEventResponse> getCalendarEvents(
            @PathVariable("userId") int userId) {

        CalendarEventResponse response = calendarEventService.getAllCalendarEvents(userId);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("calendarevents/{calendarId}")
    public ResponseEntity<CalendarEventDto> getCalendarEvent(
            @PathVariable("userId") int userId,
            @PathVariable("calendarId") int calendarId) {
        return ResponseEntity.ok(calendarEventService.getCalendarEventById(userId, calendarId));
    }

    @PostMapping("calendarevents")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CalendarEventDto> createCalendarEvent(
            @PathVariable("userId") int userId,
            @RequestBody CalendarEventDto calendarEventDto) {
        return new ResponseEntity<>(calendarEventService.createCalendarEvent(userId, calendarEventDto), HttpStatus.CREATED);
    }

    @PutMapping("calendarevents/{calendarId}")
    public ResponseEntity<CalendarEventDto> updateCalendarEvent(
            @PathVariable("userId") int userId,
            @RequestBody CalendarEventDto calendarEventDto,
            @PathVariable("calendarId") int calendarId) {
        CalendarEventDto response = calendarEventService.updateCalendarEvent(userId, calendarEventDto, calendarId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("calendarevents/{calendarId}")
    public ResponseEntity<String> deleteCalendarEvent(
            @PathVariable("userId") int userId,
            @PathVariable("calendarId") int calendarId) {
        calendarEventService.deleteCalendarEvent(userId, calendarId);
        return new ResponseEntity<>("Event successfully deleted", HttpStatus.OK);
    }
}
