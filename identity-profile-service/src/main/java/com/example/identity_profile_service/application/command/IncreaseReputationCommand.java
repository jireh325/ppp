package com.example.identity_profile_service.application.command;

import java.util.UUID;

public record IncreaseReputationCommand(
        UUID citizenId,
        int points,
        String reason,
        String source // "IDEA_APPROVED", "POSITIVE_VOTE", etc.
) {}
