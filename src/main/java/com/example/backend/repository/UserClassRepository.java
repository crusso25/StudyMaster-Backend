package com.example.backend.repository;

import com.example.backend.model.UserClass;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserClassRepository extends JpaRepository<UserClass, Integer> {
    List<UserClass> findByUserId(int userId);
    Optional<UserClass> findByIdAndUserId(int classId, int userId);
}