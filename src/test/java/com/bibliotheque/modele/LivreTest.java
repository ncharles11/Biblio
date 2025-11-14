package com.bibliotheque.modele;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests unitaires pour la classe Livre")
class LivreTest {

    private Livre livre;

    @BeforeEach
    void setUp() {
        // Initialisation avant chaque test
        livre = new Livre("978-2-1234-5680-3", "Clean Code", "Robert C. Martin", 2008);
    }

    @Test
    @DisplayName("La création d'un livre avec des données valides doit réussir")
    void testCreationLivreValide() {
        assertNotNull(livre);
        assertEquals("978-2-1234-5680-3", livre.getIsbn());
        assertEquals("Clean Code", livre.getTitre());
        assertEquals("Robert C. Martin", livre.getAuteur());
        assertEquals(2008, livre.getAnneePublication());
    }

    @Test
    @DisplayName("Un nouveau livre doit avoir 1 exemplaire disponible par défaut")
    void testExemplairesParDefaut() {
        assertEquals(1, livre.getNombreExemplaires());
        assertEquals(1, livre.getExemplairesDisponibles());
    }

    @Test
    @DisplayName("La création d'un livre avec ISBN null doit lever une exception")
    void testCreationLivreIsbnNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Livre(null, "Titre", "Auteur", 2020);
        });
    }

    @Test
    @DisplayName("La création d'un livre avec titre vide doit lever une exception")
    void testCreationLivreTitreVide() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Livre("978-2-1234-5680-3", "", "Auteur", 2020);
        });
    }

    @ParameterizedTest
    @ValueSource(ints = {1800, 3000})
    @DisplayName("La création d'un livre avec année invalide doit lever une exception")
    void testCreationLivreAnneeInvalide(int annee) {
        assertThrows(IllegalArgumentException.class, () -> {
            new Livre("978-2-1234-5680-3", "Titre", "Auteur", annee);
        });
    }

    @Test
    @DisplayName("Emprunter un livre doit diminuer les exemplaires disponibles")
    void testEmprunterLivre() {
        assertTrue(livre.emprunter());
        assertEquals(0, livre.getExemplairesDisponibles());
        assertEquals(1, livre.getNombreExemplaires());
    }

    @Test
    @DisplayName("Emprunter un livre sans exemplaire disponible doit échouer")
    void testEmprunterLivreSansExemplaire() {
        livre.emprunter(); // Premier emprunt
        assertFalse(livre.emprunter()); // Deuxième emprunt doit échouer
        assertEquals(0, livre.getExemplairesDisponibles());
    }

    @Test
    @DisplayName("Retourner un livre doit augmenter les exemplaires disponibles")
    void testRetournerLivre() {
        livre.emprunter();
        assertEquals(0, livre.getExemplairesDisponibles());

        assertTrue(livre.retourner());
        assertEquals(1, livre.getExemplairesDisponibles());
    }

    @Test
    @DisplayName("Retourner un livre alors que tous sont disponibles doit échouer")
    void testRetournerLivreDejaDisponible() {
        assertFalse(livre.retourner());
        assertEquals(1, livre.getExemplairesDisponibles());
    }

    @Test
    @DisplayName("Vérifier si un livre est disponible")
    void testEstDisponible() {
        assertTrue(livre.estDisponible());
        livre.emprunter();
        assertFalse(livre.estDisponible());
    }

    @Test
    @DisplayName("Ajouter des exemplaires doit augmenter le total et les disponibles")
    void testAjouterExemplaires() {
        livre.ajouterExemplaires(3);
        assertEquals(4, livre.getNombreExemplaires());
        assertEquals(4, livre.getExemplairesDisponibles());
    }

    @Test
    @DisplayName("Ajouter un nombre négatif d'exemplaires doit lever une exception")
    void testAjouterExemplairesNegatif() {
        assertThrows(IllegalArgumentException.class, () -> {
            livre.ajouterExemplaires(-1);
        });
    }
}
