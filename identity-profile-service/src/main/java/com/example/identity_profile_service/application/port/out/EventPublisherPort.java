package com.example.identity_profile_service.application.port.out;

import com.example.identity_profile_service.domain.enums.ModerationScopeType;
import com.example.identity_profile_service.domain.enums.UserStatus;
import com.example.identity_profile_service.domain.model.UserModel;
import java.util.UUID;

public interface EventPublisherPort {

    // Événements utilisateur
    void publishUserCreated(UserModel user);
    void publishUserUpdated(UserModel user);
    void publishUserDeactivated(UUID userId, String reason);
    void publishUserStatusChanged(UUID userId, UserStatus oldStatus, UserStatus newStatus);

    // Événements de rôle
    void publishRolePromoted(UUID userId, String fromRole, String toRole, String approvedBy);
    void publishRoleDemoted(UUID userId, String fromRole, String toRole, String reason);
    void publishModeratorScopeChanged(UUID moderatorId, ModerationScopeType oldScope, ModerationScopeType newScope);

    // Événements de réputation
    void publishReputationChanged(UUID citizenId, int oldReputation, int newReputation, String reason);
    void publishVotingPowerChanged(UUID citizenId, int oldVotingPower, int newVotingPower);

    // Événements d'audit
    void publishAdminAction(UUID adminId, String action, UUID targetUserId, String details);
}