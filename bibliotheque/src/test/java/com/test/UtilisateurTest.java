package com.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.biblio.Abonnement;
import com.biblio.Emprunt;
import com.biblio.Livre;
import com.biblio.Utilisateur;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class UtilisateurTest {

    Abonnement abo;
    Utilisateur user;

    @BeforeEach
    void setup() {
        abo = new Abonnement("Standard", 10.0, 3, 14);
        user = new Utilisateur("Alice", "Nom", "alice.nom@gmail.com", "23/07/2003", 0, abo);
    }

    // --- Tests nominaux existants conservés ---

    @Test
    void testEmprunterLivreSucces() {
        Livre livre = new Livre("Dune", "Herbert", 1);
        assertTrue(user.emprunterLivre(livre));
        assertEquals(0, livre.getExemplairesDispo());
        assertEquals(1, user.getEmprunts().size());
    }

    @Test
    void testSuppressionUtilisateurRendLesLivres() {
        // Mise en place : Alice emprunte un livre
        Livre livre = new Livre("Harry Potter", "JKR", 5);
        user.emprunterLivre(livre);
        assertEquals(4, livre.getExemplairesDispo(), "Stock doit être à 4 après emprunt");

        // Action : Supprimer l'utilisateur
        user.supprimerUtilisateur();

        // Vérifications
        assertTrue(user.isSupprime());
        assertEquals(0, user.getEmprunts().size(), "La liste d'emprunts de l'utilisateur doit être vide");
        assertEquals(5, livre.getExemplairesDispo(), "Le stock du livre doit être revenu à 5");
    }

    @Test
    void testUtilisateurSupprimeNePeutPlusEmprunter() {
        user.supprimerUtilisateur();
        Livre livre = new Livre("LOTR", "Tolkien", 5);

        assertFalse(user.emprunterLivre(livre));
        assertEquals(5, livre.getExemplairesDispo());
    }

    @Test
    void testLimiteEmpruntSelonAbonnement() {
        Abonnement aboPetit = new Abonnement("Petit", 5.0, 1, 7);
        Utilisateur userPetit = new Utilisateur("Bob", "D", "bob@mail.com", "01/01/2000", 0, aboPetit);

        Livre l1 = new Livre("A", "A", 10);
        Livre l2 = new Livre("B", "B", 10);

        assertTrue(userPetit.emprunterLivre(l1));
        assertFalse(userPetit.emprunterLivre(l2), "Devrait refuser car limite de 1 atteinte");
    }

    @Test
    void testCalculPenalite() {
        // Emprunt avec date modifiée pour simuler un retard
        Livre livre = new Livre("Dune", "Herbert", 1);
        user.emprunterLivre(livre);

        Emprunt emprunt = user.getEmprunts().get(0);
        // Retard de 10 jours : (14 jours autorisés, donc emprunté il y a 24 jours)
        emprunt.setDateEmprunt(LocalDate.now().minusDays(24));

        double penalite = user.calculerPenalite();
        // 10 jours de retard * 0.5 = 5.0
        assertEquals(5.0, penalite, 0.01);
    }

    @Test
    void testPasDePenaliteSiDansLesTemps() {
        Livre livre = new Livre("Dune", "Herbert", 1);
        user.emprunterLivre(livre);
        assertEquals(0.0, user.calculerPenalite());
    }

    @Test
    void testRetournerUnSeulLivre() {
        Livre livre = new Livre("Dune", "Herbert", 5);
        user.emprunterLivre(livre);

        // Vérification avant retour
        assertEquals(4, livre.getExemplairesDispo());
        assertEquals(1, user.getEmprunts().size());

        // Action : L'utilisateur rend le livre (Méthode à créer !)
        boolean retourEffectue = user.rendreLivre(livre);

        // Vérifications après retour
        assertTrue(retourEffectue);
        assertEquals(5, livre.getExemplairesDispo(), "Le stock doit remonter");
        assertEquals(0, user.getEmprunts().size(), "La liste d'emprunt doit être vide");
    }

    @Test
    void testEmprunterNull() {
        // Vérifier que le système ne crash pas si on passe null
        assertFalse(user.emprunterLivre(null));
        // Ou assertThrows(NullPointerException.class...) selon ta politique
    }
    @Test
    void testEmprunterMemeLivreDeuxFois() {
        Livre livre = new Livre("Dune", "Herbert", 10);

        assertTrue(user.emprunterLivre(livre), "1er emprunt OK");

        // Si la règle est "1 exemplaire par titre par personne" :
        assertFalse(user.emprunterLivre(livre), "2ème emprunt du même livre devrait être bloqué");
    }
    @Test
    void testLimiteEmpruntExacte() {
        // Abo permet 3 emprunts
        Livre l1 = new Livre("A", "A", 5);
        Livre l2 = new Livre("B", "B", 5);
        Livre l3 = new Livre("C", "C", 5);
        Livre l4 = new Livre("D", "D", 5);

        assertTrue(user.emprunterLivre(l1)); // 1/3
        assertTrue(user.emprunterLivre(l2)); // 2/3
        assertTrue(user.emprunterLivre(l3)); // 3/3 -> LIMITE ATTEINTE

        assertFalse(user.emprunterLivre(l4)); // 4/3 -> REFUS
    }

    @Test
    void testCreationUtilisateur() {
        // Teste getPrenom, getNom et modifierUtilisateur
        Utilisateur u = new Utilisateur("Jean", "Paul", "jean@mail.com", "01/01/2000", 0, abo);

        assertEquals("Jean", u.getPrenom());
        assertEquals("Paul", u.getNom());
        assertEquals("jean@mail.com", u.getEmail());
        assertEquals("01/01/2000", u.getDateNaissance());
        assertEquals(0, u.getCout());
        assertEquals(abo, u.getAbonnement());


    }
    @Test
    void testModificationtilisateur() {
        // Test getPrenom, getNom et modifierUtilisateur
        Utilisateur u = new Utilisateur("Jean", "Paul", "jean@mail.com", "01/01/2000", 0, abo);

        u.modifierUtilisateur("Jacques", "Chirac");

        assertEquals("Jacques", u.getPrenom());
        assertEquals("Chirac", u.getNom());


    }

    @Test
    void testCreationAbonnement() {
        Abonnement a = new Abonnement("Base", 5.0, 1, 7);


        assertEquals("Base", a.getNom());
        assertEquals(5.0, a.getPrix());
        assertEquals(1, a.getMaxEmprunts());
        assertEquals(7, a.getDureeEmprunts());
    }

    @Test
    void testModificationAbonnement() {
        Abonnement a = new Abonnement("Base", 5.0, 1, 7);

        assertEquals("Base", a.getNom());
        assertEquals(5.0, a.getPrix());

        a.modifierAbonnement("Super", 15.0, 10, 30);

        assertEquals("Super", a.getNom());
        assertEquals(15.0, a.getPrix());
        assertEquals(10, a.getMaxEmprunts());
        assertEquals(30, a.getDureeEmprunts());
    }

    @Test
    void testEtatSuppressionAbonnement() {
        // 1. Setup
        Abonnement abo = new Abonnement("Test", 10.0, 5, 30);

        // 2. Vérification de l'état initial (doit être false par défaut)
        assertFalse(abo.isSupprime(), "Un nouvel abonnement ne doit pas être marqué comme supprimé");

        // 3. Action : On supprime
        abo.supprimerAbonnement();

        // 4. Vérification après action (doit être true)
        assertTrue(abo.isSupprime(), "La méthode isSupprime doit retourner true après suppression");
    }
}