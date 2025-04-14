package com.chaabipay.notificationservice2.controller;

import com.chaabipay.notificationservice2.dto.UserPreferenceRequest;
import com.chaabipay.notificationservice2.dto.UserPreferenceResponse;
import com.chaabipay.notificationservice2.mapper.UserPreferenceMapper;
import com.chaabipay.notificationservice2.model.UserPreference;
import com.chaabipay.notificationservice2.service.UserPreferenceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/preferences")
public class UserPreferenceController {

    private final UserPreferenceService userPreferenceService;
    private final UserPreferenceMapper userPreferenceMapper;

    @Autowired
    public UserPreferenceController(UserPreferenceService userPreferenceService, UserPreferenceMapper userPreferenceMapper) {
        this.userPreferenceService = userPreferenceService;
        this.userPreferenceMapper = userPreferenceMapper;
    }

    /**
     * Récupère les préférences d'un utilisateur
     * @param userId L'identifiant de l'utilisateur
     * @return Les préférences de l'utilisateur
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserPreferenceResponse> getUserPreference(@PathVariable String userId) {
        UserPreference preference = userPreferenceService.getUserPreference(userId);
        return ResponseEntity.ok(userPreferenceMapper.toDto(preference));
    }

    /**
     * Récupère toutes les préférences utilisateur
     * @return La liste des préférences utilisateur
     */
    @GetMapping
    public ResponseEntity<List<UserPreferenceResponse>> getAllUserPreferences() {
        List<UserPreference> preferences = userPreferenceService.getAllUserPreferences();
        return ResponseEntity.ok(userPreferenceMapper.toDtoList(preferences));
    }

    /**
     * Crée ou met à jour les préférences d'un utilisateur
     * @param request DTO contenant les préférences utilisateur
     * @return Les préférences créées ou mises à jour
     */
    @PostMapping
    public ResponseEntity<UserPreferenceResponse> createUserPreference(@Valid @RequestBody UserPreferenceRequest request) {
        UserPreference preference = userPreferenceMapper.toEntity(request);
        UserPreference savedPreference = userPreferenceService.saveUserPreference(preference);
        return new ResponseEntity<>(userPreferenceMapper.toDto(savedPreference), HttpStatus.CREATED);
    }

    /**
     * Met à jour les préférences d'un utilisateur
     * @param userId L'identifiant de l'utilisateur
     * @param request DTO contenant les préférences utilisateur
     * @return Les préférences mises à jour
     */
    @PutMapping("/{userId}")
    public ResponseEntity<UserPreferenceResponse> updateUserPreference(
            @PathVariable String userId,
            @Valid @RequestBody UserPreferenceRequest request) {

        if (!userId.equals(request.getUserId())) {
            return ResponseEntity.badRequest().build();
        }

        UserPreference preference = userPreferenceMapper.toEntity(request);
        UserPreference updatedPreference = userPreferenceService.updateUserPreference(userId, preference);
        return ResponseEntity.ok(userPreferenceMapper.toDto(updatedPreference));
    }

    /**
     * Active ou désactive un canal de notification pour un utilisateur
     * @param userId L'identifiant de l'utilisateur
     * @param channel Le canal à configurer (PUSH, IN_APP, SMS, EMAIL)
     * @param enabled true pour activer, false pour désactiver
     * @return Les préférences mises à jour
     */
    @PutMapping("/{userId}/channel/{channel}")
    public ResponseEntity<UserPreferenceResponse> enableNotificationChannel(
            @PathVariable String userId,
            @PathVariable String channel,
            @RequestParam boolean enabled) {

        UserPreference updatedPreference = userPreferenceService.enableNotificationChannel(userId, channel, enabled);
        return ResponseEntity.ok(userPreferenceMapper.toDto(updatedPreference));
    }

    /**
     * Active ou désactive un type de notification pour un utilisateur
     * @param userId L'identifiant de l'utilisateur
     * @param type Le type à configurer (PAYMENT_REMINDER, CRC_NOTIFICATIONS)
     * @param enabled true pour activer, false pour désactiver
     * @return Les préférences mises à jour
     */
    @PutMapping("/{userId}/type/{type}")
    public ResponseEntity<UserPreferenceResponse> enableNotificationType(
            @PathVariable String userId,
            @PathVariable String type,
            @RequestParam boolean enabled) {

        UserPreference updatedPreference = userPreferenceService.enableNotificationType(userId, type, enabled);
        return ResponseEntity.ok(userPreferenceMapper.toDto(updatedPreference));
    }

    /**
     * Supprime les préférences d'un utilisateur
     * @param userId L'identifiant de l'utilisateur
     * @return 204 No Content si la suppression a réussi, 404 Not Found sinon
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserPreference(@PathVariable String userId) {
        boolean deleted = userPreferenceService.deleteUserPreference(userId);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}