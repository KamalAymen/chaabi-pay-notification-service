package com.chaabipay.notificationservice2.model;

public enum NotificationType {
    PAYMENT_REMINDER,       // Pour les rappels de paiement
    CRC_ADVISOR_AVAILABLE,  // Quand un conseiller est disponible
    CRC_ADVISOR_UNAVAILABLE, // Quand un conseiller est indisponible
    CRC_VERIFICATION_COMPLETED, // Quand une vérification est terminée
    GENERAL                  // Pour les notifications générales
}
