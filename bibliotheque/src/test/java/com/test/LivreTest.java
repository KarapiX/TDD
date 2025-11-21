package com.test;

import org.junit.jupiter.api.Test;
import com.biblio.Livre;
import static org.junit.jupiter.api.Assertions.*;

public class LivreTest {

    @Test
    void testCreationLivre() {
        Livre livre = new Livre("1984", "George Orwell", 3);
        assertEquals("1984", livre.getTitre());
        assertEquals(3, livre.getExemplairesDispo());
        assertEquals(3, livre.getStockTotal());
        assertEquals("George Orwell", livre.getAuteur());
        assertFalse(livre.isSupprime());
    }

    @Test
    void testEmpruntReduitStock() {
        Livre livre = new Livre("Dune", "Herbert", 2);
        assertTrue(livre.emprunter());
        assertEquals(1, livre.getExemplairesDispo());
        assertEquals(2, livre.getStockTotal()); // Le total ne bouge pas
    }

    @Test
    void testRetourAugmenteStock() {
        Livre livre = new Livre("Dune", "Herbert", 2);
        livre.emprunter();
        livre.retourner();
        assertEquals(2, livre.getExemplairesDispo());
    }

    @Test
    void testRetourImpossibleSiStockPlein() {
        Livre livre = new Livre("Dune", "Herbert", 2);
        livre.retourner(); // Rien à retourner
        assertEquals(2, livre.getExemplairesDispo()); // Ne doit pas passer à 3
    }

    @Test
    void testSuppressionLivrePossibleSiAucunEmprunt() {
        Livre livre = new Livre("Dune", "Herbert", 2);
        boolean resultat = livre.supprimerLivre();

        assertTrue(resultat, "La suppression devrait être autorisée");
        assertTrue(livre.isSupprime());
    }

    @Test
    void testSuppressionLivreImpossibleSiEmprunte() {
        Livre livre = new Livre("Dune", "Herbert", 2);
        livre.emprunter(); // Il en reste 1, mais 1 est dehors

        boolean resultat = livre.supprimerLivre();

        assertFalse(resultat, "La suppression doit être refusée car un livre est emprunté");
        assertFalse(livre.isSupprime());
    }

    @Test
    void testCreationLivreInvalide() {
        // On ne devrait pas pouvoir créer un livre avec -5 exemplaires
        assertThrows(IllegalArgumentException.class, () -> {
            new Livre("Titre", "Auteur", -5);
        });
    }

    @Test
    void testModificationLivre() {
        // Teste getAuteur, modifierLivre et l'impact sur le stock
        Livre l = new Livre("Titre", "AuteurBase", 5);

        assertEquals("AuteurBase", l.getAuteur());

        // On modifie : le stock dispo est reset au stock total selon ta logique actuelle
        l.modifierLivre("NouveauTitre", "NouvelAuteur", 10);

        assertEquals("NouvelAuteur", l.getAuteur());
        assertEquals("NouveauTitre", l.getTitre());
        assertEquals(10, l.getStockTotal());
        assertEquals(10, l.getExemplairesDispo()); // Vérifie le reset du dispo
    }

}