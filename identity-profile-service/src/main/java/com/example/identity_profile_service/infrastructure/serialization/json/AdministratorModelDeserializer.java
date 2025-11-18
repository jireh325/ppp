package com.example.identity_profile_service.infrastructure.serialization.json;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.example.identity_profile_service.domain.model.AdministratorModel;
import com.example.identity_profile_service.domain.enums.UserRole;
import com.example.identity_profile_service.domain.enums.UserStatus;
import com.example.identity_profile_service.domain.enums.AccessLevel;
import com.example.identity_profile_service.domain.enums.Department;

public class AdministratorModelDeserializer extends JsonDeserializer<AdministratorModel> {

    @Override
    public AdministratorModel deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        // Si Jackson a des métadonnées de type, on utilise la désérialisation standard
        if (node.isObject() && (node.has("@type") || node.has("@class"))) {
            return p.getCodec().treeToValue(node, AdministratorModel.class);
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

        // Extraction des champs spécifiques à AdministratorModel
        AccessLevel accessLevel = AccessLevel.TECHNICAL; // valeur par défaut
        if (node.has("accessLevel") && !node.get("accessLevel").isNull()) {
            accessLevel = AccessLevel.valueOf(node.get("accessLevel").asText());
        }

        Department department = Department.MODERATION_CENTRALE; // valeur par défaut
        if (node.has("department") && !node.get("department").isNull()) {
            department = Department.valueOf(node.get("department").asText());
        }

        // Construction de l'objet AdministratorModel
        AdministratorModel admin = AdministratorModel.builder()
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

        return admin;
    }
}
