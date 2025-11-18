package com.example.identity_profile_service.infrastructure.adapter.out.persistense.jpa.repository;

import com.example.identity_profile_service.domain.enums.ModerationScopeType;
import com.example.identity_profile_service.infrastructure.adapter.out.persistense.jpa.entity.ModeratorEntityJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ModeratorJpaRepository extends JpaRepository<ModeratorEntityJpa, UUID> {

    List<ModeratorEntityJpa> findByModerationScope(ModerationScopeType scope);

    @Query("SELECT m FROM ModeratorEntityJpa m WHERE m.moderationScope IN :scopes")
    List<ModeratorEntityJpa> findByModerationScopes(@Param("scopes") List<ModerationScopeType> scopes);
}
