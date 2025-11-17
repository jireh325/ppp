package com.example.identity_profile_service.application.command;

import java.util.UUID;

public record DemoteModeratorCommand(
        UUID userId,
        UUID adminId,
        String reason
) {}
