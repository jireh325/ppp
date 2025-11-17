package com.example.identity_profile_service.application.port.in;

import com.example.identity_profile_service.application.command.*;

public interface RoleManagementUseCase {
    // Promotion avec scope spécifique
    void promoteToModerator(PromoteToModeratorCommand command);

    // Rétrogradation
    void demoteModerator(DemoteModeratorCommand command);

    // Assignation rôle admin
    void assignAdministratorRole(AssignAdministratorRoleCommand command);

    // Changement de scope modérateur
    void changeModeratorScope(ChangeModeratorScopeCommand command);

    // Vérification des permissions
    boolean canUserPromoteRole(CanUserPromoteRoleCommand command);
    boolean canUserManageUser(CanUserManageUserCommand command);
}