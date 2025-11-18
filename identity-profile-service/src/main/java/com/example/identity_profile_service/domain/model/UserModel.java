package com.example.identity_profile_service.domain.model;

import com.example.identity_profile_service.domain.enums.UserRole;
import com.example.identity_profile_service.domain.enums.UserStatus;
import com.fasterxml.jackson.core.JsonToken;
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
    protected String password;
    protected String firstName;
    protected String lastName;
    protected LocalDateTime createdAt;
    protected LocalDateTime lastLogin;
    protected UserRole role;
    protected UserStatus status;
    protected String urlProfil;

    public abstract String getUserType();

    public void updateProfile(String username, String firstName, String lastName, String email, UserRole role, UserStatus status,  String urlProfil, String password) {
        this.username = username;
        this.firstName = firstName;
        this.password = password;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.status = status;
        this.urlProfil = urlProfil;
    }
}