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

    public CalendarEventResponse getAllCalendarEvents(int userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<CalendarEvent> calendarEventPage = calendarEventRepository.findByUserId(userId, pageable);
        List<CalendarEventDto> content = calendarEventPage.getContent().stream().map(this::mapToDto).collect(Collectors.toList());

        CalendarEventResponse calendarEventResponse = new CalendarEventResponse();
        calendarEventResponse.setContent(content);
        calendarEventResponse.setPageNo(calendarEventPage.getNumber());
        calendarEventResponse.setPageSize(calendarEventPage.getSize());
        calendarEventResponse.setTotalElements(calendarEventPage.getTotalElements());
        calendarEventResponse.setTotalPages(calendarEventPage.getTotalPages());
        calendarEventResponse.setLast(calendarEventPage.isLast());

        return calendarEventResponse;
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
        calendarEvent.setClassContent(calendarEventDto.getClassContent());
        calendarEvent.setClassName(calendarEventDto.getClassName());
        calendarEvent.setType(calendarEventDto.getType());
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
        dto.setClassContent(calendarEvent.getClassContent());
        dto.setClassName(calendarEvent.getClassName());
        dto.setType(calendarEvent.getType());
        return dto;
    }

    private CalendarEvent mapToEntity(CalendarEventDto calendarEventDto) {
        CalendarEvent calendarEvent = new CalendarEvent();
        calendarEvent.setTitle(calendarEventDto.getTitle());
        calendarEvent.setStartDate(calendarEventDto.getStartDate());
        calendarEvent.setEndDate(calendarEventDto.getEndDate());
        calendarEvent.setClassContent(calendarEventDto.getClassContent());
        calendarEvent.setClassName(calendarEventDto.getClassName());
        calendarEvent.setType(calendarEventDto.getType());
        return calendarEvent;
    }
}
