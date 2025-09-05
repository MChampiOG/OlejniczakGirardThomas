## Cahier des charges

### Fonctionnalités

* Affichage d’un échiquier 8×8 avec les 32 pièces en position initiale.
* Déplacement des pièces par interaction graphique (clic sur une pièce, clic sur une case).
* Indication du joueur dont c’est le tour.
* Visualisation des coups possibles (optionnel, surlignage).
* Détection de fin de partie (mat, pat, nulle).

### Règles du jeu

* Respect des déplacements standards de chaque pièce : roi, dame, tour, fou, cavalier, pion.
* Gestion des règles spéciales :

  * **Roque** (petit et grand).
  * **Prise en passant**.
  * **Promotion** d’un pion.
* Détection de l’échec et interdiction de jouer un coup qui met son propre roi en danger.

### Objectifs

* Permettre à deux joueurs humains de jouer une partie complète d’échecs sur ordinateur.
* Garantir une séparation claire entre la logique métier (plateau, règles) et l’interface graphique.
* Assurer un code lisible, testé et maintenable, avec une organisation Git collaborative.
* Répartition des responsabilités :

  * **Simon (branche `board`)** : plateau et pièces (classe `Board`, `Piece` et sous-classes, `Position`).
  * **Wiktor (branche `rules`)** : validation des coups, règles spéciales, détection échec/mat/pat.
  * **Ugo (branche `ui`)** : interface graphique, affichage du plateau et des pièces, interaction joueur.

### Spécifications techniques

* **Langage** : Java (version ≥ 17).
* **Bibliothèque graphique** : Swing ou JavaFX.
* **Organisation du code** :

  * `board` : gestion du plateau et des pièces.
  * `rules` : validation des coups et règles du jeu.
  * `ui` : interface graphique.
* **Tests** : JUnit 5 pour la validation des règles.
* **Outils de build** : Gradle ou Maven.
* **Gestion de versions** : GitHub, avec branches `main`, `develop`, et une branche par module (`board`, `rules`, `ui`).