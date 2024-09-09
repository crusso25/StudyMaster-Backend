package com.example.backend.controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.dto.AuthResponseDto;
import com.example.backend.dto.LoginDto;
import com.example.backend.dto.RegisterDto;
import com.example.backend.dto.VerifyUserDto;
import com.example.backend.model.Role;
import com.example.backend.model.UserEntity;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.UserRepository;
import com.example.backend.security.JWTGenerator;
import com.example.backend.service.AuthenticationService;
import com.example.backend.service.EmailService;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationService authenticationService;
    private JWTGenerator jwtGenerator;
    private UserRepository userRepository;

    @Autowired
    public AuthController(AuthenticationService authenticationService,
            JWTGenerator jwtGenerator,
            UserRepository userRepository) {
        this.authenticationService = authenticationService;
        this.jwtGenerator = jwtGenerator;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto, HttpServletResponse response) {
        UserEntity user = authenticationService.authenticate(loginDto);
        String accessToken = jwtGenerator.generateAccessToken(user);
        String refreshToken = jwtGenerator.generateRefreshToken(user);

        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/auth/refresh");
        response.addCookie(cookie);

        AuthResponseDto authResponse = new AuthResponseDto(accessToken, user.getId(), user.getUsername(),
                user.getEmail());
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<UserEntity> register(@RequestBody RegisterDto registerDto) {
        if (registerDto.getUsername().contains("@")) {
            throw new IllegalArgumentException("Username cannot contain '@' symbol");
        }

        UserEntity registeredUser = authenticationService.signup(registerDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestBody VerifyUserDto verifyUserDto) {
        try {
            authenticationService.verifyUser(verifyUserDto);
            return ResponseEntity.ok("Account successfully verified");
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<?> resendVerificationCode(@RequestParam String email) {
        try {
            authenticationService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code sent");
        } catch (RuntimeException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshAuthToken(HttpServletRequest request) {
        String refreshToken = getJwtFromRequest(request);
        if (jwtGenerator.validateToken(refreshToken)) {
            String username = jwtGenerator.getUsernameFromJWT(refreshToken);
            UserEntity user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            String newAccessToken = jwtGenerator.generateAccessToken(user);
            return ResponseEntity
                    .ok(new AuthResponseDto(newAccessToken, user.getId(), user.getUsername(), user.getEmail()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
        }
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("OK");
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("refreshToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}
