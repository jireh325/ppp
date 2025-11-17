package com.example.identity_profile_service.domain.enums;

public enum Department {
    // Géographique
    DISTRICT_NORD("District Nord"),
    DISTRICT_SUD("District Sud"),
    DISTRICT_EST("District Est"),
    DISTRICT_OUEST("District Ouest"),

    // Métier
    URBANISME("Urbanisme et aménagement"),
    ENVIRONNEMENT("Environnement et écologie"),
    SOCIAL("Affaires sociales"),
    CULTURE("Culture et loisirs"),
    TECHNIQUE("Services techniques"),
    FINANCES("Finances et budget"),

    // Spécial
    SUPPORT_TECHNIQUE("Support technique plateforme"),
    MODERATION_CENTRALE("Modération centrale");

    private final String label;

    Department(String label) {
        this.label = label;
    }

    public String getLabel() { return label; }
}