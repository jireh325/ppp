package com.example.identity_profile_service.application.port.in;

import com.example.identity_profile_service.application.command.CreateUserCommand;
import com.example.identity_profile_service.application.command.DeactivateUserCommand;
import com.example.identity_profile_service.application.command.UpdateUserProfileCommand;
import com.example.identity_profile_service.application.dto.UserProfileDto;
import com.example.identity_profile_service.application.query.SearchUsersQuery;
import com.example.identity_profile_service.domain.enums.UserStatus;

import java.util.List;
import java.util.UUID;

public interface UserProfileUseCase {

    // Gestion basique des profils
    UserProfileDto getUserProfile(UUID userId);
    UserProfileDto createUserProfile(CreateUserCommand command);
    UserProfileDto updateUserProfile(UpdateUserProfileCommand command);
    void deactivateUser(DeactivateUserCommand command);

    // Recherche et listing
    List<UserProfileDto> searchUsers(SearchUsersQuery query);
    List<UserProfileDto> getUsersByStatus(UserStatus status);
    List<UserProfileDto> getUsersByType(String userType);

    // Profil courant (pour l'utilisateur connecté)
    UserProfileDto getCurrentUserProfile(UUID userId);
}