package com.example.identity_profile_service.application.dto;

import com.example.identity_profile_service.domain.enums.AccessLevel;
import com.example.identity_profile_service.domain.enums.Department;
import com.example.identity_profile_service.domain.enums.ModerationScopeType;
import com.example.identity_profile_service.domain.enums.UserStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserProfileDto {
    // Champs communs à tous les users
    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;
    private UserStatus status;
    private String userType; // "CITIZEN", "MODERATOR", "ADMINISTRATOR"

    // Champs spécifiques à Citizen (y compris Moderator)
    private Integer reputation;
    private Integer votingPower;

    // Champs spécifiques à Moderator
    private LocalDateTime approvedAt;
    private ModerationScopeType moderationScope;
    private String approvedBy;

    // Champs spécifiques à Administrator
    private AccessLevel accessLevel;
    private Department department;

    // Méthode utilitaire pour savoir le type
    public boolean isCitizen() {
        return "CITIZEN".equals(userType);
    }

    public boolean isModerator() {
        return "MODERATOR".equals(userType);
    }

    public boolean isAdministrator() {
        return "ADMINISTRATOR".equals(userType);
    }
}