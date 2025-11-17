package com.example.identity_profile_service.application.service;

import com.example.identity_profile_service.application.command.*;
import com.example.identity_profile_service.application.port.in.RoleManagementUseCase;
import com.example.identity_profile_service.application.port.out.AuditPort;
import com.example.identity_profile_service.application.port.out.UserRepositoryPort;
import com.example.identity_profile_service.application.port.out.EventPublisherPort;
import com.example.identity_profile_service.domain.enums.AccessLevel;
import com.example.identity_profile_service.domain.enums.Department;
import com.example.identity_profile_service.domain.enums.ModerationScopeType;
import com.example.identity_profile_service.domain.enums.UserStatus;
import com.example.identity_profile_service.domain.exception.UserNotFoundException;
import com.example.identity_profile_service.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleManagementService implements RoleManagementUseCase {

    private final UserRepositoryPort userRepository;
    private final EventPublisherPort eventPublisher;
    private final AuditPort auditPort;

    @Override
    public void promoteToModerator(PromoteToModeratorCommand command) {
        // 1. Vérifier que le promoteur est un admin
        AdministratorModel admin = getAdministrator(command.approvedByAdminId());

        // 2. Vérifier les permissions du promoteur
        if (!canUserPromoteRole(new CanUserPromoteRoleCommand(admin.getId(), "MODERATOR"))) {
            throw new SecurityException("Admin not authorized to promote to moderator");
        }

        // 3. Récupérer le citoyen
        CitizenModel citizen = getCitizen(command.citizenId());

        // 4. Vérifier que le citoyen est éligible
        validateCitizenEligibilityForModeration(citizen);

        // 5. Créer le modérateur
        ModeratorModel moderator = ModeratorModel.builder()
                .id(citizen.getId())
                .username(citizen.getUsername())
                .email(citizen.getEmail())
                .firstName(citizen.getFirstName())
                .lastName(citizen.getLastName())
                .createdAt(citizen.getCreatedAt())
                .lastLogin(citizen.getLastLogin())
                .status(citizen.getStatus())
                .reputation(citizen.getReputation())
                .votingPower(citizen.getVotingPower())
                .approvedAt(LocalDateTime.now())
                .moderationScope(command.scope())
                .approvedBy(admin.getId().toString())
                .build();

        // 6. Sauvegarder
        ModeratorModel savedModerator = (ModeratorModel) userRepository.save(moderator);

        // 7. Publier les événements
        eventPublisher.publishRolePromoted(
                command.citizenId(),
                "CITIZEN",
                "MODERATOR",
                admin.getId().toString()
        );

        eventPublisher.publishModeratorScopeChanged(
                command.citizenId(),
                null,
                command.scope()
        );

        // 8. Auditer l'action
        auditPort.logRoleChange(
                admin.getId(),
                command.citizenId(),
                "CITIZEN",
                "MODERATOR",
                "Promoted to moderator with scope: " + command.scope()
        );
    }

    @Override
    public void demoteModerator(DemoteModeratorCommand command) {
        // 1. Récupérer le modérateur
        ModeratorModel moderator = getModerator(command.userId());

        // 2. Créer le citoyen à partir du modérateur
        CitizenModel citizen = CitizenModel.builder()
                .id(moderator.getId())
                .username(moderator.getUsername())
                .email(moderator.getEmail())
                .firstName(moderator.getFirstName())
                .lastName(moderator.getLastName())
                .createdAt(moderator.getCreatedAt())
                .lastLogin(moderator.getLastLogin())
                .status(moderator.getStatus())
                .reputation(moderator.getReputation())
                .votingPower(moderator.getVotingPower())
                .build();

        // 3. Sauvegarder en tant que citoyen
        userRepository.save(citizen);

        // 4. Publier les événements
        eventPublisher.publishRoleDemoted(
                command.userId(),
                "MODERATOR",
                "CITIZEN",
                command.reason()
        );

        // 5. Auditer l'action
        auditPort.logRoleChange(
                null, // System action
                command.userId(),
                "MODERATOR",
                "CITIZEN",
                "Demoted from moderator: " + command.reason()
        );
    }

    @Override
    public void assignAdministratorRole(AssignAdministratorRoleCommand command) {
        // 1. Récupérer l'utilisateur
        UserModel user = userRepository.findById(command.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + command.userId()));

        // 2. Vérifier que ce n'est pas déjà un admin
        if (user instanceof AdministratorModel) {
            throw new IllegalStateException("User is already an administrator");
        }

        // 3. Créer l'administrateur
        AdministratorModel admin = AdministratorModel.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                .status(user.getStatus())
                .accessLevel(command.accessLevel())
                .department(command.department())
                .build();

        // 4. Sauvegarder
        userRepository.save(admin);

        // 5. Publier l'événement
        String previousRole = getUserType(user);
        eventPublisher.publishRolePromoted(
                command.userId(),
                previousRole,
                "ADMINISTRATOR",
                "SYSTEM" // Ou l'admin qui a fait l'action
        );

        // 6. Auditer
        auditPort.logRoleChange(
                null, // System action
                command.userId(),
                previousRole,
                "ADMINISTRATOR",
                "Assigned administrator role with access: " + command.accessLevel()
        );
    }

    @Override
    public void changeModeratorScope(ChangeModeratorScopeCommand command) {
        // 1. Vérifier que le changeur est un admin
        AdministratorModel admin = getAdministrator(command.adminId());

        // 2. Récupérer le modérateur
        ModeratorModel moderator = getModerator(command.userId());



        // 3. Vérifier les permissions
        if (!canUserManageUser(new CanUserManageUserCommand(admin.getId(), command.userId()))) {
            throw new SecurityException("Admin not authorized to change moderator scope");
        }

        // 4. Sauvegarder l'ancien scope
        ModerationScopeType oldScope = moderator.getModerationScope();

        // 5. Mettre à jour le scope
        moderator.setModerationScope(command.newScope());
        userRepository.save(moderator);

        // 6. Publier l'événement
        eventPublisher.publishModeratorScopeChanged(command.userId(), oldScope, command.newScope());

        // 7. Auditer
        auditPort.logAdminAction(
                admin.getId(),
                "CHANGE_MODERATOR_SCOPE",
                command.userId(),
                "Scope changed from " + oldScope + " to " + command.newScope()
        );
    }

    @Override
    public boolean canUserPromoteRole(CanUserPromoteRoleCommand command) {
        // 1. Récupérer le promoteur
        UserModel promoter = userRepository.findById(command.userId())
                .orElseThrow(() -> new UserNotFoundException("Promoter not found: " + command.userId()));

        // 2. Seuls les admins peuvent promouvoir
        if (!(promoter instanceof AdministratorModel admin)) {
            return false;
        }

        // 3. Vérifier les permissions selon le niveau d'accès
        return switch (admin.getAccessLevel()) {
            case SUPER_ADMIN -> true; // Peut tout faire
            case MUNICIPAL -> !"SUPER_ADMIN".equals(command.targetRole()); // Ne peut pas créer de super admin
            case DEPARTMENTAL -> "MODERATOR".equals(command.targetRole()); // Peut seulement promouvoir modérateurs
            case DISTRICT -> false; // Ne peut promouvoir personne
            case TECHNICAL -> false; // Pas de permissions de promotion
            case SUPPORT -> false; // Pas de permissions de promotion
        };
    }

    @Override
    public boolean canUserManageUser(CanUserManageUserCommand command) {
        // 1. Récupérer le manager et la cible
        UserModel manager = userRepository.findById(command.managerId())
                .orElseThrow(() -> new UserNotFoundException("Manager not found: " + command.managerId()));
        UserModel target = userRepository.findById(command.targetUserId())
                .orElseThrow(() -> new UserNotFoundException("Target user not found: " + command.targetUserId()));

        // 2. Un utilisateur peut se gérer lui-même
        if (command.managerId().equals(command.targetUserId())) {
            return true;
        }

        // 3. Seuls les admins peuvent gérer d'autres utilisateurs
        if (!(manager instanceof AdministratorModel admin)) {
            return false;
        }

        // 4. Vérifier les permissions selon le niveau d'accès
        return switch (admin.getAccessLevel()) {
            case SUPER_ADMIN -> true; // Peut gérer tout le monde
            case MUNICIPAL -> !isSuperAdmin(target); // Ne peut pas gérer les super admins
            case DEPARTMENTAL -> canManageDepartmentUsers(admin, target);
            case DISTRICT -> canManageDistrictUsers(admin, target);
            case TECHNICAL -> false; // Pas de permissions de promotion
            case SUPPORT -> false; // Pas de permissions de promotion
        };
    }

    // ========== MÉTHODES HELPER ==========

    private AdministratorModel getAdministrator(UUID adminId) {
        return userRepository.findById(adminId)
                .filter(user -> user instanceof AdministratorModel)
                .map(user -> (AdministratorModel) user)
                .orElseThrow(() -> new SecurityException("User is not an administrator: " + adminId));
    }

    private CitizenModel getCitizen(UUID citizenId) {
        return userRepository.findById(citizenId)
                .filter(user -> user instanceof CitizenModel)
                .map(user -> (CitizenModel) user)
                .orElseThrow(() -> new IllegalArgumentException("User is not a citizen: " + citizenId));
    }

    private ModeratorModel getModerator(UUID moderatorId) {
        return userRepository.findById(moderatorId)
                .filter(user -> user instanceof ModeratorModel)
                .map(user -> (ModeratorModel) user)
                .orElseThrow(() -> new IllegalArgumentException("User is not a moderator: " + moderatorId));
    }

    private void validateCitizenEligibilityForModeration(CitizenModel citizen) {
        // Vérifier que le citoyen est actif
        if (citizen.getStatus() != UserStatus.ACTIVE) {
            throw new IllegalStateException("Citizen must be active to become moderator");
        }

        // Vérifier la réputation minimale
        if (citizen.getReputation() < 100) {
            throw new IllegalStateException("Citizen must have at least 100 reputation to become moderator");
        }

        // Vérifier qu'il n'est pas déjà modérateur
        if (citizen instanceof ModeratorModel) {
            throw new IllegalStateException("User is already a moderator");
        }
    }

    private String getUserType(UserModel user) {
        if (user instanceof AdministratorModel) return "ADMINISTRATOR";
        if (user instanceof ModeratorModel) return "MODERATOR";
        if (user instanceof CitizenModel) return "CITIZEN";
        return "UNKNOWN";
    }

    private boolean isSuperAdmin(UserModel user) {
        return user instanceof AdministratorModel admin &&
                admin.getAccessLevel() == AccessLevel.SUPER_ADMIN;
    }

    private boolean canManageDepartmentUsers(AdministratorModel admin, UserModel target) {
        // Un admin départemental ne peut gérer que les utilisateurs de son département
        // Cette logique dépend de votre structure organisationnelle
        return true; // Implémentation simplifiée
    }

    private boolean canManageDistrictUsers(AdministratorModel admin, UserModel target) {
        // Un admin de district a des permissions très limitées
        return target instanceof CitizenModel; // Peut seulement gérer les citoyens
    }
}