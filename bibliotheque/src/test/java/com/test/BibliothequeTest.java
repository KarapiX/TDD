package com.test;

import com.biblio.Abonnement;
import com.biblio.Bibliotheque;
import com.biblio.Livre;
import com.biblio.Utilisateur;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BibliothequeTest {

    Bibliotheque biblio;
    Abonnement aboStandard;

    @BeforeEach
    void setup() {
        biblio = new Bibliotheque();
        aboStandard = new Abonnement("Standard", 10, 3, 14);
        biblio.ajouterAbonnement(aboStandard);
    }

    // --- Tests Utilisateurs ---

    @Test
    void testAjoutUtilisateurSucces() {
        Utilisateur u = new Utilisateur("Alice", "Dupont", "alice@mail.com", "01/01/2000", 0, aboStandard);
        biblio.ajouterUtilisateur(u);
        assertEquals(1, biblio.getUtilisateurs().size());
    }

    @Test
    void testAjoutAbonnementSucces() {
        // Rappel : Le setup() a déjà ajouté "aboStandard", donc la liste contient 1 élément
        Abonnement nouvelAbo = new Abonnement("Premium", 20.0, 10, 30);

        biblio.ajouterAbonnement(nouvelAbo);

        // On doit donc en avoir 2 maintenant
        assertEquals(2, biblio.getAbonnements().size());
        assertTrue(biblio.getAbonnements().contains(nouvelAbo));
    }

    @Test
    void testAjoutLivreSucces() {
        // La liste des livres est vide au démarrage du test
        Livre livre = new Livre("Le Petit Prince", "Saint-Exupéry", 5);

        biblio.ajouterLivre(livre);

        assertEquals(1, biblio.getLivres().size());
        // On vérifie aussi que c'est bien CE livre qui est dedans
        assertEquals("Le Petit Prince", biblio.getLivres().get(0).getTitre());
    }

    @Test
    void testAffichageLivres() {
        Livre l1 = new Livre("Dune", "Herbert", 5);
        Livre l2 = new Livre("1984", "Orwell", 3);

        biblio.ajouterLivre(l1);
        biblio.ajouterLivre(l2);

        String affichage = biblio.listerLivres();

        // On vérifie que le texte contient bien les titres
        assertTrue(affichage.contains("Dune"));
        assertTrue(affichage.contains("1984"));
        assertTrue(affichage.contains("5/5")); // Vérifie l'affichage du stock
    }

    @Test
    void testAffichageAbonnements() {
        String affichage = biblio.listerAbonnements();

        assertTrue(affichage.contains("Standard"));
        assertTrue(affichage.contains("10.0€"));
    }

    @Test
    void testAffichageUtilisateurs() {
        // 1. Création et ajout d'un utilisateur
        Utilisateur u = new Utilisateur("Jean", "Valjean", "jean@prison.fr", "01/01/1800", 0, aboStandard);
        biblio.ajouterUtilisateur(u);

        // 2. Récupération de l'affichage
        String affichage = biblio.listerUtilisateurs();

        // 3. Vérifications
        assertTrue(affichage.contains("Jean"));
        assertTrue(affichage.contains("Valjean"));
        assertTrue(affichage.contains("jean@prison.fr"));
        assertTrue(affichage.contains("Standard")); // On vérifie qu'on voit bien son abonnement
    }
    @Test
    void testAjoutUtilisateurDoublon() {
        Utilisateur u1 = new Utilisateur("Alice", "Dupont", "alice@mail.com", "01/01/2000", 0, aboStandard);
        Utilisateur u2 = new Utilisateur("Alice", "Bis", "alice@mail.com", "01/01/2000", 0, aboStandard); // Même email

        biblio.ajouterUtilisateur(u1);

        // Doit lever une exception car l'email est identique
        assertThrows(IllegalArgumentException.class, () -> {
            biblio.ajouterUtilisateur(u2);
        });
    }

    @Test
    void testSupprimerUtilisateurDeLaListe() {
        Utilisateur u = new Utilisateur("Alice", "Dupont", "alice@mail.com", "01/01/2000", 0, aboStandard);
        biblio.ajouterUtilisateur(u);

        biblio.supprimerUtilisateur(u);
        assertEquals(0, biblio.getUtilisateurs().size());
        assertTrue(u.isSupprime()); // Vérifie que le flag interne est aussi passé à true
    }

    // --- Tests Livres ---

    @Test
    void testAjoutLivreDoublon() {
        Livre l1 = new Livre("Dune", "Herbert", 5);
        Livre l2 = new Livre("Dune", "Herbert", 2); // Même titre/auteur

        biblio.ajouterLivre(l1);

        assertThrows(IllegalArgumentException.class, () -> {
            biblio.ajouterLivre(l2);
        });
    }

    @Test
    void testAjoutAbonnementDoublon() {
        Abonnement abo1 = new Abonnement("Premium", 100, 10, 30);
        Abonnement abo2 = new Abonnement("Premium", 100, 10, 30);

        biblio.ajouterAbonnement(abo1);

        assertThrows(IllegalArgumentException.class, () -> {
            biblio.ajouterAbonnement(abo2);
        });
    }
    @Test
    void testSuppressionLivreRefuseeSiEmprunte() {
        Livre livre = new Livre("1984", "Orwell", 1);
        biblio.ajouterLivre(livre);

        Utilisateur u = new Utilisateur("Bob", "Sponge", "bob@mail.com", "date", 0, aboStandard);
        biblio.ajouterUtilisateur(u);

        // Bob emprunte le dernier exemplaire
        u.emprunterLivre(livre);

        // La bibliothèque essaie de supprimer le livre
        boolean suppressionReussie = biblio.supprimerLivre(livre);

        assertFalse(suppressionReussie, "La suppression doit échouer car le livre est emprunté");
    }

    // --- Tests Abonnements ---

    @Test
    void testSuppressionAbonnementRefuseeSiUtilise() {
        // Un utilisateur utilise l'aboStandard (ajouté dans le setup)
        Utilisateur u = new Utilisateur("Bob", "Sponge", "bob@mail.com", "date", 0, aboStandard);
        biblio.ajouterUtilisateur(u);

        boolean resultat = biblio.supprimerAbonnement(aboStandard);

        assertFalse(resultat, "On ne doit pas supprimer un abonnement actif");
    }

    @Test
    void testSuppressionAbonnementReussieSiInutile() {
        Abonnement aboPremium = new Abonnement("Premium", 100, 10, 30);
        biblio.ajouterAbonnement(aboPremium);

        // Personne n'utilise aboPremium
        boolean resultat = biblio.supprimerAbonnement(aboPremium);

        assertTrue(resultat, "L'abonnement non utilisé doit pouvoir être supprimé");
    }

    @Test
    void testAjoutUtilisateurAvecAbonnementValide() {
        // Cet utilisateur utilise 'aboStandard' qui est déjà dans 'biblio' (voir setup)
        Utilisateur u = new Utilisateur("Alice", "D", "alice@mail.com", "date", 0, aboStandard);

        assertDoesNotThrow(() -> biblio.ajouterUtilisateur(u));
        assertEquals(1, biblio.getUtilisateurs().size());
    }

    @Test
    void testAjoutUtilisateurAvecAbonnementInconnu() {
        // Création d'un abonnement pirate qui n'est PAS ajouté à la biblio
        Abonnement aboPirate = new Abonnement("Pirate", 0, 100, 999);

        Utilisateur u = new Utilisateur("Hacker", "Man", "hack@mail.com", "date", 0, aboPirate);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            biblio.ajouterUtilisateur(u);
        });

        assertEquals("L'abonnement de l'utilisateur n'est pas valide ou inconnu de la bibliothèque.", exception.getMessage());
    }

    @Test
    void testEmpruntLivreInconnuRefuse() {
        // 1. Création d'un utilisateur valide dans la biblio
        Utilisateur u = new Utilisateur("Alice", "D", "alice@mail.com", "date", 0, aboStandard);
        biblio.ajouterUtilisateur(u);

        // 2. Création d'un livre "Fantôme" qui N'EST PAS ajouté à la biblio
        Livre livreFantome = new Livre("Livre Interdit", "Inconnu", 10);

        // 3. Tentative d'emprunt via la bibliothèque
        boolean resultat = biblio.emprunterLivre(u, livreFantome);

        // 4. Vérifications
        assertFalse(resultat, "L'emprunt doit échouer car le livre n'est pas dans la bibliothèque");
        assertEquals(0, u.getEmprunts().size(), "L'utilisateur ne doit rien avoir emprunté");
        assertEquals(10, livreFantome.getExemplairesDispo(), "Le stock du livre fantôme ne doit pas bouger");
    }
}