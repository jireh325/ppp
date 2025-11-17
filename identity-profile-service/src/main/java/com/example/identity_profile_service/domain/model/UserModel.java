package com.example.identity_profile_service.domain.model;

import com.example.identity_profile_service.domain.enums.UserStatus;
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
    protected String firstName;
    protected String lastName;
    protected LocalDateTime createdAt;
    protected LocalDateTime lastLogin;
    protected UserStatus status;

    public abstract String getUserType();

    public void updateProfile(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}