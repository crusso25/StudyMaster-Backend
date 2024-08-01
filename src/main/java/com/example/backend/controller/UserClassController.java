package com.example.backend.controller;

import com.example.backend.dto.UserClassDto;
import com.example.backend.dto.UserClassResponse;
import com.example.backend.repository.UserRepository;
import com.example.backend.service.UserClassService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/")
public class UserClassController {

    @Autowired
    private UserClassService userClassService;

    @Autowired
    private UserRepository userRepository;

    public UserClassController(UserClassService userClassService, UserRepository userRepository) {
        this.userClassService = userClassService;
        this.userRepository = userRepository;
    }

    @GetMapping("userclasses")
    public ResponseEntity<UserClassResponse> getUserClasses(
        @PathVariable("userId") int userId,
        @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
        @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return new ResponseEntity<>(userClassService.getAllUserClasses(userId, pageNo, pageSize), HttpStatus.OK);
    }

    @GetMapping("userclasses/{classId}")
    public ResponseEntity<UserClassDto> getUserClass(
        @PathVariable("userId") int userId,
        @PathVariable("classId") int classId
    ) {
        return ResponseEntity.ok(userClassService.getUserClassById(userId, classId));
    }

    @PostMapping("userclasses")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserClassDto> createUserClass(
        @PathVariable("userId") int userId,
        @RequestBody UserClassDto userClassDto
    ) {
        return new ResponseEntity<>(userClassService.createUserClass(userId, userClassDto), HttpStatus.CREATED);
    }

    @PutMapping("userclasses/{classId}")
    public ResponseEntity<UserClassDto> updateUserClass(
        @PathVariable("userId") int userId,
        @RequestBody UserClassDto userClassDto,
        @PathVariable("classId") int classId
    ) {
        UserClassDto response = userClassService.updateUserClass(userId, userClassDto, classId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("userclasses/{classId}")
    public ResponseEntity<String> deleteUserClass(
        @PathVariable("userId") int userId,
        @PathVariable("classId") int classId
    ) {
        userClassService.deleteUserClass(userId, classId);
        return new ResponseEntity<>("Class successfully deleted", HttpStatus.OK);
    }
}
