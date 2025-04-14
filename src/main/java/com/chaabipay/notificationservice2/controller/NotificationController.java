package com.chaabipay.notificationservice2.controller;

import com.chaabipay.notificationservice2.dto.NotificationRequest;
import com.chaabipay.notificationservice2.dto.NotificationResponse;
import com.chaabipay.notificationservice2.mapper.NotificationMapper;
import com.chaabipay.notificationservice2.model.Notification;
import com.chaabipay.notificationservice2.model.NotificationStatus;
import com.chaabipay.notificationservice2.model.NotificationType;
import com.chaabipay.notificationservice2.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @Autowired
    public NotificationController(NotificationService notificationService, NotificationMapper notificationMapper) {
        this.notificationService = notificationService;
        this.notificationMapper = notificationMapper;
    }

    /**
     * Envoie une nouvelle notification
     * @param request DTO contenant les détails de la notification
     * @return La notification créée
     */
    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(@Valid @RequestBody NotificationRequest request) {
        Notification notification = notificationMapper.toEntity(request);
        Notification savedNotification = notificationService.sendNotification(notification);
        return new ResponseEntity<>(notificationMapper.toDto(savedNotification), HttpStatus.CREATED);
    }

    /**
     * Envoie une notification à un topic
     * @param request DTO contenant les détails de la notification
     * @return La notification créée
     */
    @PostMapping("/topic")
    public ResponseEntity<NotificationResponse> createTopicNotification(@Valid @RequestBody NotificationRequest request) {
        if (request.getTopic() == null || request.getTopic().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Notification notification = notificationMapper.toEntity(request);
        Notification savedNotification = notificationService.sendNotificationToTopic(notification);
        return new ResponseEntity<>(notificationMapper.toDto(savedNotification), HttpStatus.CREATED);
    }

    /**
     * Récupère toutes les notifications d'un utilisateur
     * @param userId L'identifiant de l'utilisateur
     * @return La liste des notifications
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByUserId(@PathVariable String userId) {
        List<Notification> notifications = notificationService.getNotificationsByUserId(userId);
        return ResponseEntity.ok(notificationMapper.toDtoList(notifications));
    }

    /**
     * Récupère les notifications d'un utilisateur avec un statut spécifique
     * @param userId L'identifiant de l'utilisateur
     * @param status Le statut des notifications
     * @return La liste des notifications
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByUserIdAndStatus(
            @PathVariable String userId,
            @PathVariable NotificationStatus status) {
        List<Notification> notifications = notificationService.getNotificationsByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(notificationMapper.toDtoList(notifications));
    }

    /**
     * Récupère les notifications d'un utilisateur avec un type spécifique
     * @param userId L'identifiant de l'utilisateur
     * @param type Le type des notifications
     * @return La liste des notifications
     */
    @GetMapping("/user/{userId}/type/{type}")
    public ResponseEntity<List<NotificationResponse>> getNotificationsByUserIdAndType(
            @PathVariable String userId,
            @PathVariable NotificationType type) {
        List<Notification> notifications = notificationService.getNotificationsByUserIdAndType(userId, type);
        return ResponseEntity.ok(notificationMapper.toDtoList(notifications));
    }

    /**
     * Récupère une notification par son ID
     * @param id L'identifiant de la notification
     * @return La notification, si elle existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getNotificationById(@PathVariable Long id) {
        Optional<Notification> notification = notificationService.getNotificationById(id);
        return notification
                .map(n -> ResponseEntity.ok(notificationMapper.toDto(n)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Marque une notification comme lue
     * @param id L'identifiant de la notification
     * @return La notification mise à jour, si elle existe
     */
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationResponse> markNotificationAsRead(@PathVariable Long id) {
        Optional<Notification> notification = notificationService.markNotificationAsRead(id);
        return notification
                .map(n -> ResponseEntity.ok(notificationMapper.toDto(n)))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Marque toutes les notifications d'un utilisateur comme lues
     * @param userId L'identifiant de l'utilisateur
     * @return Le nombre de notifications mises à jour
     */
    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<Integer> markAllNotificationsAsRead(@PathVariable String userId) {
        int count = notificationService.markAllNotificationsAsRead(userId);
        return ResponseEntity.ok(count);
    }

    /**
     * Supprime une notification
     * @param id L'identifiant de la notification
     * @return 204 No Content si la suppression a réussi, 404 Not Found sinon
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(@PathVariable Long id) {
        boolean deleted = notificationService.deleteNotification(id);
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }
}