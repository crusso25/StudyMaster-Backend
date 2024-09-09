package com.example.backend.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String identifier;
    private String password;
}
