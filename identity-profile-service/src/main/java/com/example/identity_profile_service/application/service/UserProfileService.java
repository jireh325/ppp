package com.example.identity_profile_service.application.service;

import com.example.identity_profile_service.application.command.CreateUserCommand;
import com.example.identity_profile_service.application.command.DeactivateUserCommand;
import com.example.identity_profile_service.application.command.UpdateUserProfileCommand;
import com.example.identity_profile_service.application.port.in.UserProfileUseCase;
import com.example.identity_profile_service.application.port.out.UserRepositoryPort;
import com.example.identity_profile_service.application.dto.UserProfileDto;
import com.example.identity_profile_service.application.query.SearchUsersQuery;
import com.example.identity_profile_service.domain.enums.AccessLevel;
import com.example.identity_profile_service.domain.enums.UserStatus;
import com.example.identity_profile_service.domain.exception.UserAlreadyExistsException;
import com.example.identity_profile_service.domain.exception.UserNotFoundException;
import com.example.identity_profile_service.domain.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileService implements UserProfileUseCase {

    private final UserRepositoryPort userRepository;

    @Override
    public UserProfileDto getUserProfile(UUID userId) {
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));
        return toDto(user);
    }

    @Override
    public UserProfileDto createUserProfile(CreateUserCommand command) {
        // Vérifier si l'utilisateur existe déjà
        if (userRepository.findByEmail(command.email()).isPresent()) {
            throw new UserAlreadyExistsException("User with email " + command.email() + " already exists");
        }
        if (userRepository.findByUsername(command.username()).isPresent()) {
            throw new UserAlreadyExistsException("User with username " + command.username() + " already exists");
        }

        // Créer un nouveau citoyen (type par défaut)
        CitizenModel newCitizen = CitizenModel.builder()
                .email(command.email())
                .username(command.username())
                .firstName(command.firstName())
                .lastName(command.lastName())
                .createdAt(java.time.LocalDateTime.now())
                .lastLogin(java.time.LocalDateTime.now())
                .status(UserStatus.ACTIVE)
                .reputation(0)
                .votingPower(1)
                .build();

        CitizenModel saved = (CitizenModel) userRepository.save(newCitizen);
        return toDto(saved);
    }

    @Override
    public UserProfileDto updateUserProfile(UpdateUserProfileCommand command) {
        UserModel user = userRepository.findById(command.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + command.userId()));

        // Vérifier les conflits d'email
        if (!user.getEmail().equals(command.email())) {
            userRepository.findByEmail(command.email())
                    .ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(command.userId())) {
                            throw new UserAlreadyExistsException("Email already taken: " + command.email());
                        }
                    });
        }

        // Vérifier les conflits de username
        if (!user.getUsername().equals(command.username())) {
            userRepository.findByUsername(command.username())
                    .ifPresent(existingUser -> {
                        if (!existingUser.getId().equals(command.userId())) {
                            throw new UserAlreadyExistsException("Username already taken: " + command.username());
                        }
                    });
        }

        user.updateProfile(command.firstName(), command.lastName(), command.email());
        user.setUsername(command.username());

        UserModel updated = userRepository.save(user);
        return toDto(updated);
    }

    @Override
    public void deactivateUser(DeactivateUserCommand command) {
        UserModel user = userRepository.findById(command.userId())
                .orElseThrow(() -> new UserNotFoundException("User not found: " + command.userId()));

        // Vérifier les permissions (basique)
        if (user instanceof AdministratorModel && !isSuperAdmin(command.adminId())) {
            throw new SecurityException("Only super admin can deactivate administrators");
        }

        user.setStatus(UserStatus.SUSPENDED);
        userRepository.save(user);

        //LOG DE L'ACTION
        // auditPort.logAdminAction(command.adminId(), "DEACTIVATE_USER", command.userId(), command.reason());
    }

    private boolean isSuperAdmin(UUID adminId) {
        // Implémenter la logique de vérification des super admins
        return userRepository.findById(adminId)
                .filter(user -> user instanceof AdministratorModel)
                .map(admin -> ((AdministratorModel) admin).getAccessLevel())
                .map(level -> level == AccessLevel.SUPER_ADMIN) // À créer cet enum
                .orElse(false);
    }

    @Override
    public List<UserProfileDto> searchUsers(SearchUsersQuery query) {
        List<UserModel> users = userRepository.findAll().stream()
                .filter(user -> matchesSearchCriteria(user, query))
                .toList();

        return users.stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<UserProfileDto> getUsersByStatus(UserStatus status) {
        return userRepository.findByStatus(status).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<UserProfileDto> getUsersByType(String userType) {
        return userRepository.findByUserType(userType).stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public UserProfileDto getCurrentUserProfile(UUID userId) {
        // Mettre à jour lastLogin
        UserModel user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + userId));

        user.setLastLogin(java.time.LocalDateTime.now());
        userRepository.save(user);

        return toDto(user);
    }

    // Méthode helper pour la recherche
    private boolean matchesSearchCriteria(UserModel user, SearchUsersQuery query) {
        if (query.username() != null && !user.getUsername().contains(query.username())) {
            return false;
        }
        if (query.email() != null && !user.getEmail().contains(query.email())) {
            return false;
        }
        if (query.status() != null && user.getStatus() != query.status()) {
            return false;
        }
        if (query.userType() != null && !user.getUserType().equals(query.userType())) {
            return false;
        }

        // Filtre réputation (seulement pour les citoyens)
        if (user instanceof CitizenModel citizen) {
            if (query.minReputation() != null && citizen.getReputation() < query.minReputation()) {
                return false;
            }
            if (query.maxReputation() != null && citizen.getReputation() > query.maxReputation()) {
                return false;
            }
        }

        // Filtre date de création
        if (query.createdAfter() != null && user.getCreatedAt().isBefore(query.createdAfter())) {
            return false;
        }
        if (query.createdBefore() != null && user.getCreatedAt().isAfter(query.createdBefore())) {
            return false;
        }

        return true;
    }

    // Méthode de conversion UserModel → UserProfileDto
    private UserProfileDto toDto(UserModel user) {
        UserProfileDto.UserProfileDtoBuilder builder = UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                .status(user.getStatus())
                .userType(user.getUserType());

        // Ajouter les champs spécifiques selon le type
        if (user instanceof CitizenModel citizen) {
            builder.reputation(citizen.getReputation())
                    .votingPower(citizen.getVotingPower());
        }

        if (user instanceof ModeratorModel moderator) {
            builder.approvedAt(moderator.getApprovedAt())
                    .moderationScope(moderator.getModerationScope())
                    .approvedBy(moderator.getApprovedBy());
        }

        if (user instanceof AdministratorModel admin) {
            builder.accessLevel(admin.getAccessLevel())
                    .department(admin.getDepartment());
        }

        return builder.build();
    }
}