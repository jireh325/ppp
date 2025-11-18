package com.example.identity_profile_service.infrastructure.adapter.out.persistense.jpa.repository;

import com.example.identity_profile_service.domain.enums.AccessLevel;
import com.example.identity_profile_service.domain.enums.Department;
import com.example.identity_profile_service.infrastructure.adapter.out.persistense.jpa.entity.AdministratorEntityJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdministratorJpaRepository extends JpaRepository<AdministratorEntityJpa, UUID> {

    List<AdministratorEntityJpa> findByAccessLevel(AccessLevel accessLevel);
    List<AdministratorEntityJpa> findByDepartment(Department department);

    @Query("SELECT a FROM AdministratorEntityJpa a WHERE a.accessLevel = :accessLevel AND a.department = :department")
    List<AdministratorEntityJpa> findByAccessLevelAndDepartment(@Param("accessLevel") AccessLevel accessLevel,
                                                             @Param("department") Department department);
}