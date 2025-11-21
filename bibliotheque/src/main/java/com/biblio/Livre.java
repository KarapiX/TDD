package com.biblio;

public class Livre {

    private String titre;
    private String auteur;
    private int stockTotal;      // Ajout: Quantité totale possédée par la biblio
    private int stockDispo;      // Renommé de 'exemplaires' pour plus de clarté
    private boolean supprime = false;

    public Livre(String titre, String auteur, int stockTotal) {
        if (stockTotal < 0) {
            throw new IllegalArgumentException("Le stock ne peut pas être négatif");
        }
        this.titre = titre;
        this.auteur = auteur;
        this.stockTotal = stockTotal;
        this.stockDispo = stockTotal;
    }

    public String getTitre() { return titre; }
    public String getAuteur() { return auteur; }
    public int getExemplairesDispo() { return stockDispo; }
    public int getStockTotal() { return stockTotal; } // Getter utile pour les tests
    public boolean isSupprime() { return supprime; }

    public void modifierLivre(String titre, String auteur, int stockTotal) {
        this.titre = titre;
        this.auteur = auteur;
        this.stockTotal = stockTotal;
        this.stockDispo = stockTotal;
    }

    public boolean supprimerLivre() {
        // Règle : On ne supprime que si aucun livre n'est dehors
        if (stockDispo < stockTotal) {
            return false; // Suppression impossible car emprunté
        }
        this.supprime = true;
        return true;
    }

    public boolean emprunter() {
        if (!supprime && stockDispo > 0) {
            stockDispo--;
            return true;
        }
        return false;
    }

    public void retourner() {
        if (stockDispo < stockTotal) {
            stockDispo++;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Livre)) return false;
        Livre livre = (Livre) o;
        return titre.equals(livre.titre) && auteur.equals(livre.auteur);
    }

    @Override
    public String toString() {
        return "Livre : " + titre + " - " + auteur + " (" + stockDispo + "/" + stockTotal + ")";
    }
}