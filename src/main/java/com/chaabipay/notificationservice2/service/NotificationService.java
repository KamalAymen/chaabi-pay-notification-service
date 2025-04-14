package com.chaabipay.notificationservice2.service;

import com.chaabipay.notificationservice2.model.Notification;
import com.chaabipay.notificationservice2.model.NotificationStatus;
import com.chaabipay.notificationservice2.model.NotificationType;

import java.util.List;
import java.util.Optional;

public interface NotificationService {

    /**
     * Envoie une notification en utilisant le canal spécifié
     * @param notification La notification à envoyer
     * @return La notification mise à jour avec son statut
     */
    Notification sendNotification(Notification notification);

    /**
     * Envoie une notification à un topic
     * @param notification La notification à envoyer au topic
     * @return La notification mise à jour avec son statut
     */
    Notification sendNotificationToTopic(Notification notification);

    /**
     * Récupère toutes les notifications d'un utilisateur
     * @param userId L'identifiant de l'utilisateur
     * @return Une liste de notifications
     */
    List<Notification> getNotificationsByUserId(String userId);

    /**
     * Récupère les notifications d'un utilisateur avec un statut spécifique
     * @param userId L'identifiant de l'utilisateur
     * @param status Le statut des notifications à récupérer
     * @return Une liste de notifications
     */
    List<Notification> getNotificationsByUserIdAndStatus(String userId, NotificationStatus status);

    /**
     * Récupère les notifications d'un utilisateur avec un type spécifique
     * @param userId L'identifiant de l'utilisateur
     * @param type Le type des notifications à récupérer
     * @return Une liste de notifications
     */
    List<Notification> getNotificationsByUserIdAndType(String userId, NotificationType type);

    /**
     * Récupère une notification par son id
     * @param id L'identifiant de la notification
     * @return La notification, si elle existe
     */
    Optional<Notification> getNotificationById(Long id);

    /**
     * Marque une notification comme lue
     * @param id L'identifiant de la notification
     * @return La notification mise à jour, si elle existe
     */
    Optional<Notification> markNotificationAsRead(Long id);

    /**
     * Marque toutes les notifications d'un utilisateur comme lues
     * @param userId L'identifiant de l'utilisateur
     * @return Le nombre de notifications mises à jour
     */
    int markAllNotificationsAsRead(String userId);

    /**
     * Supprime une notification
     * @param id L'identifiant de la notification
     * @return true si la suppression a réussi, false sinon
     */
    boolean deleteNotification(Long id);
}