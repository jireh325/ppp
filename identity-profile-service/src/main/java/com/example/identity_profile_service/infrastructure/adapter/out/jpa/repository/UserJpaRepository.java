package com.example.identity_profile_service.infrastructure.adapter.out.jpa.repository;

import com.example.identity_profile_service.domain.enums.UserStatus;
import com.example.identity_profile_service.infrastructure.adapter.out.jpa.entity.UserEntityJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserJpaRepository extends JpaRepository<UserEntityJpa, UUID> {

    Optional<UserEntityJpa> findByEmail(String email);
    Optional<UserEntityJpa> findByUsername(String username);

    List<UserEntityJpa> findByStatus(UserStatus status);
    List<UserEntityJpa> findByUserType(String userType);

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    @Query("SELECT COUNT(u) FROM UserEntityJpa u WHERE u.userType = :userType")
    Long countByUserType(@Param("userType") String userType);

    @Query("SELECT COUNT(u) FROM UserEntityJpa u WHERE u.status = 'ACTIVE'")
    Long countActiveUsers();
}
