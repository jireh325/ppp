package com.example.identity_profile_service.infrastructure.serialization.json;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.example.identity_profile_service.domain.model.ModeratorModel;
import com.example.identity_profile_service.domain.enums.UserRole;
import com.example.identity_profile_service.domain.enums.UserStatus;
import com.example.identity_profile_service.domain.enums.ModerationScopeType;

public class ModeratorModelDeserializer extends JsonDeserializer<ModeratorModel> {

    @Override
    public ModeratorModel deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Si Jackson a des métadonnées de type, on utilise la désérialisation standard
        if (node.isObject() && (node.has("@type") || node.has("@class"))) {
            return p.getCodec().treeToValue(node, ModeratorModel.class);
        }

        // Extraction des champs de UserModel
        UUID id = node.has("id") && !node.get("id").isNull() ?
                UUID.fromString(node.get("id").asText()) : null;

        String username = node.has("username") ? node.get("username").asText() : null;
        String password = node.has("password") ? node.get("password").asText() : null;
        String email = node.has("email") ? node.get("email").asText() : null;
        String firstName = node.has("firstName") ? node.get("firstName").asText() : null;
        String lastName = node.has("lastName") ? node.get("lastName").asText() : null;

        LocalDateTime createdAt = null;
        if (node.has("createdAt") && !node.get("createdAt").isNull()) {
            createdAt = LocalDateTime.parse(node.get("createdAt").asText());
        }

        LocalDateTime lastLogin = null;
        if (node.has("lastLogin") && !node.get("lastLogin").isNull()) {
            lastLogin = LocalDateTime.parse(node.get("lastLogin").asText());
        }

        UserRole role = node.has("role") && !node.get("role").isNull() ?
                UserRole.valueOf(node.get("role").asText()) : null;

        UserStatus status = node.has("status") && !node.get("status").isNull() ?
                UserStatus.valueOf(node.get("status").asText()) : null;

        String urlProfil = node.has("urlProfil") ? node.get("urlProfil").asText() : null;

        // Extraction des champs de CitizenModel
        Integer reputation = node.has("reputation") ? node.get("reputation").asInt() : 0;
        Integer votingPower = node.has("votingPower") ? node.get("votingPower").asInt() : 1;

        // Extraction des champs spécifiques à ModeratorModel
        LocalDateTime approvedAt = LocalDateTime.now(); // valeur par défaut
        if (node.has("approvedAt") && !node.get("approvedAt").isNull()) {
            approvedAt = LocalDateTime.parse(node.get("approvedAt").asText());
        }

        ModerationScopeType moderationScope = null;
        if (node.has("moderationScope") && !node.get("moderationScope").isNull()) {
            moderationScope = ModerationScopeType.valueOf(node.get("moderationScope").asText());
        }

        String approvedBy = node.has("approvedBy") ? node.get("approvedBy").asText() : null;

        // Construction de l'objet ModeratorModel
        ModeratorModel moderator = ModeratorModel.builder()
                .id(id)
                .username(username)
                .password(password)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .createdAt(createdAt)
                .lastLogin(lastLogin)
                .role(role)
                .status(status)
                .urlProfil(urlProfil)
                .reputation(reputation)
                .votingPower(votingPower)
                .approvedAt(approvedAt)
                .moderationScope(moderationScope)
                .approvedBy(approvedBy)
                .build();

        return moderator;
    }
}
