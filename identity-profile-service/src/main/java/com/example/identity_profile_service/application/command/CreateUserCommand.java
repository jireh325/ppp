package com.example.identity_profile_service.application.command;

import com.example.identity_profile_service.domain.enums.UserRole;

public record CreateUserCommand(
        String email,
        String username,
        String password,
        String firstName,
        String lastName,
        UserRole role
) {}
