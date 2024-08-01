package com.example.backend.repository;

import com.example.backend.model.UserClass;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserClassRepository extends JpaRepository<UserClass, Integer> {
    Page<UserClass> findByUserId(int userId, Pageable pageable);
    Optional<UserClass> findByIdAndUserId(int classId, int userId);
}