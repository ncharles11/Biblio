package com.bibliotheque.modele;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Représente un emprunt de livre par un adhérent
 */
public class Emprunt {
    private static final int DUREE_EMPRUNT_JOURS = 14;
    private static final int DUREE_PROLONGATION_JOURS = 7;
    private static final double FRAIS_PAR_JOUR = 0.50;

    public enum StatutEmprunt {
        EN_COURS,
        RENDU,
        RETARD
    }

    private Livre livre;
    private Adherent adherent;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue;
    private LocalDate dateRetourEffective;
    private StatutEmprunt statut;

    /**
     * Constructeur d'un emprunt
     * @param livre Livre emprunté
     * @param adherent Adhérent qui emprunte
     * @throws IllegalArgumentException si les paramètres sont invalides
     */
    public Emprunt(Livre livre, Adherent adherent) {
        if (livre == null) {
            throw new IllegalArgumentException("Le livre ne peut pas être null");
        }
        if (adherent == null) {
            throw new IllegalArgumentException("L'adhérent ne peut pas être null");
        }

        this.livre = livre;
        this.adherent = adherent;
        this.dateEmprunt = LocalDate.now();
        this.dateRetourPrevue = dateEmprunt.plusDays(DUREE_EMPRUNT_JOURS);
        this.statut = StatutEmprunt.EN_COURS;
    }

    /**
     * Retourne le livre
     */
    public void retourner() {
        this.dateRetourEffective = LocalDate.now();
        this.statut = StatutEmprunt.RENDU;
    }

    /**
     * Vérifie si l'emprunt est en retard
     * @return true si l'emprunt est en retard
     */
    public boolean estEnRetard() {
        if (statut == StatutEmprunt.RENDU) {
            return false;
        }

        LocalDate dateReference = dateRetourEffective != null ? dateRetourEffective : LocalDate.now();
        boolean enRetard = dateReference.isAfter(dateRetourPrevue);

        if (enRetard && statut == StatutEmprunt.EN_COURS) {
            statut = StatutEmprunt.RETARD;
        }

        return enRetard;
    }

    /**
     * Calcule le nombre de jours de retard
     * @return Le nombre de jours de retard (0 si pas de retard)
     */
    public int calculerJoursRetard() {
        if (!estEnRetard()) {
            return 0;
        }

        LocalDate dateReference = dateRetourEffective != null ? dateRetourEffective : LocalDate.now();
        return (int) ChronoUnit.DAYS.between(dateRetourPrevue, dateReference);
    }

    /**
     * Calcule les frais de retard
     * @return Le montant des frais de retard
     */
    public double calculerFraisRetard() {
        return calculerJoursRetard() * FRAIS_PAR_JOUR;
    }

    /**
     * Prolonge l'emprunt de 7 jours
     * @return true si la prolongation a réussi, false sinon
     */
    public boolean prolonger() {
        if (statut != StatutEmprunt.EN_COURS || estEnRetard()) {
            return false;
        }

        dateRetourPrevue = dateRetourPrevue.plusDays(DUREE_PROLONGATION_JOURS);
        return true;
    }

    // Getters
    public Livre getLivre() {
        return livre;
    }

    public Adherent getAdherent() {
        return adherent;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    public LocalDate getDateRetourPrevue() {
        return dateRetourPrevue;
    }

    public LocalDate getDateRetourEffective() {
        return dateRetourEffective;
    }

    public StatutEmprunt getStatut() {
        return statut;
    }

    // Setters pour les tests (package-private)
    void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    void setDateRetourPrevue(LocalDate dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    @Override
    public String toString() {
        return String.format("Emprunt{livre=%s, adherent=%s, dateEmprunt=%s, dateRetourPrevue=%s, statut=%s}",
                livre.getTitre(), adherent.getNom(), dateEmprunt, dateRetourPrevue, statut);
    }
}

