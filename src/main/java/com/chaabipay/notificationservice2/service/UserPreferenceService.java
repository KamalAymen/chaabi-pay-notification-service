package com.chaabipay.notificationservice2.service;

import com.chaabipay.notificationservice2.model.UserPreference;
import java.util.List;
import java.util.Optional;

public interface UserPreferenceService {

    /**
     * Récupère les préférences d'un utilisateur
     * @param userId L'identifiant de l'utilisateur
     * @return Les préférences de l'utilisateur
     */
    UserPreference getUserPreference(String userId);

    /**
     * Récupère tous les préférences de tous les utilisateurs
     * @return La liste des préférences
     */
    List<UserPreference> getAllUserPreferences();

    /**
     * Crée ou met à jour les préférences d'un utilisateur
     * @param userPreference Les préférences à enregistrer
     * @return Les préférences enregistrées
     */
    UserPreference saveUserPreference(UserPreference userPreference);

    /**
     * Met à jour les préférences d'un utilisateur
     * @param userId L'identifiant de l'utilisateur
     * @param userPreference Les nouvelles préférences
     * @return Les préférences mises à jour
     */
    UserPreference updateUserPreference(String userId, UserPreference userPreference);

    /**
     * Supprime les préférences d'un utilisateur
     * @param userId L'identifiant de l'utilisateur
     * @return true si supprimé, false sinon
     */
    boolean deleteUserPreference(String userId);

    /**
     * Active ou désactive un canal de notification pour un utilisateur
     * @param userId L'identifiant de l'utilisateur
     * @param channel Le canal à configurer (PUSH, IN_APP, SMS, EMAIL)
     * @param enabled true pour activer, false pour désactiver
     * @return Les préférences mises à jour
     */
    UserPreference enableNotificationChannel(String userId, String channel, boolean enabled);

    /**
     * Active ou désactive un type de notification pour un utilisateur
     * @param userId L'identifiant de l'utilisateur
     * @param type Le type à configurer (PAYMENT_REMINDER, CRC_NOTIFICATIONS)
     * @param enabled true pour activer, false pour désactiver
     * @return Les préférences mises à jour
     */
    UserPreference enableNotificationType(String userId, String type, boolean enabled);
}