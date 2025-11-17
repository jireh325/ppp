package com.example.identity_profile_service.application.query;

public record UserStatisticsQuery(
        long totalUsers,
        long activeUsers,
        long suspendedUsers,
        long citizenCount,
        long moderatorCount,
        long administratorCount,
        double averageReputation
) {}