package com.chaabipay.notificationservice2.repository;

import com.chaabipay.notificationservice2.model.Notification;
import com.chaabipay.notificationservice2.model.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Trouver toutes les notifications d'un utilisateur, triées par date de création (plus récentes d'abord)
    List<Notification> findByUserIdOrderByCreatedAtDesc(String userId);

    // Trouver les notifications d'un utilisateur avec un statut spécifique
    List<Notification> findByUserIdAndStatus(String userId, NotificationStatus status);

    // Trouver les notifications non lues d'un utilisateur
    default List<Notification> findUnreadByUserId(String userId) {
        return findByUserIdAndStatus(userId, NotificationStatus.DELIVERED);

    }


}
