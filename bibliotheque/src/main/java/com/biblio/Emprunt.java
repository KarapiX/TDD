package com.biblio;

import java.time.LocalDate;

public class Emprunt {
    private Livre livre;
    private LocalDate dateEmprunt;

    public Emprunt(Livre livre) {
        this.livre = livre;
        this.dateEmprunt = LocalDate.now();
    }

    public Livre getLivre() { return livre; }
    public LocalDate getDateEmprunt() { return dateEmprunt; }

    public void setDateEmprunt(LocalDate d) {
        this.dateEmprunt = d;
    }
}
