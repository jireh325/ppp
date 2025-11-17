package com.example.identity_profile_service.application.port.out;

import com.example.identity_profile_service.domain.enums.Department;
import com.example.identity_profile_service.domain.enums.ModerationScopeType;
import com.example.identity_profile_service.domain.enums.UserStatus;
import com.example.identity_profile_service.domain.model.AdministratorModel;
import com.example.identity_profile_service.domain.model.ModeratorModel;
import com.example.identity_profile_service.domain.model.UserModel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepositoryPort {

    // Opérations CRUD de base
    Optional<UserModel> findById(UUID id);
    Optional<UserModel> findByEmail(String email);
    Optional<UserModel> findByUsername(String username);
    List<UserModel> findAll();
    UserModel save(UserModel user);
    void delete(UUID id);

    // Recherches spécifiques
    List<UserModel> findByStatus(UserStatus status);
    List<UserModel> findByUserType(String userType);

    // Recherche de modérateurs
    List<ModeratorModel> findModeratorsByScope(ModerationScopeType scope);
    List<ModeratorModel> findAllModerators();

    // Recherche d'administrateurs
    List<AdministratorModel> findAdministratorsByDepartment(Department department);
    List<AdministratorModel> findAllAdministrators();

    // Statistiques
    Long countByUserType(String userType);
    Long countActiveUsers();

    // Vérifications
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}