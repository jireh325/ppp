package com.example.identity_profile_service.infrastructure.serialization.json;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.example.identity_profile_service.domain.model.CitizenModel;

public class CitizenModelSerializer extends JsonSerializer<CitizenModel> {

    @Override
    public void serialize(CitizenModel value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }

        gen.writeStartObject();
        gen.writeStringField("@class", CitizenModel.class.getName());

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

        // Champs spécifiques à CitizenModel
        gen.writeNumberField("reputation", value.getReputation());
        gen.writeNumberField("votingPower", value.getVotingPower());

        gen.writeEndObject();
    }

    @Override
    public void serializeWithType(CitizenModel value, JsonGenerator gen, SerializerProvider serializers,
                                  TypeSerializer typeSer) throws IOException {
        serialize(value, gen, serializers);
    }
}
