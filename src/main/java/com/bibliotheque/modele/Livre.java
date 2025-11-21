package com.bibliotheque.modele;

import java.time.Year;

// Représente un livre dans le système de gestion de bibliothèque

public class Livre {
    private String isbn;
    private String titre;
    private String auteur;
    private int anneePublication;
    private int nombreExemplaires;
    private int exemplairesDisponibles;




    public Livre(String isbn, String titre, String auteur, int anneePublication) {
        validerParametres(isbn, titre, auteur, anneePublication);

        this.isbn = isbn;
        this.titre = titre;
        this.auteur = auteur;
        this.anneePublication = anneePublication;
        this.nombreExemplaires = 1;
        this.exemplairesDisponibles = 1;
    }

    private void validerParametres(String isbn, String titre, String auteur, int anneePublication) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("L'ISBN ne peut pas être null ou vide");
        }

        if (titre == null || titre.trim().isEmpty()) {
            throw new IllegalArgumentException("Le titre ne peut pas être null ou vide");
        }

        if (auteur == null || auteur.trim().isEmpty()) {
            throw new IllegalArgumentException("L'auteur ne peut pas être null ou vide");
        }

        int anneeActuelle = Year.now().getValue();
        if (anneePublication < 1900 || anneePublication > anneeActuelle) {
            throw new IllegalArgumentException(
                    "L'année de publication doit être entre 1900 et " + anneeActuelle
            );
        }
    }


     // Emprunte un exemplaire du livre

    public boolean emprunter() {
        if (exemplairesDisponibles > 0) {
            exemplairesDisponibles--;
            return true;
        }
        return false;
    }

    //Retourne un exemplaire du livre
    public boolean retourner() {
        if (exemplairesDisponibles < nombreExemplaires) {
            exemplairesDisponibles++;
            return true;
        }
        return false;
    }

    // Vérifie si le livre est disponible à l'emprunt
    public boolean estDisponible() {
        return exemplairesDisponibles > 0;
    }


    // Ajoute des exemplaires au livre
    public void ajouterExemplaires(int nombre) {
        if (nombre < 0) {
            throw new IllegalArgumentException("Le nombre d'exemplaires ne peut pas être négatif");
        }
        nombreExemplaires += nombre;
        exemplairesDisponibles += nombre;
    }

    // Getters
    public String getIsbn() {
        return isbn;
    }

    public String getTitre() {
        return titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public int getAnneePublication() {
        return anneePublication;
    }

    public int getNombreExemplaires() {
        return nombreExemplaires;
    }

    public int getExemplairesDisponibles() {
        return exemplairesDisponibles;
    }

    @Override
    public String toString() {
        return String.format("Livre{isbn='%s', titre='%s', auteur='%s', année=%d, exemplaires=%d/%d}",
                isbn, titre, auteur, anneePublication, exemplairesDisponibles, nombreExemplaires);
    }
}
