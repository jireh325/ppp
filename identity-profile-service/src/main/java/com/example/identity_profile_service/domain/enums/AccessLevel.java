package com.example.identity_profile_service.domain.enums;

public enum AccessLevel {
    SUPER_ADMIN("Super Administrateur - Accès complet système"),
    MUNICIPAL("Administrateur municipal - accès complet"),
    DISTRICT("Administrateur de district - accès limité à un district"),
    DEPARTMENTAL("Administrateur départemental - accès à un département"),
    TECHNICAL("Administrateur technique - accès aux fonctionnalités techniques uniquement"),
    SUPPORT("Support utilisateur - accès limité aux fonctions de support");

    private final String description;

    AccessLevel(String description) {
        this.description = description;
    }

    public String getDescription() { return description; }
}
