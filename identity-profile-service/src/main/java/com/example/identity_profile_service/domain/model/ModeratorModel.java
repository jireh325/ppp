package com.example.identity_profile_service.domain.model;

import com.example.identity_profile_service.domain.enums.AccessLevel;
import com.example.identity_profile_service.domain.enums.Department;
import com.example.identity_profile_service.domain.enums.ModerationScopeType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class ModeratorModel extends CitizenModel {
    private LocalDateTime approvedAt;
    private ModerationScopeType moderationScope;
    private String approvedBy; // Référence à l'admin qui a approuvé

    public ModeratorModel() {
        super();
        this.approvedAt = LocalDateTime.now();
    }

    @Override
    public String getUserType() {
        return "MODERATOR";
    }
}