package com.chaabipay.notificationservice2.mapper;

import com.chaabipay.notificationservice2.dto.UserPreferenceRequest;
import com.chaabipay.notificationservice2.dto.UserPreferenceResponse;
import com.chaabipay.notificationservice2.model.UserPreference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserPreferenceMapper {

    /**
     * Convertit un DTO de requête en entité UserPreference
     */
    public UserPreference toEntity(UserPreferenceRequest request) {
        UserPreference preference = new UserPreference();
        preference.setUserId(request.getUserId());
        // Définir les préférences en fonction de la map dans la requête
        updatePreferencesFromMap(preference, request.getPreferences());
        return preference;
    }

    /**
     * Met à jour une entité existante avec les données d'un DTO
     */
    public void updateEntity(UserPreference entity, UserPreferenceRequest request) {
        // Mettre à jour les préférences en fonction de la map dans la requête
        updatePreferencesFromMap(entity, request.getPreferences());
    }

    /**
     * Convertit une entité UserPreference en DTO de réponse
     */
    public UserPreferenceResponse toDto(UserPreference preference) {
        UserPreferenceResponse response = new UserPreferenceResponse();
        // Compléter le DTO selon les champs disponibles
        return response;
    }

    /**
     * Convertit une liste d'entités UserPreference en liste de DTOs
     */
    public List<UserPreferenceResponse> toDtoList(List<UserPreference> preferences) {
        return preferences.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Convertit la structure de map en attributs booléens
     */
    private void updatePreferencesFromMap(UserPreference preference,
                                          java.util.Map<String, java.util.Map<String, Boolean>> preferencesMap) {
        if (preferencesMap == null) {
            return;
        }

        // Pour chaque type de notification
        preferencesMap.forEach((type, channelMap) -> {
            if (type.equals("PAYMENT_REMINDER")) {
                preference.setPaymentRemindersEnabled(getChannelPreference(channelMap, "ALL", true));
            } else if (type.startsWith("CRC_")) {
                preference.setCrcNotificationsEnabled(getChannelPreference(channelMap, "ALL", true));
            }

            // Pour chaque canal
            if (channelMap != null) {
                channelMap.forEach((channel, enabled) -> {
                    if (channel.equals("PUSH")) {
                        preference.setPushEnabled(enabled);
                    } else if (channel.equals("IN_APP")) {
                        preference.setInAppEnabled(enabled);
                    } else if (channel.equals("SMS")) {
                        preference.setSmsEnabled(enabled);
                    } else if (channel.equals("EMAIL")) {
                        preference.setEmailEnabled(enabled);
                    }
                });
            }
        });
    }

    /**
     * Récupère la préférence pour un canal spécifique
     */
    private boolean getChannelPreference(java.util.Map<String, Boolean> channelMap, String channel, boolean defaultValue) {
        if (channelMap == null || !channelMap.containsKey(channel)) {
            return defaultValue;
        }
        return channelMap.get(channel);
    }
}