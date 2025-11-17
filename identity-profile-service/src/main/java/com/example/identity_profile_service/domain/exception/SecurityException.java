package com.example.identity_profile_service.domain.exception;

public class SecurityException extends RuntimeException {
    public SecurityException(String message) {
        super(message);
    }
}