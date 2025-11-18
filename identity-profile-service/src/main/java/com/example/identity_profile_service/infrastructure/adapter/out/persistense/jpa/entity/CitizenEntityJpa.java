package com.example.identity_profile_service.infrastructure.adapter.out.persistense.jpa.entity;


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
@Table(name = "citizens")
public class CitizenEntityJpa extends UserEntityJpa {

    @Column(nullable = false)
    private Integer reputation;

    @Column(name = "voting_power", nullable = false)
    private Integer votingPower;
}
