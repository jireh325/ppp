package com.example.identity_profile_service.domain.model;

import com.example.identity_profile_service.domain.enums.AccessLevel;
import com.example.identity_profile_service.domain.enums.Department;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class AdministratorModel extends UserModel {
    private AccessLevel accessLevel;
    private Department department;

    public AdministratorModel() {
        super();
        this.accessLevel = AccessLevel.TECHNICAL;
        this.department = Department.MODERATION_CENTRALE;
    }

    @Override
    public String getUserType() {
        return "ADMINISTRATOR";
    }
}