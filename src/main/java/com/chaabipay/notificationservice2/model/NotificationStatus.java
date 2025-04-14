package com.chaabipay.notificationservice2.model;

public enum NotificationStatus {
    PENDING,   // En attente d'envoi
    SENT,      // Envoyée
    DELIVERED, // Livrée au destinataire
    READ,      // Lue par l'utilisateur
    FAILED     // Échec d'envoi
}
