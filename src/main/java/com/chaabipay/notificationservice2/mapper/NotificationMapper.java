package com.chaabipay.notificationservice2.mapper;

import com.chaabipay.notificationservice2.dto.NotificationRequest;
import com.chaabipay.notificationservice2.dto.NotificationResponse;
import com.chaabipay.notificationservice2.model.Notification;
import com.chaabipay.notificationservice2.model.NotificationStatus;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationMapper {

    private final ObjectMapper objectMapper;

    public NotificationMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Convertit un DTO de requête en entité Notification
     */
    public Notification toEntity(NotificationRequest request) {
        Notification notification = new Notification();
        notification.setUserId(request.getRecipientId());
        notification.setTitle(request.getTitle());
        notification.setContent(request.getContent());
        notification.setType(request.getType());
        notification.setChannel(request.getChannel());
        notification.setStatus(NotificationStatus.PENDING);
        notification.setImageUrl(request.getImageUrl());

        // Si vous avez besoin de stocker les données dans le payload
        if (request.getData() != null) {
            try {
                notification.setPayload(objectMapper.writeValueAsString(request.getData()));
            } catch (Exception e) {
                notification.setPayload("{}");
            }
        }

        return notification;
    }

    /**
     * Convertit une entité Notification en DTO de réponse
     */
    public NotificationResponse toDto(Notification notification) {
        NotificationResponse response = new NotificationResponse();
        response.setId(notification.getId());
        response.setRecipientId(notification.getUserId());
        response.setTitle(notification.getTitle());
        response.setContent(notification.getContent());
        response.setType(notification.getType());
        response.setChannel(notification.getChannel());
        response.setStatus(notification.getStatus());
        response.setImageUrl(notification.getImageUrl());
        response.setCreatedAt(notification.getCreatedAt());
        response.setSentAt(notification.getSentAt());
        response.setReadAt(notification.getReadAt());

        // Si vous avez besoin de récupérer les données du payload
        if (notification.getPayload() != null && !notification.getPayload().isEmpty()) {
            try {
                response.setData(objectMapper.readValue(notification.getPayload(),
                        objectMapper.getTypeFactory().constructMapType(
                                java.util.Map.class, String.class, String.class)));
            } catch (Exception e) {
                response.setData(java.util.Collections.emptyMap());
            }
        }

        return response;
    }

    /**
     * Convertit une liste d'entités Notification en liste de DTOs
     */
    public List<NotificationResponse> toDtoList(List<Notification> notifications) {
        return notifications.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}