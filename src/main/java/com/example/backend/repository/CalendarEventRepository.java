package com.example.backend.repository;

import com.example.backend.model.CalendarEvent;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CalendarEventRepository extends JpaRepository<CalendarEvent, Integer> {
    List<CalendarEvent> findByUserId(int userId);
    Optional<CalendarEvent> findByIdAndUserId(int id, int userId);
}
