package com.example.identity_profile_service.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class CitizenModel extends UserModel {
    private Integer reputation;
    private Integer votingPower;

    public CitizenModel() {
        super();
        this.reputation = 0;
        this.votingPower = 1;
    }

    @Override
    public String getUserType() {
        return "CITIZEN";
    }

    // Méthodes liées uniquement à l'identité
    public void increaseReputation(int points) {
        this.reputation += points;
    }

    public void decreaseReputation(int points) {
        this.reputation = Math.max(0, this.reputation - points);
    }

    public void updateVotingPower() {
        // Le pouvoir de vote évolue avec la réputation
        if (this.reputation >= 1500) {
            this.votingPower = 5; // Citoyen très influent
        } else if (this.reputation >= 1000) {
            this.votingPower = 4; // influent
        } else if (this.reputation >= 500) {
            this.votingPower = 3; // Citoyen actif
        } else if (this.reputation >= 100) {
            this.votingPower = 2; // Citoyen engagé
        } else {
            this.votingPower = 1; // Nouveau citoyen
        }
    }
}