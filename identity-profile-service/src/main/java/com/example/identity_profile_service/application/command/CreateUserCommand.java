package com.example.identity_profile_service.application.command;

public record CreateUserCommand(
        String email,
        String username,
        String firstName,
        String lastName
) {}
