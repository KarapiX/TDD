package com.biblio;

public class Abonnement {
    private String nom;
    private double prix;
    private int maxEmprunts;
    private int dureeEmprunts;
    private boolean supprime = false;

    public Abonnement(String nom, double prix, int maxEmprunts, int dureeEmprunts) {
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