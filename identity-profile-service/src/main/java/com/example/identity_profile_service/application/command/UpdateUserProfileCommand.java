package com.example.identity_profile_service.application.command;

import com.example.identity_profile_service.domain.enums.UserRole;
import com.example.identity_profile_service.domain.enums.UserStatus;

import java.util.UUID;

public record UpdateUserProfileCommand(
        UUID userId,
        String email,
        String username,
        String password,
        String firstName,
        String lastName,
        UserRole role,
        UserStatus status,
        String urlProfil
) {}
