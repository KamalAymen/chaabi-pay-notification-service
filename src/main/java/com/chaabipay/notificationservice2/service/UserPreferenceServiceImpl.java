package com.chaabipay.notificationservice2.service;

import com.chaabipay.notificationservice2.exception.ApiException;
import com.chaabipay.notificationservice2.model.UserPreference;
import com.chaabipay.notificationservice2.repository.UserPreferenceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserPreferenceServiceImpl implements UserPreferenceService {

    private static final Logger logger = LoggerFactory.getLogger(UserPreferenceServiceImpl.class);

    private final UserPreferenceRepository userPreferenceRepository;

    @Autowired
    public UserPreferenceServiceImpl(UserPreferenceRepository userPreferenceRepository) {
        this.userPreferenceRepository = userPreferenceRepository;
    }

    @Override
    public UserPreference getUserPreference(String userId) {
        return userPreferenceRepository.getOrCreateUserPreference(userId);
    }

    @Override
    public List<UserPreference> getAllUserPreferences() {
        return userPreferenceRepository.findAll();
    }

    @Override
    @Transactional
    public UserPreference saveUserPreference(UserPreference userPreference) {
        if (userPreference.getUserId() == null || userPreference.getUserId().isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "L'identifiant utilisateur est obligatoire");
        }
        return userPreferenceRepository.save(userPreference);
    }

    @Override
    @Transactional
    public UserPreference updateUserPreference(String userId, UserPreference userPreference) {
        UserPreference existingPreference = userPreferenceRepository.findById(userId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Préférences utilisateur non trouvées pour l'ID: " + userId));

        // Mettre à jour les préférences
        existingPreference.setPushEnabled(userPreference.isPushEnabled());
        existingPreference.setInAppEnabled(userPreference.isInAppEnabled());
        existingPreference.setSmsEnabled(userPreference.isSmsEnabled());
        existingPreference.setEmailEnabled(userPreference.isEmailEnabled());
        existingPreference.setPaymentRemindersEnabled(userPreference.isPaymentRemindersEnabled());
        existingPreference.setCrcNotificationsEnabled(userPreference.isCrcNotificationsEnabled());

        return userPreferenceRepository.save(existingPreference);
    }

    @Override
    @Transactional
    public boolean deleteUserPreference(String userId) {
        if (userPreferenceRepository.existsById(userId)) {
            userPreferenceRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public UserPreference enableNotificationChannel(String userId, String channel, boolean enabled) {
        UserPreference preference = userPreferenceRepository.getOrCreateUserPreference(userId);

        switch (channel.toUpperCase()) {
            case "PUSH":
                preference.setPushEnabled(enabled);
                break;
            case "IN_APP":
                preference.setInAppEnabled(enabled);
                break;
            case "SMS":
                preference.setSmsEnabled(enabled);
                break;
            case "EMAIL":
                preference.setEmailEnabled(enabled);
                break;
            default:
                throw new ApiException(HttpStatus.BAD_REQUEST, "Canal de notification non reconnu: " + channel);
        }

        logger.info("Canal de notification {} {} pour l'utilisateur {}",
                channel, enabled ? "activé" : "désactivé", userId);
        return userPreferenceRepository.save(preference);
    }

    @Override
    @Transactional
    public UserPreference enableNotificationType(String userId, String type, boolean enabled) {
        UserPreference preference = userPreferenceRepository.getOrCreateUserPreference(userId);

        switch (type.toUpperCase()) {
            case "PAYMENT_REMINDER":
                preference.setPaymentRemindersEnabled(enabled);
                break;
            case "CRC_NOTIFICATIONS":
                preference.setCrcNotificationsEnabled(enabled);
                break;
            default:
                throw new ApiException(HttpStatus.BAD_REQUEST, "Type de notification non reconnu: " + type);
        }

        logger.info("Type de notification {} {} pour l'utilisateur {}",
                type, enabled ? "activé" : "désactivé", userId);
        return userPreferenceRepository.save(preference);
    }
}