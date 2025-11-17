package com.example.identity_profile_service.application.command;

import java.util.UUID;

public record UpdateUserProfileCommand(
        UUID userId,
        String email,
        String username,
        String firstName,
        String lastName,
        String urlProfil
) {}
