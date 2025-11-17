package com.example.identity_profile_service.application.command;

import com.example.identity_profile_service.domain.enums.ModerationScopeType;

import java.util.UUID;

public record PromoteToModeratorCommand(
        UUID citizenId,
        UUID approvedByAdminId,
        ModerationScopeType scope,
        String reason
) {}
