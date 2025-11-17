package com.example.identity_profile_service.application.command;

import java.util.UUID;

public record CanUserPromoteRoleCommand(
        UUID userId,
        String targetRole
) {}
