package com.bibliotheque.modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests unitaires pour la classe Adherent")
class AdherentTest {

    private Adherent adherent;

    @BeforeEach
    void setUp() {
        adherent = new Adherent("Eljalil", "Mourad", "mourad.eljalil@email.com");
    }

    @Test
    @DisplayName("La création d'un adhérent avec des données valides doit réussir")
    void testCreationAdherentValide() {
        assertNotNull(adherent);
        assertEquals("Eljalil", adherent.getNom());
        assertEquals("Mourad", adherent.getPrenom());
        assertEquals("mourad.eljalil@email.com", adherent.getEmail());
        assertTrue(adherent.isActif());
    }

    @Test
    @DisplayName("Un nouvel adhérent doit recevoir un numéro de carte unique")
    void testNumeroCarte() {
        assertNotNull(adherent.getNumeroCarte());
        assertFalse(adherent.getNumeroCarte().isEmpty());

        // Créer un autre adhérent pour vérifier l'unicité
        Adherent autreAdherent = new Adherent("Ndiaye", "Charles", "charles.ndiaye@email.com");
        assertNotEquals(adherent.getNumeroCarte(), autreAdherent.getNumeroCarte());
    }

    @Test
    @DisplayName("La création d'un adhérent avec email null doit lever une exception")
    void testCreationAdherentEmailNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Adherent("Eljalil", "Mourad", null);
        });
    }

    @Test
    @DisplayName("La création d'un adhérent avec email invalide doit lever une exception")
    void testCreationAdherentEmailInvalide() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Adherent("Eljalil", "Mourad", "email-invalide");
        });
    }

    @Test
    @DisplayName("Un adhérent peut être désactivé")
    void testDesactiverAdherent() {
        assertTrue(adherent.isActif());
        adherent.desactiver();
        assertFalse(adherent.isActif());
    }

    @Test
    @DisplayName("Un adhérent désactivé peut être réactivé")
    void testReactiverAdherent() {
        adherent.desactiver();
        assertFalse(adherent.isActif());

        adherent.reactiver();
        assertTrue(adherent.isActif());
    }

    @Test
    @DisplayName("Un adhérent peut emprunter jusqu'à 5 livres")
    void testEmprunterLivre() {
        for (int i = 0; i < 5; i++) {
            assertTrue(adherent.peutEmprunter());
            adherent.ajouterEmprunt();
        }
        assertEquals(5, adherent.getNombreEmpruntsEnCours());
        assertFalse(adherent.peutEmprunter());
    }

    @Test
    @DisplayName("Retourner un livre doit diminuer le nombre d'emprunts")
    void testRetournerLivre() {
        adherent.ajouterEmprunt();
        adherent.ajouterEmprunt();
        assertEquals(2, adherent.getNombreEmpruntsEnCours());

        adherent.retirerEmprunt();
        assertEquals(1, adherent.getNombreEmpruntsEnCours());
        assertTrue(adherent.peutEmprunter());
    }

    @Test
    @DisplayName("Un adhérent inactif ne peut pas emprunter")
    void testAdherentInactifNePeutPasEmprunter() {
        adherent.desactiver();
        assertFalse(adherent.peutEmprunter());
    }

    @Test
    @DisplayName("Un adhérent avec des retards ne peut pas emprunter")
    void testAdherentAvecRetardsNePeutPasEmprunter() {
        adherent.ajouterRetard();
        assertFalse(adherent.peutEmprunter());
    }

    @Test
    @DisplayName("Calculer les frais de retard")
    void testCalculerFraisRetard() {
        adherent.ajouterRetard();
        adherent.ajouterJoursRetard(10);

        double fraisAttendus = 10 * 0.50; // 0.50€ par jour
        assertEquals(fraisAttendus, adherent.calculerFraisRetard(), 0.01);
    }
}

