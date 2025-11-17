package com.example.identity_profile_service.application.command;

import com.example.identity_profile_service.domain.enums.AccessLevel;
import com.example.identity_profile_service.domain.enums.Department;

import java.util.UUID;

public record AssignAdministratorRoleCommand(
        UUID userId,
        AccessLevel accessLevel,
        Department department
) {}
