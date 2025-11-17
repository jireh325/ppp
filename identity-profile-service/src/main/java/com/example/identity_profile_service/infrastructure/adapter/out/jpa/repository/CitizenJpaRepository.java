package com.example.identity_profile_service.infrastructure.adapter.out.jpa.repository;

import com.example.identity_profile_service.infrastructure.adapter.out.jpa.entity.CitizenEntityJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CitizenJpaRepository extends JpaRepository<CitizenEntityJpa, UUID> {

    @Query("SELECT c FROM CitizenEntityJpa c WHERE c.reputation >= :minReputation AND c.reputation <= :maxReputation")
    List<CitizenEntityJpa> findByReputationBetween(@Param("minReputation") Integer minReputation,
                                                @Param("maxReputation") Integer maxReputation);

    @Query("SELECT c FROM CitizenEntityJpa c WHERE c.reputation >= :minReputation")
    List<CitizenEntityJpa> findByMinReputation(@Param("minReputation") Integer minReputation);
}
