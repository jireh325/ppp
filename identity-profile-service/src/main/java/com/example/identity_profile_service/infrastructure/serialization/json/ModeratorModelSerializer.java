package com.example.identity_profile_service.infrastructure.serialization.json;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.example.identity_profile_service.domain.model.ModeratorModel;
import com.example.identity_profile_service.domain.enums.ModerationScopeType;

public class ModeratorModelSerializer extends JsonSerializer<ModeratorModel> {

    @Override
    public void serialize(ModeratorModel value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        gen.writeStartObject();
        gen.writeStringField("@class", ModeratorModel.class.getName());

        // Champs de UserModel
        gen.writeStringField("id", value.getId() != null ? value.getId().toString() : null);
        gen.writeStringField("username", value.getUsername());
        gen.writeStringField("password", value.getPassword());
        gen.writeStringField("email", value.getEmail());
        gen.writeStringField("firstName", value.getFirstName());
        gen.writeStringField("lastName", value.getLastName());

        if (value.getCreatedAt() != null) {
            gen.writeStringField("createdAt", value.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME));
        } else {
            gen.writeNullField("createdAt");
        }

        if (value.getLastLogin() != null) {
            gen.writeStringField("lastLogin", value.getLastLogin().format(DateTimeFormatter.ISO_DATE_TIME));
        } else {
            gen.writeNullField("lastLogin");
        }

        if (value.getRole() != null) {
            gen.writeStringField("role", value.getRole().name());
        } else {
            gen.writeNullField("role");
        }

        if (value.getStatus() != null) {
            gen.writeStringField("status", value.getStatus().name());
        } else {
            gen.writeNullField("status");
        }

        gen.writeStringField("urlProfil", value.getUrlProfil());
        gen.writeStringField("userType", value.getUserType());

        // Champs de CitizenModel
        gen.writeNumberField("reputation", value.getReputation());
        gen.writeNumberField("votingPower", value.getVotingPower());

        // Champs spécifiques à ModeratorModel
        if (value.getApprovedAt() != null) {
            gen.writeStringField("approvedAt", value.getApprovedAt().format(DateTimeFormatter.ISO_DATE_TIME));
        } else {
            gen.writeNullField("approvedAt");
        }

        if (value.getModerationScope() != null) {
            gen.writeStringField("moderationScope", value.getModerationScope().name());
        } else {
            gen.writeNullField("moderationScope");
        }

        gen.writeStringField("approvedBy", value.getApprovedBy());

        gen.writeEndObject();
    }

    @Override
    public void serializeWithType(ModeratorModel value, JsonGenerator gen, SerializerProvider serializers,
                                  TypeSerializer typeSer) throws IOException {
        serialize(value, gen, serializers);
    }
}