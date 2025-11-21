package com.biblio;

public class Abonnement {
    private String nom;
    private double prix;
    private int maxEmprunts;
    private int dureeEmprunts;
    private boolean supprime = false;

    public Abonnement(String nom, double prix, int maxEmprunts, int dureeEmprunts) {
        // 1. Vérif Prix
        if (prix < 0) {
            throw new IllegalArgumentException("Le prix de l'abonnement ne peut pas être négatif.");
        }

        // 2. Vérif Max Emprunts
        if (maxEmprunts < 0) {
            throw new IllegalArgumentException("Le nombre maximum d'emprunts ne peut pas être négatif.");
        }

        // 3. Vérif Durée
        if (dureeEmprunts < 0) {
            throw new IllegalArgumentException("La durée d'emprunt ne peut pas être négative.");
        }
        this.nom = nom;
        this.prix = prix;
        this.maxEmprunts = maxEmprunts;
        this.dureeEmprunts = dureeEmprunts;
    }

    // ... Getters ...
    public String getNom() { return nom; }
    public double getPrix() { return prix; }
    public int getMaxEmprunts() { return maxEmprunts; }
    public int getDureeEmprunts() { return dureeEmprunts; }
    public boolean isSupprime() { return supprime; }

    public void modifierAbonnement(String nom, double prix, int max, int duree) {
        // 1. Vérif Prix
        if (prix < 0) {
            throw new IllegalArgumentException("Le prix de l'abonnement ne peut pas être négatif.");
        }

        // 2. Vérif Max Emprunts
        if (max < 0) {
            throw new IllegalArgumentException("Le nombre maximum d'emprunts ne peut pas être négatif.");
        }

        // 3. Vérif Durée
        if (duree < 0) {
            throw new IllegalArgumentException("La durée d'emprunt ne peut pas être négative.");
        }
        this.nom = nom;
        this.prix = prix;
        this.maxEmprunts = max;
        this.dureeEmprunts = duree;
    }

    public void supprimerAbonnement() {
        this.supprime = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Abonnement)) return false;
        Abonnement that = (Abonnement) o;
        return nom.equals(that.nom);
    }

    @Override
    public String toString() {
        return "Abonnement " + nom + " (" + prix + "€) - Max: " + maxEmprunts;
    }
}