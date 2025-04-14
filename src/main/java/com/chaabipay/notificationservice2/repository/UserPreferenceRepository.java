package com.chaabipay.notificationservice2.repository;

import com.chaabipay.notificationservice2.model.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, String> {
    // Méthode utilitaire pour obtenir les préférences d'un utilisateur ou créer des préférences par défaut si inexistantes
    default UserPreference getOrCreateUserPreference(String userId) {
        return findById(userId).orElseGet(() -> {
            UserPreference newPreference = new UserPreference();
            newPreference.setUserId(userId);
            return save(newPreference);
        });
    }
}
