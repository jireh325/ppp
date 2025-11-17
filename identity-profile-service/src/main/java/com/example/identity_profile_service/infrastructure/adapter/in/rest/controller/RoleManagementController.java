package com.example.identity_profile_service.infrastructure.adapter.in.rest.controller;

import com.example.identity_profile_service.application.command.*;
import com.example.identity_profile_service.application.port.in.RoleManagementUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleManagementController {

    private final RoleManagementUseCase roleManagementUseCase;

    // ========== GESTION DES MODÉRATEURS ==========

    @PostMapping("/promote/moderator")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> promoteToModerator(
            @RequestBody PromoteToModeratorCommand request,
            @AuthenticationPrincipal Jwt jwt) {

        UUID adminId = extractUserIdFromJwt(jwt);

        PromoteToModeratorCommand command = new PromoteToModeratorCommand(
                request.citizenId(),
                adminId,
                request.scope()
        );

        roleManagementUseCase.promoteToModerator(command);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/demote/moderator")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> demoteModerator(
            @RequestBody DemoteModeratorCommand request,
            @AuthenticationPrincipal Jwt jwt) {

        UUID adminId = extractUserIdFromJwt(jwt);

        DemoteModeratorCommand command = new DemoteModeratorCommand(
                request.userId(),
                adminId,
                request.reason()
        );

        roleManagementUseCase.demoteModerator(command);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/moderator/{moderatorId}/scope")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> changeModeratorScope(
            @PathVariable UUID moderatorId,
            @RequestBody ChangeModeratorScopeCommand request,
            @AuthenticationPrincipal Jwt jwt) {

        UUID adminId = extractUserIdFromJwt(jwt);

        ChangeModeratorScopeCommand command = new ChangeModeratorScopeCommand(
                moderatorId,
                request.newScope(),
                adminId
        );

        roleManagementUseCase.changeModeratorScope(command);
        return ResponseEntity.ok().build();
    }

    // ========== GESTION DES ADMINISTRATEURS ==========

    @PostMapping("/promote/administrator")
    @PreAuthorize("hasRole('SUPER_ADMIN')") // Seul un SUPER_ADMIN peut créer d'autres admins
    public ResponseEntity<Void> assignAdministratorRole(
            @RequestBody AssignAdministratorRoleCommand request,
            @AuthenticationPrincipal Jwt jwt) {

        UUID adminId = extractUserIdFromJwt(jwt);

        AssignAdministratorRoleCommand command = new AssignAdministratorRoleCommand(
                request.userId(),
                request.accessLevel(),
                request.department()
        );

        roleManagementUseCase.assignAdministratorRole(command);
        return ResponseEntity.ok().build();
    }

    // ========== VÉRIFICATIONS DE PERMISSIONS ==========

    @GetMapping("/permissions/can-promote")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Boolean> canUserPromoteRole(
            @RequestParam String targetRole,
            @AuthenticationPrincipal Jwt jwt) {

        UUID userId = extractUserIdFromJwt(jwt);

        CanUserPromoteRoleCommand command = new CanUserPromoteRoleCommand(userId, targetRole);
        boolean canPromote = roleManagementUseCase.canUserPromoteRole(command);

        return ResponseEntity.ok(canPromote);
    }

    @GetMapping("/permissions/can-manage")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Boolean> canUserManageUser(
            @RequestParam UUID targetUserId,
            @AuthenticationPrincipal Jwt jwt) {

        UUID managerId = extractUserIdFromJwt(jwt);

        CanUserManageUserCommand command = new CanUserManageUserCommand(managerId, targetUserId);
        boolean canManage = roleManagementUseCase.canUserManageUser(command);

        return ResponseEntity.ok(canManage);
    }

    // ========== MÉTHODES HELPER ==========

    private UUID extractUserIdFromJwt(Jwt jwt) {
        String subject = jwt.getSubject();
        return UUID.fromString(subject);
    }
}