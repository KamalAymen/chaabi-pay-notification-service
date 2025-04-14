package com.chaabipay.notificationservice2.dto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class UserPreferenceResponse {

    private String userId;
    private boolean pushEnabled;
    private boolean inAppEnabled;
    private boolean smsEnabled;
    private boolean emailEnabled;
    private boolean paymentRemindersEnabled;
    private boolean crcNotificationsEnabled;

    // Constructeur par défaut
    public UserPreferenceResponse() {
    }

    // Getters et Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isPushEnabled() {
        return pushEnabled;
    }

    public void setPushEnabled(boolean pushEnabled) {
        this.pushEnabled = pushEnabled;
    }

    public boolean isInAppEnabled() {
        return inAppEnabled;
    }

    public void setInAppEnabled(boolean inAppEnabled) {
        this.inAppEnabled = inAppEnabled;
    }

    public boolean isSmsEnabled() {
        return smsEnabled;
    }

    public void setSmsEnabled(boolean smsEnabled) {
        this.smsEnabled = smsEnabled;
    }

    public boolean isEmailEnabled() {
        return emailEnabled;
    }

    public void setEmailEnabled(boolean emailEnabled) {
        this.emailEnabled = emailEnabled;
    }

    public boolean isPaymentRemindersEnabled() {
        return paymentRemindersEnabled;
    }

    public void setPaymentRemindersEnabled(boolean paymentRemindersEnabled) {
        this.paymentRemindersEnabled = paymentRemindersEnabled;
    }

    public boolean isCrcNotificationsEnabled() {
        return crcNotificationsEnabled;
    }

    public void setCrcNotificationsEnabled(boolean crcNotificationsEnabled) {
        this.crcNotificationsEnabled = crcNotificationsEnabled;
    }

    // Méthode utilitaire pour convertir en format Map
    public Map<String, Map<String, Boolean>> toPreferencesMap() {
        Map<String, Map<String, Boolean>> result = new HashMap<>();

        // Préférences pour les rappels de paiement
        Map<String, Boolean> paymentChannels = new HashMap<>();
        paymentChannels.put("PUSH", pushEnabled);
        paymentChannels.put("IN_APP", inAppEnabled);
        paymentChannels.put("SMS", smsEnabled);
        paymentChannels.put("EMAIL", emailEnabled);
        result.put("PAYMENT_REMINDER", paymentChannels);

        // Préférences pour les notifications CRC
        Map<String, Boolean> crcChannels = new HashMap<>();
        crcChannels.put("PUSH", pushEnabled);
        crcChannels.put("IN_APP", inAppEnabled);
        crcChannels.put("SMS", smsEnabled);
        crcChannels.put("EMAIL", emailEnabled);
        result.put("CRC_NOTIFICATIONS", crcChannels);

        return result;
    }
}