# 📚 Système de Gestion de Bibliothèque (Approche TDD)

Ce projet est une application Java de gestion de bibliothèque développée en suivant la méthodologie **TDD (Test Driven Development)**. Elle gère le cycle de vie des livres, des abonnés et des emprunts avec une application stricte des règles métier et de l'intégrité des données.

![Java](https://img.shields.io/badge/Java-17%2B-orange)
![JUnit 5](https://img.shields.io/badge/Test-JUnit%205-green)

## 🚀 Fonctionnalités Clés

### 📖 Gestion des Livres
* **Suivi du stock** : Distinction entre *Stock Total* (inventaire) et *Stock Disponible* (en rayon).
* **Intégrité de suppression** : Impossible de supprimer un livre s'il reste des exemplaires empruntés.
* **Gestion des doublons** : Unicité basée sur le couple Titre + Auteur.

### 👤 Gestion des Utilisateurs
* **Emprunt et retour** : Mécanismes sécurisés via la classe centrale `Bibliotheque`.
* **Limites d'emprunt** : Respect strict du quota imposé par l'abonnement.
* **Calcul de pénalités** : Détection automatique des retards par rapport à la durée de l'abonnement.
* **Cascade de suppression** : Supprimer un utilisateur entraîne le retour automatique de tous ses livres dans le stock.
* **Sécurité** : Validation des données (pas de coût négatif, email unique).

### 💳 Gestion des Abonnements
* **Configuration flexible** : Prix, Nombre max d'emprunts, Durée d'emprunt.
* **Protection** : Impossible de supprimer un abonnement s'il est utilisé par un membre actif.
* **Validation** : Interdiction absolue des valeurs négatives (Prix, Durée, Max).

### 🏛️ Bibliothèque (Contrôleur Central)
* **Centralisation** : Gestionnaire unique des listes (Livres, Utilisateurs, Abonnements).
* **Sécurité des emprunts** : Un utilisateur ne peut emprunter qu'un livre référencé dans la bibliothèque.
* **Intégrité référentielle** : Un utilisateur ne peut être créé qu'avec un abonnement existant dans la bibliothèque.

---

## 🛠️ Architecture Technique

Le projet est divisé en deux packages principaux :

* `com.biblio` : Contient les classes métier (`Livre`, `Utilisateur`, `Abonnement`, `Bibliotheque`, `Emprunt`).
* `com.test` : Contient la suite de tests unitaires JUnit 5.
