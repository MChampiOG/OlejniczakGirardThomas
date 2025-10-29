## Cahier des charges - Jeu d'Échecs Java

### Contexte et objectifs du projet

Ce projet vise à développer une application de jeu d'échecs complète en Java, permettant à deux joueurs humains de s'affronter sur un même ordinateur. Le projet s'inscrit dans le cadre du cours de Qualité de Développement (IUT S5) et met l'accent sur les bonnes pratiques de génie logiciel : architecture modulaire, tests unitaires, gestion de versions avec Git, et documentation exhaustive.

**Objectifs pédagogiques** :
- Appliquer les principes SOLID et la séparation des préoccupations
- Pratiquer le développement piloté par les tests (TDD)
- Maîtriser Git et le workflow avec branches
- Produire une documentation technique de qualité professionnelle

### Fonctionnalités

#### Fonctionnalités essentielles (implémentées)

* ✅ Affichage d'un échiquier 8×8 avec les 32 pièces en position initiale
* ✅ Déplacement des pièces par interaction graphique (clic sur une pièce, clic sur une case de destination)
* ✅ Indication claire du joueur dont c'est le tour (affichage en haut de la fenêtre)
* ✅ Validation en temps réel : seuls les coups légaux sont autorisés
* ✅ Détection automatique de fin de partie (échec et mat, pat, nulle)
* ✅ Capture de pièces avec affichage de la liste des pièces capturées
* ✅ Messages d'état du jeu (échec, mat, pat, nulle)

#### Fonctionnalités avancées (implémentées)

* ✅ **Gestion complète des règles spéciales** (voir section suivante)
* ✅ **Détection d'échec** : impossible de jouer un coup qui met ou laisse son roi en échec
* ✅ **Détection de nulle par répétition triple** : même position 3 fois
* ✅ **Règle des 50 coups** : nulle après 50 coups sans capture ni mouvement de pion
* ✅ **Matériel insuffisant** : nulle si impossibilité théorique de mater

#### Fonctionnalités optionnelles (non implémentées)

* ⏳ Visualisation des coups possibles avec surlignage des cases
* ⏳ Annulation du dernier coup (undo)
* ⏳ Sauvegarde et chargement de parties
* ⏳ Timer de réflexion pour chaque joueur
* ⏳ Historique des coups en notation algébrique
* ⏳ Mode contre IA

### Règles du jeu

#### Mouvements standards

L'application implémente les déplacements officiels de toutes les pièces :

* **Roi** : Une case dans toutes les directions (8 directions)
* **Dame** : Nombre illimité de cases en ligne droite (horizontale, verticale, diagonale)
* **Tour** : Nombre illimité de cases en ligne droite (horizontale, verticale)
* **Fou** : Nombre illimité de cases en diagonale
* **Cavalier** : Déplacement en "L" (2+1 ou 1+2 cases), peut sauter par-dessus d'autres pièces
* **Pion** : 
  - Avance d'une case vers l'avant
  - Avance de deux cases lors du premier mouvement
  - Capture en diagonale (une case)

#### Règles spéciales

* ✅ **Roque** (petit et grand)
  - Conditions : ni le roi ni la tour n'ont bougé
  - Le roi n'est pas en échec
  - Les cases entre le roi et la tour sont vides
  - Le roi ne traverse pas une case attaquée
  - Exécution : le roi se déplace de 2 cases, la tour saute par-dessus

* ✅ **Prise en passant**
  - Conditions : un pion adverse vient d'avancer de deux cases
  - Un pion ami se trouve sur la même rangée, colonne adjacente
  - Capture possible immédiatement après le coup adverse
  - Exécution : le pion capture en diagonale comme si le pion adverse n'avait avancé que d'une case

* ✅ **Promotion du pion**
  - Conditions : un pion atteint la dernière rangée (rangée 0 pour blanc, rangée 7 pour noir)
  - Choix : le joueur choisit entre Dame, Tour, Fou ou Cavalier
  - Interface : boîte de dialogue avec 4 boutons pour le choix

#### Sécurité du roi

* ✅ **Détection d'échec** : le jeu affiche "Échec !" lorsque le roi est attaqué
* ✅ **Interdiction des coups suicidaires** : impossible de jouer un coup qui met son propre roi en échec
* ✅ **Échec et mat** : détecté automatiquement (échec + aucun coup légal) → fin de partie
* ✅ **Pat** : détecté automatiquement (pas en échec + aucun coup légal) → nulle

#### Conditions de nulle

* ✅ **Répétition triple** : même position de toutes les pièces répétée 3 fois
* ✅ **Règle des 50 coups** : 50 coups consécutifs sans capture ni mouvement de pion
* ✅ **Matériel insuffisant** :
  - Roi contre roi
  - Roi + fou contre roi
  - Roi + cavalier contre roi

### Objectifs

* Permettre à deux joueurs humains de jouer une partie complète d’échecs sur ordinateur.
* Garantir une séparation claire entre la logique métier (plateau, règles) et l’interface graphique.
* Assurer un code lisible, testé et maintenable, avec une organisation Git collaborative.

### Spécifications techniques

* **Langage** : Java (version ≥ 17).
* **Bibliothèque graphique** : Swing ou JavaFX.
* **Organisation du code** :

  * `board` : gestion du plateau et des pièces.
  * `rules` : validation des coups et règles du jeu.
  * `ui` : interface graphique.
* **Tests** : JUnit 5 pour la validation des règles (47 tests unitaires, 100% de réussite).
* **Outils de build** : Maven 3.6+ avec packaging JAR exécutable.
* **Gestion de versions** : GitHub, avec branches `main`, `develop`, et une branche par module (`board`, `rules`, `ui`).
* **Distribution** : JAR exécutable autonome (`chess-game.jar`) + scripts de lancement multi-plateformes.

### Livrables

#### Code source
- ✅ 17 classes Java (src/main/java)
- ✅ 6 classes de tests (src/test/java)
- ✅ Configuration Maven (pom.xml)

#### Exécutables et scripts
- ✅ JAR exécutable (chess-game.jar, 42 Ko)
- ✅ Script de lancement Linux/Mac (play.sh)
- ✅ Script de lancement Windows (play.bat)

#### Documentation
- ✅ README.md : guide utilisateur complet
- ✅ DOCUMENTATION_TECHNIQUE.md : architecture et conception détaillées
- ✅ Cahier des charges : spécifications fonctionnelles et techniques
- ✅ Commentaires Javadoc dans le code

#### Tests et qualité
- ✅ 47 tests unitaires JUnit 5
- ✅ Couverture fonctionnelle : 100% des règles
- ✅ 0 échec de tests
- ✅ Temps d'exécution des tests : < 2 secondes

### Critères de qualité

**Architecture** :
- ✅ Séparation claire en 3 modules (board, rules, ui)
- ✅ Respect des principes SOLID
- ✅ Pas de dépendances circulaires
- ✅ Couplage faible, cohésion forte

**Code** :
- ✅ Nomenclature Java standard (CamelCase)
- ✅ Méthodes courtes (moyenne 15 lignes)
- ✅ Commentaires sur les algorithmes complexes
- ✅ Pas de code dupliqué

**Tests** :
- ✅ Tests unitaires pour chaque classe métier
- ✅ Tests d'intégration pour les interactions inter-modules
- ✅ Cas nominaux et cas limites couverts

**Documentation** :
- ✅ README complet avec instructions d'installation et utilisation
- ✅ Documentation technique avec diagrammes d'architecture
- ✅ Cahier des charges détaillé
- ✅ Commentaires de code pertinents

**Git** :
- ✅ Historique de commits propre et descriptif
- ✅ Utilisation de branches feature
- ✅ Merge sans conflits
- ✅ 13 commits sur develop, bien structurés

### Conformité aux exigences

| Exigence | Statut | Note |
|----------|--------|------|
| Affichage du plateau 8×8 | ✅ Implémenté | Interface Swing claire |
| Déplacement de toutes les pièces | ✅ Implémenté | 6 types de pièces |
| Règles spéciales (roque, en passant, promotion) | ✅ Implémenté | 100% fonctionnel |
| Détection échec/mat/pat | ✅ Implémenté | Algorithmes complets |
| Détection de nulle | ✅ Implémenté | 3 types de nulles |
| Tests unitaires | ✅ 47 tests | 100% de réussite |
| Documentation technique | ✅ Complète | ~500 lignes |
| Architecture modulaire | ✅ 3 modules | Séparation nette |
| Build automatisé | ✅ Maven | Package JAR |
| Gestion Git | ✅ GitHub | Workflow propre |

---

**Statut du projet** : ✅ **TERMINÉ ET OPÉRATIONNEL**

Le projet répond à toutes les exigences du cahier des charges et peut être utilisé immédiatement pour jouer aux échecs. L'architecture permet des évolutions futures sans refactoring majeur.