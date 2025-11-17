package com.example.identity_profile_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public abstract class UserModel {
    protected UUID id;
    protected String username;
    protected String email;
    protected String passwordHash;
    protected String firstName;
    protected String lastName;
    protected LocalDateTime createdAt;
    protected LocalDateTime lastLogin;
    protected Boolean isActive;
    protected String keycloakId; // Référence à Keycloak

    public abstract String getUserType();

    public boolean authenticate() {
        // Logique d'authentification de base
        return isActive && passwordHash != null;
    }

    public void updateProfile(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}