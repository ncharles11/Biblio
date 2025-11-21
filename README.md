# Système de Gestion de Bibliothèque


## Technologies Utilisées

- **Java 11**
- **JUnit 5** (Jupiter) - Framework de tests
- **Gradle** - Gestion de build et automatisation

## Fonctionnalités Implémentées

### Gestion des Livres
- Ajouter un livre au catalogue
- Rechercher par titre, auteur ou ISBN
- Vérifier la disponibilité
- Gérer les exemplaires multiples

### Gestion des Adhérents
- Inscrire un adhérent
- Génération automatique de numéro de carte
- Activation/désactivation du compte
- Limite de 5 emprunts simultanés
- Blocage en cas de retard

### Gestion des Emprunts
- Emprunter un livre (durée: 14 jours)
- Retourner un livre
- Prolonger un emprunt (+7 jours)
- Calcul automatique des retards
- Calcul des frais (0,50€/jour)
- Historique des emprunts


## Tests réalisés

### Tests unitaires pour la classe Livre
```java
    @Test
    @DisplayName("La création d'un livre avec des données valides doit réussir")
    void testCreationLivreValide()

    @Test
    @DisplayName("Un nouveau livre doit avoir 1 exemplaire disponible par défaut")
    void testExemplairesParDefaut()

    @Test
    @DisplayName("La création d'un livre avec ISBN null doit lever une exception")
    void testCreationLivreIsbnNull()

    @Test
    @DisplayName("La création d'un livre avec titre vide doit lever une exception")
    void testCreationLivreTitreVide()

    @ParameterizedTest
    @ValueSource(ints = {1800, 3000})
    @DisplayName("La création d'un livre avec année invalide doit lever une exception")
    void testCreationLivreAnneeInvalide(int annee)

    @Test
    @DisplayName("Emprunter un livre doit diminuer les exemplaires disponibles")
    void testEmprunterLivre()

    @Test
    @DisplayName("Emprunter un livre sans exemplaire disponible doit échouer")
    void testEmprunterLivreSansExemplaire()

    @Test
    @DisplayName("Retourner un livre doit augmenter les exemplaires disponibles")
    void testRetournerLivre()

    @Test
    @DisplayName("Retourner un livre alors que tous sont disponibles doit échouer")
    void testRetournerLivreDejaDisponible()

    @Test
    @DisplayName("Vérifier si un livre est disponible")
    void testEstDisponible()

    @Test
    @DisplayName("Ajouter des exemplaires doit augmenter le total et les disponibles")
    void testAjouterExemplaires()

    @Test
    @DisplayName("Ajouter un nombre négatif d'exemplaires doit lever une exception")
    void testAjouterExemplairesNegatif()
```

### Tests unitaires pour la classe Adherent 
```java
    @Test
    @DisplayName("La création d'un adhérent avec des données valides doit réussir")
    void testCreationAdherentValide()

    @Test
    @DisplayName("Un nouvel adhérent doit recevoir un numéro de carte unique")
    void testNumeroCarte()

    @Test
    @DisplayName("La création d'un adhérent avec email null doit lever une exception")
    void testCreationAdherentEmailNull()

    @Test
    @DisplayName("La création d'un adhérent avec email invalide doit lever une exception")
    void testCreationAdherentEmailInvalide()

    @Test
    @DisplayName("Un adhérent peut être désactivé")
    void testDesactiverAdherent()

    @Test
    @DisplayName("Un adhérent désactivé peut être réactivé")
    void testReactiverAdherent()

    @Test
    @DisplayName("Un adhérent peut emprunter jusqu'à 5 livres")
    void testEmprunterLivre()

    @Test
    @DisplayName("Retourner un livre doit diminuer le nombre d'emprunts")
    void testRetournerLivre()

    @Test
    @DisplayName("Un adhérent inactif ne peut pas emprunter")
    void testAdherentInactifNePeutPasEmprunter() 

    @Test
    @DisplayName("Un adhérent avec des retards ne peut pas emprunter")
    void testAdherentAvecRetardsNePeutPasEmprunter() 

    @Test
    @DisplayName("Calculer les frais de retard")
    void testCalculerFraisRetard()
```
### Tests unitaires pour la classe Emprunt
```java
    @Test
    @DisplayName("La création d'un emprunt avec des données valides doit réussir")
    void testCreationEmpruntValide()

    @Test
    @DisplayName("La date de retour prévue doit être 14 jours après l'emprunt")
    void testDateRetourPrevue()

    @Test
    @DisplayName("La création d'un emprunt avec livre null doit lever une exception")
    void testCreationEmpruntLivreNull()

    @Test
    @DisplayName("La création d'un emprunt avec adhérent null doit lever une exception")
    void testCreationEmpruntAdherentNull()

    @Test
    @DisplayName("Retourner un livre doit mettre à jour le statut et la date")
    void testRetournerLivre()

    @Test
    @DisplayName("Un emprunt est en retard si la date actuelle dépasse la date prévue")
    void testEmpruntEnRetard()

    @Test
    @DisplayName("Calculer le nombre de jours de retard")
    void testCalculerJoursRetard()

    @Test
    @DisplayName("Un emprunt sans retard doit retourner 0 jours")
    void testCalculerJoursRetardSansRetard()

    @Test
    @DisplayName("Prolonger un emprunt ajoute 7 jours")
    void testProlongerEmprunt()

    @Test
    @DisplayName("Ne pas pouvoir prolonger un emprunt en retard")
    void testProlongerEmpruntEnRetard()

    @Test
    @DisplayName("Ne pas pouvoir prolonger un emprunt déjà rendu")
    void testProlongerEmpruntRendu()

    @Test
    @DisplayName("Calculer les frais de retard")
    void testCalculerFraisRetard()
```
### Tests unitaires pour BibliothequeService
```java
    @Test
    @DisplayName("Ajouter un livre au catalogue")
    void testAjouterLivre()

    @Test
    @DisplayName("Inscrire un adhérent")
    void testInscrireAdherent()

    @Test
    @DisplayName("Emprunter un livre avec succès")
    void testEmprunterLivreSuccess() 

    @Test
    @DisplayName("Emprunter un livre non disponible doit échouer")
    void testEmprunterLivreNonDisponible() 

    @Test
    @DisplayName("Un adhérent ne peut pas emprunter plus de 5 livres")
    void testLimiteEmprunts()

    @Test
    @DisplayName("Retourner un livre")
    void testRetournerLivre() 

    @Test
    @DisplayName("Rechercher des livres par titre")
    void testRechercherLivreParTitre()

    @Test
    @DisplayName("Rechercher des livres par auteur")
    void testRechercherLivreParAuteur()

    @Test
    @DisplayName("Lister les emprunts d'un adhérent")
    void testListerEmpruntsAdherent()

    @Test
    @DisplayName("Calculer les statistiques de la bibliothèque")
    void testStatistiques()
```
## Règles Métier Implémentées

1. **Livres**
   - ISBN obligatoire et unique
   - Année de publication entre 1900 et année actuelle
   - Gestion des exemplaires disponibles

2. **Adhérents**
   - Email valide obligatoire
   - Maximum 5 emprunts simultanés
   - Blocage si retard non régularisé
   - Numéro de carte unique auto-généré

3. **Emprunts**
   - Durée standard : 14 jours
   - Prolongation possible : +7 jours (si pas de retard)
   - Frais de retard : 0,50€ par jour
   - Statuts : EN_COURS, RENDU, RETARD

## Auteurs

Mourad Eljalil & Charles Ndiaye
