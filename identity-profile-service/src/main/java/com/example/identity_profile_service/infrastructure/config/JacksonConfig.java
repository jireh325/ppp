package com.example.identity_profile_service.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import com.example.identity_profile_service.domain.model.*;
import com.example.identity_profile_service.infrastructure.serialization.json.*;

@Configuration
public class JacksonConfig {

    @Bean
    public SimpleModule customSerializationModule() {
        SimpleModule module = new SimpleModule("SecuritySerializationModule");

        // Serializers
        module.addSerializer(UserModel.class, new UserModelSerializer());
        module.addSerializer(CitizenModel.class, new CitizenModelSerializer());
        module.addSerializer(AdministratorModel.class, new AdministratorModelSerializer());
        module.addSerializer(ModeratorModel.class, new ModeratorModelSerializer());


        // Deserializers
        module.addDeserializer(UserModel.class, new UserModelDeserializer());
        module.addDeserializer(CitizenModel.class, new CitizenModelDeserializer());
        module.addDeserializer(AdministratorModel.class, new AdministratorModelDeserializer());
        module.addDeserializer(ModeratorModel.class, new ModeratorModelDeserializer());

        return module;
    }

    @Bean
    public ObjectMapper objectMapper(SimpleModule customSerializationModule) {
        ObjectMapper mapper = new ObjectMapper();

        // Configuration de base
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // Modules standards
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(customSerializationModule);

        // Configuration polymorphique pour Redis
        mapper.activateDefaultTyping(
                mapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        return mapper;
    }
}
