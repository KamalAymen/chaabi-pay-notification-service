package com.chaabipay.notificationservice2.service;

import com.chaabipay.notificationservice2.exception.ApiException;
import com.chaabipay.notificationservice2.model.Notification;
import com.chaabipay.notificationservice2.model.NotificationChannel;
import com.chaabipay.notificationservice2.model.NotificationStatus;
import com.chaabipay.notificationservice2.model.NotificationType;
import com.chaabipay.notificationservice2.model.UserPreference;
import com.chaabipay.notificationservice2.repository.NotificationRepository;
import com.chaabipay.notificationservice2.repository.UserPreferenceRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    private final FCMService fcmService;
    private final ObjectMapper objectMapper;

    @Autowired
    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            UserPreferenceRepository userPreferenceRepository,
            FCMService fcmService,
            ObjectMapper objectMapper) {
        this.notificationRepository = notificationRepository;
        this.userPreferenceRepository = userPreferenceRepository;
        this.fcmService = fcmService;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public Notification sendNotification(Notification notification) {
        // Vérifier les préférences utilisateur
        if (!shouldSendNotification(notification)) {
            logger.info("Notification non envoyée car désactivée dans les préférences utilisateur: {}", notification);
            notification.setStatus(NotificationStatus.FAILED);
            notification.setSentAt(LocalDateTime.now());
            return notificationRepository.save(notification);
        }

        try {
            // Enregistrer la notification avec le statut PENDING
            notification.setStatus(NotificationStatus.PENDING);
            notification = notificationRepository.save(notification);

            // Envoyer la notification via le canal approprié
            switch (notification.getChannel()) {
                case PUSH:
                    // Vérifier si le token de l'appareil est disponible
                    if (notification.getRecipientToken() == null || notification.getRecipientToken().isEmpty()) {
                        throw new ApiException(HttpStatus.BAD_REQUEST, "Token FCM non spécifié pour l'envoi de notification PUSH");
                    }

                    // Créer les données additionnelles
                    Map<String, String> data = new HashMap<>();
                    data.put("notificationId", notification.getId().toString());
                    data.put("type", notification.getType().name());

                    // Utiliser le service FCM pour envoyer la notification
                    String messageId = fcmService.sendNotification(
                            notification.getRecipientToken(),
                            notification.getTitle(),
                            notification.getContent(),
                            data);

                    notification.setFirebaseMessageId(messageId);
                    logger.info("Notification PUSH envoyée avec succès: {}", messageId);
                    break;

                case IN_APP:
                    // Les notifications in-app sont simplement stockées et récupérées par l'application
                    logger.info("Notification IN_APP créée: {}", notification.getId());
                    break;

                case SMS:
                    // Mock pour le moment - à implémenter avec un service SMS réel
                    logger.info("Simulation d'envoi SMS à {}: {}", notification.getUserId(), notification.getContent());
                    break;

                case EMAIL:
                    // Mock pour le moment - à implémenter avec un service Email réel
                    logger.info("Simulation d'envoi Email à {}: {}", notification.getUserId(), notification.getTitle());
                    break;

                default:
                    throw new ApiException(HttpStatus.BAD_REQUEST, "Canal de notification non supporté: " + notification.getChannel());
            }

            // Mettre à jour le statut de la notification
            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            return notificationRepository.save(notification);

        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de la notification: {}", e.getMessage(), e);
            notification.setStatus(NotificationStatus.FAILED);
            notification.setSentAt(LocalDateTime.now());
            return notificationRepository.save(notification);
        }
    }

    @Override
    @Transactional
    public Notification sendNotificationToTopic(Notification notification) {
        try {
            // Vérifier si un topic est spécifié dans le payload
            String topic = extractTopicFromPayload(notification.getPayload());
            if (topic.isEmpty()) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Topic non spécifié pour l'envoi à un topic");
            }

            // Enregistrer la notification avec le statut PENDING
            notification.setStatus(NotificationStatus.PENDING);
            notification = notificationRepository.save(notification);

            // Pour les notifications PUSH, envoyer via FCM
            if (notification.getChannel() == NotificationChannel.PUSH) {
                Map<String, String> data = new HashMap<>();
                data.put("notificationId", notification.getId().toString());
                data.put("type", notification.getType().name());

                String messageId = fcmService.sendNotificationToTopic(
                        topic,
                        notification.getTitle(),
                        notification.getContent(),
                        data
                );

                notification.setFirebaseMessageId(messageId);
                logger.info("Notification envoyée au topic {} avec succès: {}", topic, messageId);
            } else {
                logger.info("Notification de type {} créée pour le topic {}", notification.getChannel(), topic);
            }

            // Mettre à jour le statut
            notification.setStatus(NotificationStatus.SENT);
            notification.setSentAt(LocalDateTime.now());
            return notificationRepository.save(notification);

        } catch (Exception e) {
            logger.error("Erreur lors de l'envoi de la notification au topic: {}", e.getMessage(), e);
            notification.setStatus(NotificationStatus.FAILED);
            notification.setSentAt(LocalDateTime.now());
            return notificationRepository.save(notification);
        }
    }

    @Override
    public List<Notification> getNotificationsByUserId(String userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Override
    public List<Notification> getNotificationsByUserIdAndStatus(String userId, NotificationStatus status) {
        return notificationRepository.findByUserIdAndStatus(userId, status);
    }

    @Override
    public List<Notification> getNotificationsByUserIdAndType(String userId, NotificationType type) {
        // Cette méthode n'existe pas encore dans le repository, nous filtrons manuellement
        List<Notification> allUserNotifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
        return allUserNotifications.stream()
                .filter(n -> n.getType() == type)
                .toList();
    }

    @Override
    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<Notification> markNotificationAsRead(Long id) {
        Optional<Notification> notificationOpt = notificationRepository.findById(id);

        if (notificationOpt.isPresent()) {
            Notification notification = notificationOpt.get();
            notification.setStatus(NotificationStatus.READ);
            notification.setReadAt(LocalDateTime.now());
            return Optional.of(notificationRepository.save(notification));
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public int markAllNotificationsAsRead(String userId) {
        List<Notification> unreadNotifications = notificationRepository.findUnreadByUserId(userId);

        if (unreadNotifications.isEmpty()) {
            return 0;
        }

        LocalDateTime now = LocalDateTime.now();
        unreadNotifications.forEach(notification -> {
            notification.setStatus(NotificationStatus.READ);
            notification.setReadAt(now);
        });

        notificationRepository.saveAll(unreadNotifications);
        return unreadNotifications.size();
    }

    @Override
    @Transactional
    public boolean deleteNotification(Long id) {
        if (notificationRepository.existsById(id)) {
            notificationRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Vérifie si une notification doit être envoyée selon les préférences utilisateur
     */
    private boolean shouldSendNotification(Notification notification) {
        // Récupérer les préférences de l'utilisateur
        UserPreference preferences = userPreferenceRepository.getOrCreateUserPreference(notification.getUserId());

        // Vérifier si le canal est activé
        boolean channelEnabled = false;
        switch (notification.getChannel()) {
            case PUSH:
                channelEnabled = preferences.isPushEnabled();
                break;
            case IN_APP:
                channelEnabled = preferences.isInAppEnabled();
                break;
            case SMS:
                channelEnabled = preferences.isSmsEnabled();
                break;
            case EMAIL:
                channelEnabled = preferences.isEmailEnabled();
                break;
        }

        // Vérifier si le type est activé
        boolean typeEnabled = false;
        switch (notification.getType()) {
            case PAYMENT_REMINDER:
                typeEnabled = preferences.isPaymentRemindersEnabled();
                break;
            case CRC_ADVISOR_AVAILABLE:
            case CRC_ADVISOR_UNAVAILABLE:
            case CRC_VERIFICATION_COMPLETED:
                typeEnabled = preferences.isCrcNotificationsEnabled();
                break;
            case GENERAL:
                typeEnabled = true; // Les notifications générales sont toujours activées
                break;
        }

        return channelEnabled && typeEnabled;
    }

    /**
     * Extrait le topic du payload JSON
     */
    private String extractTopicFromPayload(String payload) {
        // Utiliser Jackson pour extraire le topic du JSON
        if (payload == null || payload.isEmpty()) {
            return "";
        }

        try {
            Map<String, Object> payloadMap = objectMapper.readValue(payload,
                    objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class));

            Object topic = payloadMap.get("topic");
            return topic != null ? topic.toString() : "";
        } catch (Exception e) {
            logger.error("Erreur lors de l'extraction du topic du payload: {}", e.getMessage());
            return "";
        }
    }
}