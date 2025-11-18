package com.example.identity_profile_service.infrastructure.adapter.out.messaging;

import com.example.identity_profile_service.application.port.out.EventPublisherPort;
import com.example.identity_profile_service.domain.enums.ModerationScopeType;
import com.example.identity_profile_service.domain.enums.UserStatus;
import com.example.identity_profile_service.domain.model.UserModel;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class KafkaEventPublisher implements EventPublisherPort {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public KafkaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    private void sendEvent(String topic, Object payload) {
        kafkaTemplate.send(topic, payload);
    }

    // ==========================
    // Événements utilisateur
    // ==========================
    @Override
    public void publishUserCreated(UserModel user) {
        sendEvent("user-created", user);
    }

    @Override
    public void publishUserUpdated(UserModel user) {
        sendEvent("user-updated", user);
    }

    @Override
    public void publishUserDeactivated(UUID userId, String reason) {
        sendEvent("user-deactivated", userId + ":" + reason);
    }

    @Override
    public void publishUserStatusChanged(UUID userId, UserStatus oldStatus, UserStatus newStatus) {
        sendEvent("user-status-changed", userId + ":" + oldStatus + "->" + newStatus);
    }

    // ==========================
    // Événements de rôle
    // ==========================
    @Override
    public void publishRolePromoted(UUID userId, String fromRole, String toRole, String approvedBy) {
        sendEvent("role-promoted", userId + ":" + fromRole + "->" + toRole + " by " + approvedBy);
    }

    @Override
    public void publishRoleDemoted(UUID userId, String fromRole, String toRole, String reason) {
        sendEvent("role-demoted", userId + ":" + fromRole + "->" + toRole + " reason: " + reason);
    }

    @Override
    public void publishModeratorScopeChanged(UUID moderatorId, ModerationScopeType oldScope, ModerationScopeType newScope) {
        sendEvent("moderator-scope-changed", moderatorId + ":" + oldScope + "->" + newScope);
    }

    // ==========================
    // Événements de réputation
    // ==========================
    @Override
    public void publishReputationChanged(UUID citizenId, int oldReputation, int newReputation, String reason) {
        sendEvent("reputation-changed", citizenId + ":" + oldReputation + "->" + newReputation + " reason: " + reason);
    }

    @Override
    public void publishVotingPowerChanged(UUID citizenId, int oldVotingPower, int newVotingPower) {
        sendEvent("votingpower-changed", citizenId + ":" + oldVotingPower + "->" + newVotingPower);
    }

    // ==========================
    // Événements d'audit
    // ==========================
    @Override
    public void publishAdminAction(UUID adminId, String action, UUID targetUserId, String details) {
        sendEvent("admin-action", adminId + ":" + action + " on " + targetUserId + " details: " + details);
    }
}
