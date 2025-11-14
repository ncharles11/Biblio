package com.bibliotheque;

import com.bibliotheque.modele.Adherent;
import com.bibliotheque.modele.Emprunt;
import com.bibliotheque.modele.Livre;
import com.bibliotheque.service.BibliothequeService;

/**
 * Application principale de démonstration du système de gestion de bibliothèque
 */
public class BibliothequeApp {

    public static void main(String[] args) {
        System.out.println("=== Système de Gestion de Bibliothèque ===\n");

        // Créer le service
        BibliothequeService service = new BibliothequeService();

        // Ajouter des livres
        System.out.println("--- Ajout de livres au catalogue ---");
        Livre livre1 = new Livre("978-2-1234-5680-3", "Clean Code", "Robert C. Martin", 2008);
        Livre livre2 = new Livre("978-2-1234-5681-0", "Design Patterns", "Gang of Four", 1994);
        Livre livre3 = new Livre("978-2-1234-5682-7", "Refactoring", "Martin Fowler", 1999);

        service.ajouterLivre(livre1);
        service.ajouterLivre(livre2);
        service.ajouterLivre(livre3);

        System.out.println("✓ " + livre1);
        System.out.println("✓ " + livre2);
        System.out.println("✓ " + livre3);
        System.out.println();

        // Inscrire des adhérents
        System.out.println("--- Inscription d'adhérents ---");
        Adherent adherent1 = new Adherent("Dupont", "Jean", "jean.dupont@email.com");
        Adherent adherent2 = new Adherent("Martin", "Marie", "marie.martin@email.com");

        service.inscrireAdherent(adherent1);
        service.inscrireAdherent(adherent2);

        System.out.println("✓ " + adherent1);
        System.out.println("✓ " + adherent2);
        System.out.println();

        // Effectuer des emprunts
        System.out.println("--- Emprunts de livres ---");
        try {
            Emprunt emprunt1 = service.emprunterLivre(livre1.getIsbn(), adherent1.getNumeroCarte());
            System.out.println("✓ " + adherent1.getPrenom() + " " + adherent1.getNom() +
                    " a emprunté '" + livre1.getTitre() + "'");
            System.out.println("  Date de retour prévue: " + emprunt1.getDateRetourPrevue());

            Emprunt emprunt2 = service.emprunterLivre(livre2.getIsbn(), adherent1.getNumeroCarte());
            System.out.println("✓ " + adherent1.getPrenom() + " " + adherent1.getNom() +
                    " a emprunté '" + livre2.getTitre() + "'");

            Emprunt emprunt3 = service.emprunterLivre(livre3.getIsbn(), adherent2.getNumeroCarte());
            System.out.println("✓ " + adherent2.getPrenom() + " " + adherent2.getNom() +
                    " a emprunté '" + livre3.getTitre() + "'");
        } catch (IllegalStateException e) {
            System.out.println("✗ Erreur: " + e.getMessage());
        }
        System.out.println();

        // Recherche de livres
        System.out.println("--- Recherche de livres ---");
        System.out.println("Recherche par titre 'Clean':");
        service.rechercherLivreParTitre("Clean").forEach(l ->
                System.out.println("  - " + l.getTitre() + " par " + l.getAuteur())
        );

        System.out.println("\nRecherche par auteur 'Martin':");
        service.rechercherLivreParAuteur("Martin").forEach(l ->
                System.out.println("  - " + l.getTitre() + " par " + l.getAuteur())
        );
        System.out.println();

        // Afficher les statistiques
        System.out.println("--- Statistiques de la bibliothèque ---");
        BibliothequeService.Statistiques stats = service.obtenirStatistiques();
        System.out.println("Nombre de livres: " + stats.getNombreLivres());
        System.out.println("Nombre d'adhérents: " + stats.getNombreAdherents());
        System.out.println("Emprunts en cours: " + stats.getNombreEmpruntsEnCours());
        System.out.println();

        // Lister les emprunts d'un adhérent
        System.out.println("--- Emprunts de " + adherent1.getPrenom() + " " + adherent1.getNom() + " ---");
        service.listerEmpruntsAdherent(adherent1.getNumeroCarte()).forEach(e ->
                System.out.println("  - " + e.getLivre().getTitre() +
                        " (à rendre le " + e.getDateRetourPrevue() + ")")
        );

        System.out.println("\n=== Fin de la démonstration ===");
    }
}

