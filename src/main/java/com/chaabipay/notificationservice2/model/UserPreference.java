package com.chaabipay.notificationservice2.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data

public class UserPreference {
    @Id // Clé primaire
    private String userId; // Utilisera l'ID utilisateur comme clé primaire

    // Préférences par canal
    private boolean pushEnabled = true;   // Notifications push activées par défaut
    private boolean inAppEnabled = true;  // Notifications in-app activées par défaut
    private boolean smsEnabled = true;    // Notifications SMS activées par défaut
    private boolean emailEnabled = true;  // Notifications email activées par défaut

    // Préférences par type
    private boolean paymentRemindersEnabled = true; // Rappels de paiement activés par défaut
    private boolean crcNotificationsEnabled = true; // Notifications CRC activées par défaut
}
