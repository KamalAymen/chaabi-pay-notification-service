package com.chaabipay.notificationservice2.dto;

import com.chaabipay.notificationservice2.model.NotificationChannel;
import com.chaabipay.notificationservice2.model.NotificationType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public class NotificationRequest {

    @NotBlank(message = "Le destinataire est obligatoire")
    private String recipientId;

    @NotBlank(message = "Le titre est obligatoire")
    private String title;

    @NotBlank(message = "Le contenu est obligatoire")
    private String content;

    @NotNull(message = "Le type de notification est obligatoire")
    private NotificationType type;

    @NotNull(message = "Le canal de notification est obligatoire")
    private NotificationChannel channel;

    private Map<String, String> data;

    private String imageUrl;

    private String actionUrl;

    // Topic pour envoyer à un groupe d'utilisateurs (optionnel)
    private String topic;

    // Constructeurs, getters et setters

    public NotificationRequest() {
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public void setChannel(NotificationChannel channel) {
        this.channel = channel;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}