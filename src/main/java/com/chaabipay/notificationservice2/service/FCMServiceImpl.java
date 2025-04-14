package com.chaabipay.notificationservice2.service;

import com.chaabipay.notificationservice2.model.Notification;
import com.chaabipay.notificationservice2.model.NotificationStatus;
import com.chaabipay.notificationservice2.repository.NotificationRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.TopicManagementResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FCMServiceImpl implements FCMService {
    private final Logger logger = LoggerFactory.getLogger(FCMServiceImpl.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public String sendNotification(String token, String title, String body, Map<String, String> data)
            throws ExecutionException, InterruptedException {
        // Créer le message de notification
        Message message = Message.builder()
                .setToken(token)
                .setNotification(
                        com.google.firebase.messaging.Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build()
                )
                .putAllData(data != null ? data : new HashMap<>())
                .build();

        // Envoyer la notification et obtenir l'ID du message
        String messageId = FirebaseMessaging.getInstance().sendAsync(message).get();
        logger.info("Notification envoyée avec succès, messageId: {}", messageId);
        return messageId;
    }

    @Override
    public String sendNotificationToTopic(String topic, String title, String body, Map<String, String> data)
            throws ExecutionException, InterruptedException {
        // Créer le message de notification pour un topic
        Message message = Message.builder()
                .setTopic(topic)
                .setNotification(
                        com.google.firebase.messaging.Notification.builder()
                                .setTitle(title)
                                .setBody(body)
                                .build()
                )
                .putAllData(data != null ? data : new HashMap<>())
                .build();

        // Envoyer la notification et obtenir l'ID du message
        String messageId = FirebaseMessaging.getInstance().sendAsync(message).get();
        logger.info("Notification envoyée au topic {} avec succès, messageId: {}", topic, messageId);
        return messageId;
    }

    @Override
    public String sendNotification(Notification notification)
            throws ExecutionException, InterruptedException {
        // Mettre à jour le statut de la notification avant envoi
        notification.setStatus(NotificationStatus.PENDING);
        notification.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(notification);

        // Préparer les données additionnelles
        Map<String, String> data = new HashMap<>();
        if (notification.getId() != null) {
            data.put("notificationId", notification.getId().toString());
        }
        data.put("type", notification.getType().name());

        // Envoyer la notification
        String messageId;
        try {
            messageId = sendNotification(
                    notification.getRecipientToken(),
                    notification.getTitle(),
                    notification.getContent(),
                    data);

            // Mettre à jour le statut après envoi réussi
            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            notification.setFirebaseMessageId(messageId);
            notificationRepository.save(notification);

        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de la notification: {}", e.getMessage());
            notification.setStatus(NotificationStatus.FAILED);
            notificationRepository.save(notification);
            throw e;
        }

        return messageId;
    }

    @Override
    public void subscribeToTopic(String token, String topic) {
        try {
            // Abonner le token au topic
            TopicManagementResponse response = FirebaseMessaging.getInstance()
                    .subscribeToTopic(Collections.singletonList(token), topic);

            logger.info("Abonnement au topic {} réussi: {} succès, {} échecs",
                    topic, response.getSuccessCount(), response.getFailureCount());

        } catch (FirebaseMessagingException e) {
            logger.error("Erreur lors de l'abonnement au topic {}: {}", topic, e.getMessage());
        }
    }

    @Override
    public void unsubscribeFromTopic(String token, String topic) {
        try {
            // Désabonner le token du topic
            TopicManagementResponse response = FirebaseMessaging.getInstance()
                    .unsubscribeFromTopic(Collections.singletonList(token), topic);

            logger.info("Désabonnement du topic {} réussi: {} succès, {} échecs",
                    topic, response.getSuccessCount(), response.getFailureCount());

        } catch (FirebaseMessagingException e) {
            logger.error("Erreur lors du désabonnement du topic {}: {}", topic, e.getMessage());
        }
    }
}