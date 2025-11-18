package com.example.identity_profile_service.infrastructure.mapper;

import com.example.identity_profile_service.domain.model.*;
import com.example.identity_profile_service.domain.enums.*;
import com.example.identity_profile_service.infrastructure.adapter.out.persistense.jpa.entity.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserEntityMapper {

    // ========== DOMAIN TO ENTITY ==========

    public UserEntityJpa toEntity(UserModel user) {
        if (user == null) {
            return null;
        }

        if (user instanceof CitizenModel citizen) {
            return toEntity(citizen);
        } else if (user instanceof ModeratorModel moderator) {
            return toEntity(moderator);
        } else if (user instanceof AdministratorModel administrator) {
            return toEntity(administrator);
        }

        throw new IllegalArgumentException("Type de modèle non supporté: " + user.getClass().getName());
    }

    public CitizenEntityJpa toEntity(CitizenModel citizen) {
        if (citizen == null) {
            return null;
        }

        CitizenEntityJpa entity = CitizenEntityJpa.builder()
                .id(citizen.getId())
                .username(citizen.getUsername())
                .password(citizen.getPassword())
                .email(citizen.getEmail())
                .firstName(citizen.getFirstName())
                .lastName(citizen.getLastName())
                .createdAt(citizen.getCreatedAt() != null ? citizen.getCreatedAt() : LocalDateTime.now())
                .lastLogin(citizen.getLastLogin())
                .status(citizen.getStatus())
                .userType(citizen.getUserType())
                .reputation(citizen.getReputation())
                .votingPower(citizen.getVotingPower())
                .build();

        return entity;
    }

    public ModeratorEntityJpa toEntity(ModeratorModel moderator) {
        if (moderator == null) {
            return null;
        }

        ModeratorEntityJpa entity = ModeratorEntityJpa.builder()
                .id(moderator.getId())
                .username(moderator.getUsername())
                .password(moderator.getPassword())
                .email(moderator.getEmail())
                .firstName(moderator.getFirstName())
                .lastName(moderator.getLastName())
                .createdAt(moderator.getCreatedAt() != null ? moderator.getCreatedAt() : LocalDateTime.now())
                .lastLogin(moderator.getLastLogin())
                .status(moderator.getStatus())
                .userType(moderator.getUserType())
                .approvedAt(moderator.getApprovedAt())
                .moderationScope(moderator.getModerationScope())
                .approvedBy(moderator.getApprovedBy())
                .build();

        return entity;
    }

    public AdministratorEntityJpa toEntity(AdministratorModel administrator) {
        if (administrator == null) {
            return null;
        }

        AdministratorEntityJpa entity = AdministratorEntityJpa.builder()
                .id(administrator.getId())
                .username(administrator.getUsername())
                .password(administrator.getPassword())
                .email(administrator.getEmail())
                .firstName(administrator.getFirstName())
                .lastName(administrator.getLastName())
                .createdAt(administrator.getCreatedAt() != null ? administrator.getCreatedAt() : LocalDateTime.now())
                .lastLogin(administrator.getLastLogin())
                .status(administrator.getStatus())
                .userType(administrator.getUserType())
                .accessLevel(administrator.getAccessLevel())
                .department(administrator.getDepartment())
                .build();

        return entity;
    }

    // ========== ENTITY TO DOMAIN ==========

    public UserModel toDomain(UserEntityJpa entity) {
        if (entity == null) {
            return null;
        }

        if (entity instanceof CitizenEntityJpa citizen) {
            return toDomainCitizen(citizen);
        } else if (entity instanceof ModeratorEntityJpa moderator) {
            return toDomainModerator(moderator);
        } else if (entity instanceof AdministratorEntityJpa administrator) {
            return toDomainAdministrator(administrator);
        }

        throw new IllegalArgumentException("Type d'entité non supporté: " + entity.getClass().getName());
    }

    public CitizenModel toDomainCitizen(CitizenEntityJpa entity) {
        if (entity == null) {
            return null;
        }

        return CitizenModel.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .createdAt(entity.getCreatedAt())
                .lastLogin(entity.getLastLogin())
                .status(entity.getStatus())
                .role(UserRole.valueOf(entity.getUserType()))
                .reputation(entity.getReputation())
                .votingPower(entity.getVotingPower())
                .build();
    }

    public ModeratorModel toDomainModerator(ModeratorEntityJpa entity) {
        if (entity == null) {
            return null;
        }

        return ModeratorModel.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .createdAt(entity.getCreatedAt())
                .lastLogin(entity.getLastLogin())
                .status(entity.getStatus())
                .role(UserRole.valueOf(entity.getUserType()))
                .approvedAt(entity.getApprovedAt())
                .moderationScope(entity.getModerationScope())
                .approvedBy(entity.getApprovedBy())
                .build();
    }

    public AdministratorModel toDomainAdministrator(AdministratorEntityJpa entity) {
        if (entity == null) {
            return null;
        }

        return AdministratorModel.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .createdAt(entity.getCreatedAt())
                .lastLogin(entity.getLastLogin())
                .status(entity.getStatus())
                .role(UserRole.valueOf(entity.getUserType()))
                .accessLevel(entity.getAccessLevel())
                .department(entity.getDepartment())
                .build();
    }

}