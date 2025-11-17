package com.example.identity_profile_service.infrastructure.mapper;

import com.example.identity_profile_service.domain.model.*;
import com.example.identity_profile_service.infrastructure.adapter.out.jpa.entity.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {

    UserEntityMapper INSTANCE = Mappers.getMapper(UserEntityMapper.class);

    // Domain to Entity
    UserEntityJpa toEntity(UserModel user);

    @Mapping(target = "userType", expression = "java(getUserType(user))")
    UserModel toDomain(UserEntityJpa user);

    // Specific mappings
    CitizenModel toDomainCitizen(CitizenEntityJpa citizen);
    ModeratorModel toDomainModerator(ModeratorEntityJpa moderator);
    AdministratorModel toDomainAdministrator(AdministratorEntityJpa administrator);

    default String getUserType(UserEntityJpa user) {
        if (user instanceof AdministratorEntityJpa) return "ADMINISTRATOR";
        if (user instanceof ModeratorEntityJpa) return "MODERATOR";
        if (user instanceof CitizenEntityJpa) return "CITIZEN";
        return "UNKNOWN";
    }
}