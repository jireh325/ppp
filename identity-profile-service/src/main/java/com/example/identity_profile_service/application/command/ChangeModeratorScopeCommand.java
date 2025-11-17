package com.example.identity_profile_service.application.command;

import com.example.identity_profile_service.domain.enums.ModerationScopeType;

import java.util.UUID;

public record ChangeModeratorScopeCommand(
        UUID userId,
        ModerationScopeType newScope,
        UUID adminId
) {}
