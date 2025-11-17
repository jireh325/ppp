package com.example.identity_profile_service.infrastructure.adapter.out.jpa.entity;

import com.example.identity_profile_service.domain.enums.ModerationScopeType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "citizen_id")
@Table(name = "moderators")
public class ModeratorEntityJpa extends CitizenEntityJpa {

    @Column(name = "approved_at", nullable = false)
    private LocalDateTime approvedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "moderation_scope", nullable = false, length = 50)
    private ModerationScopeType moderationScope;

    @Column(name = "approved_by", length = 36) // UUID length
    private String approvedBy;
}
