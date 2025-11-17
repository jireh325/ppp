package com.example.identity_profile_service.application.query;

import com.example.identity_profile_service.domain.enums.UserStatus;

import java.time.LocalDateTime;

public record SearchUsersQuery(
        String username,
        String email,
        UserStatus status,
        String userType,
        Integer minReputation,
        Integer maxReputation,
        LocalDateTime createdAfter,
        LocalDateTime createdBefore
) {}