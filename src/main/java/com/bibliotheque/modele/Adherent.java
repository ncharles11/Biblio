package com.bibliotheque.modele;

import java.time.LocalDate;
import java.util.UUID;
import java.util.regex.Pattern;


//Représente un adhérent de la bibliothèque
public class Adherent {
    private static final int MAX_EMPRUNTS = 5;
    private static final double FRAIS_PAR_JOUR = 0.50;
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private String numeroCarte;
    private String nom;
    private String prenom;
    private String email;
    private LocalDate dateInscription;
    private boolean actif;
    private int nombreEmpruntsEnCours;
    private boolean aDesRetards;
    private int joursRetard;

    // Constructeur d'un adhérent
    public Adherent(String nom, String prenom, String email) {
        validerParametres(nom, prenom, email);

        this.numeroCarte = genererNumeroCarte();
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.dateInscription = LocalDate.now();
        this.actif = true;
        this.nombreEmpruntsEnCours = 0;
        this.aDesRetards = false;
        this.joursRetard = 0;
    }

    private void validerParametres(String nom, String prenom, String email) {
        if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom ne peut pas être null ou vide");
        }

        if (prenom == null || prenom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le prénom ne peut pas être null ou vide");
        }

        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("L'email est invalide");
        }
    }

    private String genererNumeroCarte() {
        return "BIB-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }


     // Désactive le compte de l'adhérent

    public void desactiver() {
        this.actif = false;
    }

    // Réactive le compte de l'adhérent
    public void reactiver() {
        this.actif = true;
    }


    // Vérifie si l'adhérent peut emprunter un livre
    public boolean peutEmprunter() {
        return actif && nombreEmpruntsEnCours < MAX_EMPRUNTS && !aDesRetards;
    }

    // Ajoute un emprunt à l'adhérent
    public void ajouterEmprunt() {
        if (!peutEmprunter()) {
            throw new IllegalStateException("L'adhérent ne peut pas emprunter de livre");
        }
        nombreEmpruntsEnCours++;
    }

    //Retire un emprunt de l'adhérent (lors d'un retour)
    public void retirerEmprunt() {
        if (nombreEmpruntsEnCours > 0) {
            nombreEmpruntsEnCours--;
        }
    }


    // Indique que l'adhérent a un retard
    public void ajouterRetard() {
        this.aDesRetards = true;
    }


     // Ajoute des jours de retard
    public void ajouterJoursRetard(int jours) {
        if (jours < 0) {
            throw new IllegalArgumentException("Le nombre de jours ne peut pas être négatif");
        }
        this.joursRetard += jours;
    }

    // Calcule les frais de retard

    public double calculerFraisRetard() {
        return joursRetard * FRAIS_PAR_JOUR;
    }


     // Réinitialise les retards après paiement

    public void reinitialiserRetards() {
        this.aDesRetards = false;
        this.joursRetard = 0;
    }

    // Getters
    public String getNumeroCarte() {
        return numeroCarte;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDateInscription() {
        return dateInscription;
    }

    public boolean isActif() {
        return actif;
    }

    public int getNombreEmpruntsEnCours() {
        return nombreEmpruntsEnCours;
    }

    public boolean aDesRetards() {
        return aDesRetards;
    }

    public int getJoursRetard() {
        return joursRetard;
    }

    @Override
    public String toString() {
        return String.format("Adherent{carte='%s', nom='%s %s', email='%s', actif=%s, emprunts=%d}",
                numeroCarte, prenom, nom, email, actif, nombreEmpruntsEnCours);
    }
}

