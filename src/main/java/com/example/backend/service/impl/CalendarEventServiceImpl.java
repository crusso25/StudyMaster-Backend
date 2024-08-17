package com.example.backend.service.impl;

import com.example.backend.dto.CalendarEventDto;
import com.example.backend.dto.CalendarEventResponse;
import com.example.backend.model.CalendarEvent;
import com.example.backend.model.UserEntity;
import com.example.backend.repository.CalendarEventRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.CalendarEventService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CalendarEventServiceImpl implements CalendarEventService {

    @Autowired
    private CalendarEventRepository calendarEventRepository;

    @Autowired
    private UserRepository userRepository;

    public CalendarEventResponse getAllCalendarEvents(int userId) {
        List<CalendarEvent> events = calendarEventRepository.findByUserId(userId);
        List<CalendarEventDto> eventDtos = events.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());

        return new CalendarEventResponse(eventDtos);
    }

    public CalendarEventDto getCalendarEventById(int userId, int id) {
        CalendarEvent calendarEvent = calendarEventRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return mapToDto(calendarEvent);
    }

    public CalendarEventDto createCalendarEvent(int userId, CalendarEventDto calendarEventDto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        CalendarEvent calendarEvent = mapToEntity(calendarEventDto);
        calendarEvent.setUser(user);
        CalendarEvent newCalendarEvent = calendarEventRepository.save(calendarEvent);
        return mapToDto(newCalendarEvent);
    }

    public CalendarEventDto updateCalendarEvent(int userId, CalendarEventDto calendarEventDto, int id) {
        CalendarEvent calendarEvent = calendarEventRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        calendarEvent.setTitle(calendarEventDto.getTitle());
        calendarEvent.setStartDate(calendarEventDto.getStartDate());
        calendarEvent.setEndDate(calendarEventDto.getEndDate());
        calendarEvent.setContent(calendarEventDto.getContent());
        calendarEvent.setClassName(calendarEventDto.getClassName());
        calendarEvent.setType(calendarEventDto.getType());
        calendarEvent.setContentGenerated(calendarEventDto.getContentGenerated());
        calendarEvent.setPracticeProblems(calendarEventDto.getPracticeProblems());
        calendarEvent.setExamFor(calendarEventDto.getExamFor());
        CalendarEvent updatedCalendarEvent = calendarEventRepository.save(calendarEvent);
        return mapToDto(updatedCalendarEvent);
    }

    public void deleteCalendarEvent(int userId, int id) {
        CalendarEvent calendarEvent = calendarEventRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        calendarEventRepository.delete(calendarEvent);
    }

    private CalendarEventDto mapToDto(CalendarEvent calendarEvent) {
        CalendarEventDto dto = new CalendarEventDto();
        dto.setId(calendarEvent.getId());
        dto.setTitle(calendarEvent.getTitle());
        dto.setStartDate(calendarEvent.getStartDate());
        dto.setEndDate(calendarEvent.getEndDate());
        dto.setContent(calendarEvent.getContent());
        dto.setClassName(calendarEvent.getClassName());
        dto.setType(calendarEvent.getType());
        dto.setContentGenerated(calendarEvent.getContentGenerated());
        dto.setPracticeProblems(calendarEvent.getPracticeProblems());
        dto.setExamFor(calendarEvent.getExamFor());
        return dto;
    }

    private CalendarEvent mapToEntity(CalendarEventDto calendarEventDto) {
        CalendarEvent calendarEvent = new CalendarEvent();
        calendarEvent.setTitle(calendarEventDto.getTitle());
        calendarEvent.setStartDate(calendarEventDto.getStartDate());
        calendarEvent.setEndDate(calendarEventDto.getEndDate());
        calendarEvent.setContent(calendarEventDto.getContent());
        calendarEvent.setClassName(calendarEventDto.getClassName());
        calendarEvent.setType(calendarEventDto.getType());
        calendarEvent.setContentGenerated(calendarEventDto.getContentGenerated());
        calendarEvent.setPracticeProblems(calendarEventDto.getPracticeProblems());
        calendarEvent.setExamFor(calendarEventDto.getExamFor());
        return calendarEvent;
    }
}
