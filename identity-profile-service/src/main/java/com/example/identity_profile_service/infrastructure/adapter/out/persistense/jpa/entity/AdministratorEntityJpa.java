package com.example.identity_profile_service.infrastructure.adapter.out.persistense.jpa.entity;

import com.example.identity_profile_service.domain.enums.AccessLevel;
import com.example.identity_profile_service.domain.enums.Department;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@PrimaryKeyJoinColumn(name = "user_id")
@Table(name = "administrators")
public class AdministratorEntityJpa extends UserEntityJpa {

    @Enumerated(EnumType.STRING)
    @Column(name = "access_level", nullable = false, length = 20)
    private AccessLevel accessLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Department department;
}