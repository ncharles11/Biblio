package com.bibliotheque.modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests unitaires pour la classe Emprunt")
class EmpruntTest {

    private Livre livre;
    private Adherent adherent;
    private Emprunt emprunt;

    @BeforeEach
    void setUp() {
        livre = new Livre("978-2-1234-5680-3", "Clean Code", "Robert C. Martin", 2008);
        adherent = new Adherent("Dupont", "Jean", "jean.dupont@email.com");
        emprunt = new Emprunt(livre, adherent);
    }

    @Test
    @DisplayName("La création d'un emprunt avec des données valides doit réussir")
    void testCreationEmpruntValide() {
        assertNotNull(emprunt);
        assertEquals(livre, emprunt.getLivre());
        assertEquals(adherent, emprunt.getAdherent());
        assertEquals(LocalDate.now(), emprunt.getDateEmprunt());
        assertEquals(Emprunt.StatutEmprunt.EN_COURS, emprunt.getStatut());
    }

    @Test
    @DisplayName("La date de retour prévue doit être 14 jours après l'emprunt")
    void testDateRetourPrevue() {
        LocalDate dateRetourAttendue = LocalDate.now().plusDays(14);
        assertEquals(dateRetourAttendue, emprunt.getDateRetourPrevue());
    }

    @Test
    @DisplayName("La création d'un emprunt avec livre null doit lever une exception")
    void testCreationEmpruntLivreNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Emprunt(null, adherent);
        });
    }

    @Test
    @DisplayName("La création d'un emprunt avec adhérent null doit lever une exception")
    void testCreationEmpruntAdherentNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Emprunt(livre, null);
        });
    }

    @Test
    @DisplayName("Retourner un livre doit mettre à jour le statut et la date")
    void testRetournerLivre() {
        emprunt.retourner();

        assertEquals(Emprunt.StatutEmprunt.RENDU, emprunt.getStatut());
        assertEquals(LocalDate.now(), emprunt.getDateRetourEffective());
        assertFalse(emprunt.estEnRetard());
    }

    @Test
    @DisplayName("Un emprunt est en retard si la date actuelle dépasse la date prévue")
    void testEmpruntEnRetard() {
        // Créer un emprunt avec une date de retour dans le passé
        Emprunt empruntEnRetard = new Emprunt(livre, adherent);
        // Simuler un emprunt ancien en modifiant directement (pour le test)
        empruntEnRetard.setDateEmprunt(LocalDate.now().minusDays(20));
        empruntEnRetard.setDateRetourPrevue(LocalDate.now().minusDays(6));

        assertTrue(empruntEnRetard.estEnRetard());
        assertEquals(Emprunt.StatutEmprunt.RETARD, empruntEnRetard.getStatut());
    }

    @Test
    @DisplayName("Calculer le nombre de jours de retard")
    void testCalculerJoursRetard() {
        emprunt.setDateEmprunt(LocalDate.now().minusDays(20));
        emprunt.setDateRetourPrevue(LocalDate.now().minusDays(6));

        assertEquals(6, emprunt.calculerJoursRetard());
    }

    @Test
    @DisplayName("Un emprunt sans retard doit retourner 0 jours")
    void testCalculerJoursRetardSansRetard() {
        assertEquals(0, emprunt.calculerJoursRetard());
    }

    @Test
    @DisplayName("Prolonger un emprunt ajoute 7 jours")
    void testProlongerEmprunt() {
        LocalDate dateRetourInitiale = emprunt.getDateRetourPrevue();

        assertTrue(emprunt.prolonger());

        LocalDate dateRetourApres = emprunt.getDateRetourPrevue();
        assertEquals(dateRetourInitiale.plusDays(7), dateRetourApres);
    }

    @Test
    @DisplayName("Ne pas pouvoir prolonger un emprunt en retard")
    void testProlongerEmpruntEnRetard() {
        emprunt.setDateEmprunt(LocalDate.now().minusDays(20));
        emprunt.setDateRetourPrevue(LocalDate.now().minusDays(6));

        assertFalse(emprunt.prolonger());
    }

    @Test
    @DisplayName("Ne pas pouvoir prolonger un emprunt déjà rendu")
    void testProlongerEmpruntRendu() {
        emprunt.retourner();
        assertFalse(emprunt.prolonger());
    }

    @Test
    @DisplayName("Calculer les frais de retard")
    void testCalculerFraisRetard() {
        emprunt.setDateEmprunt(LocalDate.now().minusDays(20));
        emprunt.setDateRetourPrevue(LocalDate.now().minusDays(6));

        double fraisAttendus = 6 * 0.50; // 6 jours * 0.50€
        assertEquals(fraisAttendus, emprunt.calculerFraisRetard(), 0.01);
    }
}

