package com.example.identity_profile_service.infrastructure.serialization.json;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.example.identity_profile_service.domain.model.UserModel;
import com.example.identity_profile_service.domain.model.CitizenModel;
import com.example.identity_profile_service.domain.model.AdministratorModel;
import com.example.identity_profile_service.domain.model.ModeratorModel;
import com.example.identity_profile_service.domain.enums.UserRole;
import com.example.identity_profile_service.domain.enums.UserStatus;
import com.example.identity_profile_service.domain.enums.AccessLevel;
import com.example.identity_profile_service.domain.enums.Department;
import com.example.identity_profile_service.domain.enums.ModerationScopeType;

public class UserModelDeserializer extends JsonDeserializer<UserModel> {

    @Override
    public UserModel deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Si Jackson a des métadonnées de type, on utilise la désérialisation standard
        if (node.isObject() && (node.has("@type") || node.has("@class"))) {
            String className = node.has("@class") ? node.get("@class").asText() : node.get("@type").asText();

            // Délégation à Jackson pour le polymorphisme
            if (className.equals(CitizenModel.class.getName()) || className.contains("CitizenModel")) {
                return p.getCodec().treeToValue(node, CitizenModel.class);
            } else if (className.equals(AdministratorModel.class.getName()) || className.contains("AdministratorModel")) {
                return p.getCodec().treeToValue(node, AdministratorModel.class);
            } else if (className.equals(ModeratorModel.class.getName()) || className.contains("ModeratorModel")) {
                return p.getCodec().treeToValue(node, ModeratorModel.class);
            }

            // Si c'est UserModel abstrait, on ne peut pas l'instancier
            throw new IOException("Cannot deserialize abstract UserModel directly. Use concrete subclass.");
        }

        // Détection du type basée sur le champ userType
        String userType = node.has("userType") ? node.get("userType").asText() : null;

        if ("CITIZEN".equals(userType)) {
            return deserializeCitizen(node);
        } else if ("ADMINISTRATOR".equals(userType)) {
            return deserializeAdministrator(node);
        } else if ("MODERATOR".equals(userType) || hasModeratorFields(node)) {
            return deserializeModerator(node);
        }

        // Pour UserModel abstrait sans type spécifique, on ne peut pas instancier directement
        throw new IOException("Cannot deserialize abstract UserModel directly. Use concrete subclass or provide userType field.");
    }

    private boolean hasModeratorFields(JsonNode node) {
        return node.has("approvedAt") && node.has("moderationScope");
    }

    private CitizenModel deserializeCitizen(JsonNode node) throws IOException {
        // Extraction des champs
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

        // Extraction des champs spécifiques à CitizenModel
        Integer reputation = node.has("reputation") ? node.get("reputation").asInt() : 0;
        Integer votingPower = node.has("votingPower") ? node.get("votingPower").asInt() : 1;

        return CitizenModel.builder()
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
                .build();
    }

    private AdministratorModel deserializeAdministrator(JsonNode node) throws IOException {
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

        // Extraction des champs spécifiques à AdministratorModel
        AccessLevel accessLevel = AccessLevel.TECHNICAL;
        if (node.has("accessLevel") && !node.get("accessLevel").isNull()) {
            accessLevel = AccessLevel.valueOf(node.get("accessLevel").asText());
        }

        Department department = Department.MODERATION_CENTRALE;
        if (node.has("department") && !node.get("department").isNull()) {
            department = Department.valueOf(node.get("department").asText());
        }

        return AdministratorModel.builder()
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
                .accessLevel(accessLevel)
                .department(department)
                .build();
    }

    private ModeratorModel deserializeModerator(JsonNode node) throws IOException {
        // Extraction des champs de UserModel
        UUID id = node.has("id") && !node.get("id").isNull() ?
                UUID.fromString(node.get("id").asText()) : null;

        String username = node.has("username") ? node.get("username").asText() : null;
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

        return ModeratorModel.builder()
                .id(id)
                .username(username)
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
    }
}