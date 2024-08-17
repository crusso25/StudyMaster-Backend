package com.example.backend.service.impl;

import com.example.backend.dto.UserClassDto;
import com.example.backend.dto.UserClassResponse;
import com.example.backend.model.UserClass;
import com.example.backend.model.UserEntity;
import com.example.backend.repository.UserClassRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserClassService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserClassServiceImpl implements UserClassService {

    @Autowired
    private UserClassRepository userClassRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserClassResponse getAllUserClasses(int userId) {
        List<UserClass> classes = userClassRepository.findByUserId(userId);
        List<UserClassDto> classDtos = classes.stream().map(this::mapToDto).collect(Collectors.toList());

        return new UserClassResponse(classDtos);
    }

    @Override
    public UserClassDto getUserClassById(int userId, int classId) {
        UserClass userClass = userClassRepository.findByIdAndUserId(classId, userId)
                .orElseThrow(() -> new RuntimeException("Class not found"));
        return mapToDto(userClass);
    }

    @Override
    public UserClassDto createUserClass(int userId, UserClassDto userClassDto) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        UserClass userClass = mapToEntity(userClassDto);
        userClass.setUser(user);
        UserClass newUserClass = userClassRepository.save(userClass);
        return mapToDto(newUserClass);
    }

    @Override
    public UserClassDto updateUserClass(int userId, UserClassDto userClassDto, int classId) {
        UserClass userClass = userClassRepository.findByIdAndUserId(classId, userId)
                .orElseThrow(() -> new RuntimeException("Class not found"));
        userClass.setClassName(userClassDto.getClassName());
        userClass.setClassContent(userClassDto.getClassContent());
        UserClass updatedUserClass = userClassRepository.save(userClass);
        return mapToDto(updatedUserClass);
    }

    @Override
    public void deleteUserClass(int userId, int classId) {
        UserClass userClass = userClassRepository.findByIdAndUserId(classId, userId)
                .orElseThrow(() -> new RuntimeException("Class not found"));
        userClassRepository.delete(userClass);
    }

    private UserClassDto mapToDto(UserClass userClass) {
        UserClassDto dto = new UserClassDto();
        dto.setId(userClass.getId());
        dto.setClassName(userClass.getClassName());
        dto.setClassContent(userClass.getClassContent());
        return dto;
    }

    private UserClass mapToEntity(UserClassDto userClassDto) {
        UserClass userClass = new UserClass();
        userClass.setClassName(userClassDto.getClassName());
        userClass.setClassContent(userClassDto.getClassContent());
        return userClass;
    }
}
