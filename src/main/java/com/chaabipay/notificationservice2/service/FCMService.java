package com.chaabipay.notificationservice2.service;

import com.chaabipay.notificationservice2.model.Notification;

import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface FCMService {

    /**
     * Envoie une notification à un appareil spécifique
     * @param token Le token FCM de l'appareil
     * @param title Le titre de la notification
     * @param body Le corps de la notification
     * @param data Données supplémentaires (optionnel)
     * @return L'identifiant de la notification envoyée
     */
    String sendNotification(String token, String title, String body, Map<String, String> data)
            throws ExecutionException, InterruptedException;

    /**
     * Envoie une notification à un topic
     * @param topic Le nom du topic
     * @param title Le titre de la notification
     * @param body Le corps de la notification
     * @param data Données supplémentaires (optionnel)
     * @return L'identifiant de la notification envoyée
     */
    String sendNotificationToTopic(String topic, String title, String body, Map<String, String> data)
            throws ExecutionException, InterruptedException;

    /**
     * Envoie une notification basée sur l'objet Notification
     * @param notification L'objet Notification contenant les détails
     * @return L'identifiant de la notification envoyée
     */
    String sendNotification(Notification notification)
            throws ExecutionException, InterruptedException;

    /**
     * Abonne un token à un topic
     * @param token Le token FCM à abonner
     * @param topic Le nom du topic
     */
    void subscribeToTopic(String token, String topic);

    /**
     * Désabonne un token d'un topic
     * @param token Le token FCM à désabonner
     * @param topic Le nom du topic
     */
    void unsubscribeFromTopic(String token, String topic);
}