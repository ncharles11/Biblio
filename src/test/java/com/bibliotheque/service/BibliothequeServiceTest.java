package com.bibliotheque.service;

import com.bibliotheque.modele.Adherent;
import com.bibliotheque.modele.Emprunt;
import com.bibliotheque.modele.Livre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests unitaires pour BibliothequeService")
class BibliothequeServiceTest {

    private BibliothequeService service;
    private Livre livre;
    private Adherent adherent;

    @BeforeEach
    void setUp() {
        service = new BibliothequeService();
        livre = new Livre("978-2-1234-5680-3", "Clean Code", "Robert C. Martin", 2008);
        adherent = new Adherent("Eljalil", "Mourad", "mourad.eljalil@email.com");
    }

    @Test
    @DisplayName("Ajouter un livre au catalogue")
    void testAjouterLivre() {
        service.ajouterLivre(livre);

        List<Livre> livres = service.rechercherLivreParTitre("Clean Code");
        assertEquals(1, livres.size());
        assertEquals(livre, livres.get(0));
    }

    @Test
    @DisplayName("Inscrire un adhérent")
    void testInscrireAdherent() {
        service.inscrireAdherent(adherent);

        Adherent adherentRecupere = service.rechercherAdherent(adherent.getNumeroCarte());
        assertNotNull(adherentRecupere);
        assertEquals(adherent, adherentRecupere);
    }

    @Test
    @DisplayName("Emprunter un livre avec succès")
    void testEmprunterLivreSuccess() {
        service.ajouterLivre(livre);
        service.inscrireAdherent(adherent);

        Emprunt emprunt = service.emprunterLivre(livre.getIsbn(), adherent.getNumeroCarte());

        assertNotNull(emprunt);
        assertEquals(livre, emprunt.getLivre());
        assertEquals(adherent, emprunt.getAdherent());
        assertEquals(0, livre.getExemplairesDisponibles());
    }

    @Test
    @DisplayName("Emprunter un livre non disponible doit échouer")
    void testEmprunterLivreNonDisponible() {
        service.ajouterLivre(livre);
        service.inscrireAdherent(adherent);

        // Premier emprunt
        service.emprunterLivre(livre.getIsbn(), adherent.getNumeroCarte());

        // Deuxième emprunt avec un autre adhérent
        Adherent autreAdherent = new Adherent("Ndiaye", "Charles", "charles.ndiaye@email.com");
        service.inscrireAdherent(autreAdherent);

        assertThrows(IllegalStateException.class, () -> {
            service.emprunterLivre(livre.getIsbn(), autreAdherent.getNumeroCarte());
        });
    }

    @Test
    @DisplayName("Un adhérent ne peut pas emprunter plus de 5 livres")
    void testLimiteEmprunts() {
        service.inscrireAdherent(adherent);

        // Créer et ajouter 6 livres
        for (int i = 0; i < 6; i++) {
            Livre l = new Livre("978-2-1234-568" + i + "-3", "Livre " + i, "Auteur " + i, 2020);
            service.ajouterLivre(l);
        }

        // Emprunter 5 livres avec succès
        for (int i = 0; i < 5; i++) {
            String isbn = "978-2-1234-568" + i + "-3";
            assertDoesNotThrow(() -> service.emprunterLivre(isbn, adherent.getNumeroCarte()));
        }

        // Le 6ème emprunt doit échouer
        assertThrows(IllegalStateException.class, () -> {
            service.emprunterLivre("978-2-1234-5685-3", adherent.getNumeroCarte());
        });
    }

    @Test
    @DisplayName("Retourner un livre")
    void testRetournerLivre() {
        service.ajouterLivre(livre);
        service.inscrireAdherent(adherent);

        Emprunt emprunt = service.emprunterLivre(livre.getIsbn(), adherent.getNumeroCarte());
        assertEquals(0, livre.getExemplairesDisponibles());

        service.retournerLivre(emprunt);

        assertEquals(1, livre.getExemplairesDisponibles());
        assertEquals(Emprunt.StatutEmprunt.RENDU, emprunt.getStatut());
    }

    @Test
    @DisplayName("Rechercher des livres par titre")
    void testRechercherLivreParTitre() {
        Livre livre1 = new Livre("978-1-1111-1111-1", "Java Programming", "Author 1", 2020);
        Livre livre2 = new Livre("978-2-2222-2222-2", "Java Advanced", "Author 2", 2021);
        Livre livre3 = new Livre("978-3-3333-3333-3", "Python Basics", "Author 3", 2019);

        service.ajouterLivre(livre1);
        service.ajouterLivre(livre2);
        service.ajouterLivre(livre3);

        List<Livre> resultats = service.rechercherLivreParTitre("Java");
        assertEquals(2, resultats.size());
        assertTrue(resultats.contains(livre1));
        assertTrue(resultats.contains(livre2));
    }

    @Test
    @DisplayName("Rechercher des livres par auteur")
    void testRechercherLivreParAuteur() {
        Livre livre1 = new Livre("978-1-1111-1111-1", "Book 1", "Martin Fowler", 2020);
        Livre livre2 = new Livre("978-2-2222-2222-2", "Book 2", "Martin Fowler", 2021);
        Livre livre3 = new Livre("978-3-3333-3333-3", "Book 3", "Robert Martin", 2019);

        service.ajouterLivre(livre1);
        service.ajouterLivre(livre2);
        service.ajouterLivre(livre3);

        List<Livre> resultats = service.rechercherLivreParAuteur("Martin Fowler");
        assertEquals(2, resultats.size());
    }

    @Test
    @DisplayName("Lister les emprunts d'un adhérent")
    void testListerEmpruntsAdherent() {
        service.ajouterLivre(livre);
        service.inscrireAdherent(adherent);

        Livre livre2 = new Livre("978-2-2222-2222-2", "Book 2", "Author 2", 2021);
        service.ajouterLivre(livre2);

        service.emprunterLivre(livre.getIsbn(), adherent.getNumeroCarte());
        service.emprunterLivre(livre2.getIsbn(), adherent.getNumeroCarte());

        List<Emprunt> emprunts = service.listerEmpruntsAdherent(adherent.getNumeroCarte());
        assertEquals(2, emprunts.size());
    }

    @Test
    @DisplayName("Calculer les statistiques de la bibliothèque")
    void testStatistiques() {
        service.ajouterLivre(livre);
        service.inscrireAdherent(adherent);
        service.emprunterLivre(livre.getIsbn(), adherent.getNumeroCarte());

        BibliothequeService.Statistiques stats = service.obtenirStatistiques();

        assertEquals(1, stats.getNombreLivres());
        assertEquals(1, stats.getNombreAdherents());
        assertEquals(1, stats.getNombreEmpruntsEnCours());
    }
}
