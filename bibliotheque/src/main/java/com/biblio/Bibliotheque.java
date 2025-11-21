package com.biblio;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Bibliotheque {

    private List<Utilisateur> utilisateurs = new ArrayList<>();
    private List<Livre> livres = new ArrayList<>();
    private List<Abonnement> abonnements = new ArrayList<>();

    // --- Gestion des Utilisateurs ---

    public void ajouterUtilisateur(Utilisateur u) {
        if (utilisateurs.contains(u)) {
            throw new IllegalArgumentException("Cet utilisateur existe déjà.");
        }

        // NOUVELLE VÉRIFICATION : L'abonnement doit exister dans la bibliothèque
        if (!abonnements.contains(u.getAbonnement())) {
            throw new IllegalArgumentException("L'abonnement de l'utilisateur n'est pas valide ou inconnu de la bibliothèque.");
        }

        utilisateurs.add(u);
    }

    public void supprimerUtilisateur(Utilisateur u) {
        if (utilisateurs.contains(u)) {
            // On déclenche la logique de retour des livres définie dans la classe Utilisateur
            u.supprimerUtilisateur();
            utilisateurs.remove(u);
        }
    }

    public List<Utilisateur> getUtilisateurs() { return utilisateurs; }
    public List<Livre> getLivres() { return livres; }
    public List<Abonnement> getAbonnements() { return abonnements; }


    // Méthode d'affichage
    public String listerAbonnements() {
        if (abonnements.isEmpty()) return "Aucun abonnement disponible.";
        // Utilise le toString() défini plus haut pour chaque élément
        return abonnements.stream()
                .map(Abonnement::toString)
                .collect(Collectors.joining("\n"));
    }

    public String listerLivres() {
        if (livres.isEmpty()) return "Bibliothèque vide.";
        return livres.stream()
                .map(Livre::toString)
                .collect(Collectors.joining("\n"));
    }


    public String listerUtilisateurs() {
        if (utilisateurs.isEmpty()) return "Aucun utilisateur inscrit.";

        return utilisateurs.stream()
                .map(Utilisateur::toString)
                .collect(Collectors.joining("\n"));
    }
    // --- Gestion des Livres ---

    public void ajouterLivre(Livre l) {
        if (livres.contains(l)) {
            throw new IllegalArgumentException("Ce livre existe déjà dans la bibliothèque.");
        }
        livres.add(l);
    }

    public boolean supprimerLivre(Livre l) {
        // On utilise la méthode du Livre qui vérifie s'il est emprunté (stockDispo vs StockTotal)
        if (l.supprimerLivre()) {
            livres.remove(l);
            return true;
        }
        return false; // Suppression refusée car des exemplaires sont dehors
    }

    // --- Gestion des Abonnements ---

    public void ajouterAbonnement(Abonnement a) {
        if (abonnements.contains(a)) {
            throw new IllegalArgumentException("Cet abonnement existe déjà.");
        }
        abonnements.add(a);
    }

    public boolean supprimerAbonnement(Abonnement a) {
        // Règle : Impossible de supprimer un abonnement s'il est utilisé par un utilisateur actif
        boolean estUtilise = utilisateurs.stream()
                .anyMatch(u -> u.getAbonnement().equals(a));

        if (estUtilise) {
            return false; // Refus de suppression
        }

        a.supprimerAbonnement();
        abonnements.remove(a);
        return true;
    }

    // --- GESTION DES EMPRUNTS ---

    /**
     * Orchestre l'emprunt d'un livre par un utilisateur.
     * Vérifie que le livre appartient bien à la bibliothèque.
     */
    public boolean emprunterLivre(Utilisateur u, Livre l) {
        // 1. Vérification de sécurité : Le livre existe-t-il dans NOTRE catalogue ?
        if (!livres.contains(l)) {
            return false; // Refus : Ce livre ne vient pas d'ici
        }

        // 2. (Optionnel) Vérification : L'utilisateur est-il inscrit ?
        if (!utilisateurs.contains(u)) {
            return false;
        }

        // 3. On délègue la logique métier à l'utilisateur (quotas, stock, etc.)
        return u.emprunterLivre(l);
    }


}