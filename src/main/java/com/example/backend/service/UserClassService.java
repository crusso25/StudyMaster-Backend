package com.example.backend.service;

import com.example.backend.dto.*;

public interface UserClassService {
    UserClassDto getUserClassById(int userId, int classId);
    UserClassResponse getAllUserClasses(int userId, int pageNo, int pageSize);
    UserClassDto createUserClass(int userId, UserClassDto userClassDto);
    UserClassDto updateUserClass(int userId, UserClassDto userClassDto, int classId);
    void deleteUserClass(int userId, int classId);
}