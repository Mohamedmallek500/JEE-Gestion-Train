# 🚆 Application Web JEE – Gestion d'Achat de Tickets de Train

## 📌 Description

Ce projet est une application web développée en **Java EE** qui permet aux utilisateurs de rechercher, réserver et acheter des **billets de train** en ligne. Il offre également une interface d'administration pour la gestion complète des trajets, utilisateurs, et paiements.

---

## 🔧 Technologies Utilisées

- Java EE (Servlets, JSP, JSTL, EL)
- Hibernate 
- JDBC / MySQL
- MVC + DAO 
- Tomcat 9+
- HTML / CSS / Bootstrap (pour l’interface utilisateur)

---

## 🎯 Fonctionnalités Principales

### 🔍 Recherche de Trajets
- Recherche possible sans authentification.
- Filtres : date, ville de départ, ville d’arrivée.
- Affichage des trajets disponibles avec :
  - Heure de départ / arrivée
  - Durée
  - Prix
  - Disponibilité

### 🎫 Réservation & Options
- Sélection de la **classe** (1ère, 2ème, économique)
- Choix de préférences (fenêtre, non-fumeur, etc.)
- Réservation de trajet retour

### 🔐 Authentification & Paiement
- Création de compte obligatoire avant achat
- Paiement en ligne (simulé)
- Billets avec états : acheté, utilisé, annulé

### 👤 Espace Utilisateur
- Historique de voyages
- Modification ou annulation de billets
- Téléchargement de billets en **PDF**

### 🛠️ Espace Administrateur
- Gestion des :
  - Trajets
  - Gares
  - Voyages
  - Utilisateurs
  - Demandes d'annulation
  - Paiements

---

## 🌟 Fonctionnalités Optionnelles Implémentées

- Trajets avec gares de passage
- Système de promotions et réductions pour familles
- Génération de statistiques (trajets populaires, revenus, etc.)

---

## 🏁 Installation et Exécution

1. Cloner le projet :
   ```bash
   git clone https://github.com/Mohamedmallek500/JEE-Gestion-Train.git
