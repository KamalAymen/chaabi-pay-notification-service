package com.chaabipay.notificationservice2.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity // Indique que cette classe est une entité JPA (sera stockée en base de données)
@Data // Annotation Lombok qui génère automatiquement getters, setters, equals, hashCode, etc.
@NoArgsConstructor
@AllArgsConstructor

public class Notification {
    @Id // Clé primaire
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-incrémentation
    private Long id;

    private String userId;      // Identifiant de l'utilisateur destinataire
    private String title;       // Titre de la notification
    private String content;     // Contenu textuel de la notification
    private String imageUrl;    // URL optionnelle d'une image


    @Enumerated(EnumType.STRING) // Stocke l'enum comme une chaîne de caractères
    private NotificationType type; // Type de notification (paiement, CRC, etc.)

    @Enumerated(EnumType.STRING)
    private NotificationChannel channel; // Canal utilisé (push, SMS, etc.)

    @Enumerated(EnumType.STRING)
    private NotificationStatus status; // Statut de la notification

    private LocalDateTime createdAt; // Date de création
    private LocalDateTime sentAt;    // Date d'envoi
    private LocalDateTime readAt;    // Date de lecture
    private String firebaseMessageId;
    private String recipientToken;
   /// ///////
    /// test d un commit
    // Données additionnelles au format JSON
    @Column(columnDefinition = "TEXT") // Pour stocker de grands textes
    private String payload;

    // Initialisation des dates
    @PrePersist // Exécuté avant la première sauvegarde
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
