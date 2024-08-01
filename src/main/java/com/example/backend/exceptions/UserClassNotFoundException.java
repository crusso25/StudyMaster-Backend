package com.example.backend.exceptions;

public class UserClassNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1;

    public UserClassNotFoundException(String message) {
        super(message);
    }
}