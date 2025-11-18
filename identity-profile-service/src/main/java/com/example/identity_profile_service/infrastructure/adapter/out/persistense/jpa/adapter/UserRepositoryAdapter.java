package com.example.identity_profile_service.infrastructure.adapter.out.persistense.jpa.adapter;

import com.example.identity_profile_service.application.port.out.UserRepositoryPort;
import com.example.identity_profile_service.domain.enums.Department;
import com.example.identity_profile_service.domain.enums.ModerationScopeType;
import com.example.identity_profile_service.domain.enums.UserStatus;
import com.example.identity_profile_service.domain.model.*;
import com.example.identity_profile_service.infrastructure.adapter.out.persistense.jpa.entity.*;
import com.example.identity_profile_service.infrastructure.adapter.out.persistense.jpa.repository.*;
import com.example.identity_profile_service.infrastructure.mapper.UserEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository userJpaRepository;
    private final CitizenJpaRepository citizenJpaRepository;
    private final ModeratorJpaRepository moderatorJpaRepository;
    private final AdministratorJpaRepository administratorJpaRepository;
    private final UserEntityMapper userEntityMapper;

    @Override
    public Optional<UserModel> findById(UUID id) {
        return userJpaRepository.findById(id)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<UserModel> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public Optional<UserModel> findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .map(userEntityMapper::toDomain);
    }

    @Override
    public List<UserModel> findAll() {
        return userJpaRepository.findAll().stream()
                .map(userEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public UserModel save(UserModel user) {
        UserEntityJpa entity = userEntityMapper.toEntity(user);
        UserEntityJpa savedEntity = userJpaRepository.save(entity);
        return userEntityMapper.toDomain(savedEntity);
    }

    @Override
    public void delete(UUID id) {
        userJpaRepository.deleteById(id);
    }

    @Override
    public List<UserModel> findByStatus(UserStatus status) {
        return userJpaRepository.findByStatus(status).stream()
                .map(userEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserModel> findByUserType(String userType) {
        return userJpaRepository.findByUserType(userType).stream()
                .map(userEntityMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<ModeratorModel> findModeratorsByScope(ModerationScopeType scope) {
        return moderatorJpaRepository.findByModerationScope(scope).stream()
                .map(userEntityMapper::toDomainModerator)
                .collect(Collectors.toList());
    }

    @Override
    public List<ModeratorModel> findAllModerators() {
        return moderatorJpaRepository.findAll().stream()
                .map(userEntityMapper::toDomainModerator)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdministratorModel> findAdministratorsByDepartment(Department department) {
        return administratorJpaRepository.findByDepartment(department).stream()
                .map(userEntityMapper::toDomainAdministrator)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdministratorModel> findAllAdministrators() {
        return administratorJpaRepository.findAll().stream()
                .map(userEntityMapper::toDomainAdministrator)
                .collect(Collectors.toList());
    }

    @Override
    public Long countByUserType(String userType) {
        return userJpaRepository.countByUserType(userType);
    }

    @Override
    public Long countActiveUsers() {
        return userJpaRepository.countActiveUsers();
    }

    @Override
    public boolean existsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }
}
