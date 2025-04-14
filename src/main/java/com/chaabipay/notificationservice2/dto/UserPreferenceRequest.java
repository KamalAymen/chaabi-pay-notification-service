package com.chaabipay.notificationservice2.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public class UserPreferenceRequest {

    @NotBlank(message = "L'identifiant utilisateur est obligatoire")
    private String userId;

    // Map avec clé = NotificationType.name(), valeur = Map<NotificationChannel.name(), Boolean>
    @NotNull(message = "Les préférences sont obligatoires")
    private Map<String, Map<String, Boolean>> preferences;

    // Constructeurs, getters et setters

    public UserPreferenceRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Map<String, Map<String, Boolean>> getPreferences() {
        return preferences;
    }

    public void setPreferences(Map<String, Map<String, Boolean>> preferences) {
        this.preferences = preferences;
    }
}