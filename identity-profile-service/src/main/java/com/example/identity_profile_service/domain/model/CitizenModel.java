package com.example.identity_profile_service.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class CitizenModel extends UserModel {
    private Integer reputation;
    private Integer votingPower;
    private List<PartyMembership> partyMemberships;

    public CitizenModel() {
        super();
        this.reputation = 0;
        this.votingPower = 1;
        this.partyMemberships = new ArrayList<>();
    }

    @Override
    public String getUserType() {
        return "CITIZEN";
    }

    public void submitIdea() {
        // Logique de soumission d'idée
        this.reputation += 5;
    }

    public void vote() {
        // Logique de vote
        this.reputation += 1;
    }

    public PartyMembership joinParty(Party party) {
        PartyMembership membership = PartyMembership.builder()
                .citizen(this)
                .party(party)
                .role(MembershipRole.MEMBER)
                .isApproved(false)
                .build();

        this.partyMemberships.add(membership);
        return membership;
    }

    public void requestModeratorRole(String motivation) {
        // Cette méthode déclenchera un événement de domaine
        // pour créer une ModeratorRequest
    }
}