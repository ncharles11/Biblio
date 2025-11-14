package com.bibliotheque.service;

import com.bibliotheque.modele.Adherent;
import com.bibliotheque.modele.Emprunt;
import com.bibliotheque.modele.Livre;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service principal de gestion de la bibliothèque
 */
public class BibliothequeService {

    private Map<String, Livre> catalogueLivres;
    private Map<String, Adherent> adherents;
    private List<Emprunt> emprunts;

    public BibliothequeService() {
        this.catalogueLivres = new HashMap<>();
        this.adherents = new HashMap<>();
        this.emprunts = new ArrayList<>();
    }

    /**
     * Ajoute un livre au catalogue
     * @param livre Le livre à ajouter
     */
    public void ajouterLivre(Livre livre) {
        if (livre == null) {
            throw new IllegalArgumentException("Le livre ne peut pas être null");
        }
        catalogueLivres.put(livre.getIsbn(), livre);
    }

    /**
     * Inscrit un nouvel adhérent
     * @param adherent L'adhérent à inscrire
     */
    public void inscrireAdherent(Adherent adherent) {
        if (adherent == null) {
            throw new IllegalArgumentException("L'adhérent ne peut pas être null");
        }
        adherents.put(adherent.getNumeroCarte(), adherent);
    }

    /**
     * Emprunte un livre
     * @param isbn ISBN du livre
     * @param numeroCarte Numéro de carte de l'adhérent
     * @return L'emprunt créé
     * @throws IllegalStateException si l'emprunt n'est pas possible
     */
    public Emprunt emprunterLivre(String isbn, String numeroCarte) {
        Livre livre = catalogueLivres.get(isbn);
        if (livre == null) {
            throw new IllegalStateException("Livre non trouvé");
        }

        Adherent adherent = adherents.get(numeroCarte);
        if (adherent == null) {
            throw new IllegalStateException("Adhérent non trouvé");
        }

        if (!adherent.peutEmprunter()) {
            throw new IllegalStateException("L'adhérent ne peut pas emprunter de livre");
        }

        if (!livre.estDisponible()) {
            throw new IllegalStateException("Le livre n'est pas disponible");
        }

        // Effectuer l'emprunt
        if (!livre.emprunter()) {
            throw new IllegalStateException("Impossible d'emprunter le livre");
        }

        adherent.ajouterEmprunt();

        Emprunt emprunt = new Emprunt(livre, adherent);
        emprunts.add(emprunt);

        return emprunt;
    }

    /**
     * Retourne un livre
     * @param emprunt L'emprunt à terminer
     */
    public void retournerLivre(Emprunt emprunt) {
        if (emprunt == null) {
            throw new IllegalArgumentException("L'emprunt ne peut pas être null");
        }

        emprunt.retourner();
        emprunt.getLivre().retourner();
        emprunt.getAdherent().retirerEmprunt();

        // Si l'emprunt est en retard, mettre à jour l'adhérent
        if (emprunt.estEnRetard()) {
            emprunt.getAdherent().ajouterRetard();
            emprunt.getAdherent().ajouterJoursRetard(emprunt.calculerJoursRetard());
        }
    }

    /**
     * Recherche des livres par titre
     * @param titre Le titre (ou partie du titre) à rechercher
     * @return La liste des livres correspondants
     */
    public List<Livre> rechercherLivreParTitre(String titre) {
        if (titre == null || titre.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return catalogueLivres.values().stream()
                .filter(livre -> livre.getTitre().toLowerCase().contains(titre.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Recherche des livres par auteur
     * @param auteur L'auteur à rechercher
     * @return La liste des livres correspondants
     */
    public List<Livre> rechercherLivreParAuteur(String auteur) {
        if (auteur == null || auteur.trim().isEmpty()) {
            return new ArrayList<>();
        }

        return catalogueLivres.values().stream()
                .filter(livre -> livre.getAuteur().toLowerCase().contains(auteur.toLowerCase()))
                .collect(Collectors.toList());
    }

    /**
     * Recherche un livre par ISBN
     * @param isbn L'ISBN du livre
     * @return Le livre correspondant ou null
     */
    public Livre rechercherLivreParIsbn(String isbn) {
        return catalogueLivres.get(isbn);
    }

    /**
     * Recherche un adhérent par numéro de carte
     * @param numeroCarte Le numéro de carte
     * @return L'adhérent correspondant ou null
     */
    public Adherent rechercherAdherent(String numeroCarte) {
        return adherents.get(numeroCarte);
    }

    /**
     * Liste les emprunts d'un adhérent
     * @param numeroCarte Le numéro de carte de l'adhérent
     * @return La liste des emprunts
     */
    public List<Emprunt> listerEmpruntsAdherent(String numeroCarte) {
        return emprunts.stream()
                .filter(e -> e.getAdherent().getNumeroCarte().equals(numeroCarte))
                .collect(Collectors.toList());
    }

    /**
     * Liste tous les emprunts en cours
     * @return La liste des emprunts en cours
     */
    public List<Emprunt> listerEmpruntsEnCours() {
        return emprunts.stream()
                .filter(e -> e.getStatut() == Emprunt.StatutEmprunt.EN_COURS)
                .collect(Collectors.toList());
    }

    /**
     * Obtient les statistiques de la bibliothèque
     * @return Les statistiques
     */
    public Statistiques obtenirStatistiques() {
        long nombreEmpruntsEnCours = emprunts.stream()
                .filter(e -> e.getStatut() == Emprunt.StatutEmprunt.EN_COURS)
                .count();

        return new Statistiques(
                catalogueLivres.size(),
                adherents.size(),
                (int) nombreEmpruntsEnCours
        );
    }

    /**
     * Classe interne pour les statistiques
     */
    public static class Statistiques {
        private final int nombreLivres;
        private final int nombreAdherents;
        private final int nombreEmpruntsEnCours;

        public Statistiques(int nombreLivres, int nombreAdherents, int nombreEmpruntsEnCours) {
            this.nombreLivres = nombreLivres;
            this.nombreAdherents = nombreAdherents;
            this.nombreEmpruntsEnCours = nombreEmpruntsEnCours;
        }

        public int getNombreLivres() {
            return nombreLivres;
        }

        public int getNombreAdherents() {
            return nombreAdherents;
        }

        public int getNombreEmpruntsEnCours() {
            return nombreEmpruntsEnCours;
        }

        @Override
        public String toString() {
            return String.format("Statistiques{livres=%d, adhérents=%d, emprunts en cours=%d}",
                    nombreLivres, nombreAdherents, nombreEmpruntsEnCours);
        }
    }
}

