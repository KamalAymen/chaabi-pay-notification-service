# Chaabi Pay Notification Service

Microservice de notification autonome conçu pour gérer l'envoi et le suivi des notifications aux utilisateurs de l'écosystème Chaabi Pay. Ce service est capable d'envoyer des notifications via plusieurs canaux (push, in-app, SMS, email) et est optimisé pour fonctionner avec des applications mobiles sur iOS et Android.

## Fonctionnalités

- Envoi de notifications via 4 canaux différents:
    - Push notifications (Firebase Cloud Messaging)
    - Notifications in-app
    - SMS
    - Email
- Gestion des préférences utilisateur
- Système de rappels automatiques pour les paiements de factures (J-3, J+1, J+5)
- API REST complète pour la gestion des notifications
- Intégration avec le service d'appel vidéo CRC

## Prérequis

- Java 17 ou supérieur
- Maven
- PostgreSQL
- Compte Firebase pour les notifications push

## Installation

1. Cloner le dépôt
```
git clone https://github.com/votre-username/chaabi-pay-notification-service.git
```

2. Configurer la base de données PostgreSQL dans `application.properties`

3. Ajouter votre fichier de configuration Firebase `firebase-service-account.json` dans `src/main/resources/`

4. Compiler le projet
```
mvn clean install
```

5. Démarrer l'application
```
mvn spring-boot:run
```

## API Documentation

### Notifications API

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | /api/notifications | Crée et envoie une nouvelle notification |
| POST | /api/notifications/topic | Envoie une notification à un topic |
| GET | /api/notifications/user/{userId} | Récupère les notifications d'un utilisateur |
| GET | /api/notifications/{id} | Récupère une notification par son ID |
| PUT | /api/notifications/{id}/read | Marque une notification comme lue |
| PUT | /api/notifications/user/{userId}/read-all | Marque toutes les notifications d'un utilisateur comme lues |
| DELETE | /api/notifications/{id} | Supprime une notification |

### Préférences API

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | /api/preferences/{userId} | Récupère les préférences d'un utilisateur |
| POST | /api/preferences | Crée ou met à jour des préférences |
| PUT | /api/preferences/{userId} | Met à jour les préférences d'un utilisateur |
| PUT | /api/preferences/{userId}/channel/{channel} | Active/désactive un canal spécifique |
| PUT | /api/preferences/{userId}/type/{type} | Active/désactive un type spécifique |
| DELETE | /api/preferences/{userId} | Supprime les préférences d'un utilisateur |

## Structure du projet

```
notification-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── chaabipay/
│   │   │           └── notificationservice2/
│   │   │               ├── config/ - Configuration de l'application
│   │   │               ├── controller/ - Contrôleurs REST
│   │   │               ├── dto/ - Objets de transfert de données
│   │   │               ├── exception/ - Gestion des exceptions
│   │   │               ├── mapper/ - Conversion entre DTOs et entités
│   │   │               ├── model/ - Modèles/entités
│   │   │               ├── repository/ - Accès aux données
│   │   │               ├── service/ - Logique métier
│   │   │               └── NotificationService2Application.java - Point d'entrée
│   │   └── resources/ - Fichiers de configuration
```

## Contribution

Les contributions sont les bienvenues. Veuillez créer une issue ou soumettre une pull request.

## Licence

[Licence en cours de décision]