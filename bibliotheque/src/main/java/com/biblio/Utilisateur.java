package com.biblio;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Utilisateur {
    // ... (Champs existants : prenom, nom, etc.)
    private String prenom;
    private String nom;
    private String email;
    private String dateNaissance;
    private int cout;
    private Abonnement abonnement;
    private boolean supprime = false; // Ajout du flag

    private List<Emprunt> emprunts = new ArrayList<>();

    public Utilisateur(String prenom, String nom, String email, String dateNaissance, int cout, Abonnement abo) {
        // (Attention: Le test 'Doublon' ne peut pas fonctionner ici sans base de données ou liste statique)
        this.prenom = prenom;
        this.nom = nom;
        this.email = email;
        this.dateNaissance = dateNaissance;
        this.cout = cout;
        this.abonnement = abo;
    }

    // ... Getters existants ...
    public String getPrenom() { return prenom; }
    public String getNom() { return nom; }

    public String getEmail() { return email; }
    public String getDateNaissance() { return dateNaissance; }
    public int getCout() { return cout; }
    public Abonnement getAbonnement() { return abonnement; }
    public List<Emprunt> getEmprunts() { return emprunts; }
    public boolean isSupprime() { return supprime; }

    public void modifierUtilisateur(String prenom, String nom) {
        this.prenom = prenom;
        this.nom = nom;
    }

    public void supprimerUtilisateur() {
        // Règle : Supprimer l'utilisateur rend ses livres
        // On crée une copie de la liste pour éviter les erreurs de concurrence lors de la modification
        List<Emprunt> empruntsActuels = new ArrayList<>(this.emprunts);

        for (Emprunt e : empruntsActuels) {
            e.getLivre().retourner(); // Le stock du livre remonte
        }
        this.emprunts.clear(); // On vide la liste d'emprunts
        this.supprime = true;
    }

    public boolean emprunterLivre(Livre livre) {
        if (livre == null) return false;
        if (supprime) return false;
        if (emprunts.size() >= abonnement.getMaxEmprunts()) return false;

        // 2. Vérification si l'utilisateur a DÉJÀ ce livre
        for (Emprunt e : emprunts) {
            if (e.getLivre().equals(livre)) {
                return false; // Refus car déjà emprunté
            }
        }

        if (!livre.emprunter()) return false;
        emprunts.add(new Emprunt(livre));
        return true;
    }

    public double calculerPenalite() {
        double total = 0;
        for (Emprunt e : emprunts) {
            long jours = ChronoUnit.DAYS.between(e.getDateEmprunt(), LocalDate.now());
            long retard = jours - abonnement.getDureeEmprunts();
            if (retard > 0) total += retard * 0.5;
        }
        return total;
    }

    public boolean rendreLivre(Livre livre) {
        // On cherche l'emprunt correspondant à ce livre
        Emprunt empruntATrouver = null;
        for (Emprunt e : emprunts) {
            if (e.getLivre().equals(livre)) {
                empruntATrouver = e;
                break;
            }
        }

        if (empruntATrouver != null) {
            livre.retourner(); // Remet le stock
            emprunts.remove(empruntATrouver); // Enlève de la liste de l'user
            return true;
        }
        return false; // Livre non trouvé dans les emprunts
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Utilisateur that = (Utilisateur) o;
        // On considère que l'email est l'identifiant unique
        return email.equals(that.email);
    }

    @Override
    public String toString() {
        return prenom + " " + nom + " (" + email + ") - Abo: " + abonnement.getNom();
    }


}