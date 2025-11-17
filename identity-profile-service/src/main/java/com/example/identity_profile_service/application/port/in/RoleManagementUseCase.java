package com.example.identity_profile_service.application.port.in;

import com.example.identity_profile_service.domain.enums.AccessLevel;
import com.example.identity_profile_service.domain.enums.Department;
import com.example.identity_profile_service.domain.enums.ModerationScopeType;

import java.util.UUID;

public interface RoleManagementUseCase {
    // Promotion avec scope spécifique
    void promoteToModerator(UUID citizenId, UUID approvedByAdminId, ModerationScopeType scope);

    // Rétrogradation
    void demoteModerator(UUID moderatorId, String reason);

    // Assignation rôle admin
    void assignAdministratorRole(UUID userId, AccessLevel accessLevel, Department department);

    // Changement de scope modérateur
    void changeModeratorScope(UUID moderatorId, ModerationScopeType newScope, UUID changedByAdminId);

    // Vérification des permissions
    boolean canUserPromoteRole(UUID promoterId, String targetRole);
    boolean canUserManageUser(UUID managerId, UUID targetUserId);
}