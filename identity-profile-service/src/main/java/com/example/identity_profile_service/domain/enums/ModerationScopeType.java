package com.example.identity_profile_service.domain.enums;

public enum ModerationScopeType {
    GLOBAL("Modération globale - accès complet"),
    CATEGORY("Modération par catégorie d'idées"),
    DISTRICT("Modération par district géographique"),
    PARTY("Modération spécifique à un parti"),
    TECHNICAL("Modération technique - spam, bots"),
    CONTENT_QUALITY("Modération qualité de contenu");

    private final String description;

    ModerationScopeType(String description) {
        this.description = description;
    }
}