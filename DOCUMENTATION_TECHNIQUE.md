# Documentation Technique - Jeu d'Échecs Java

## Table des matières

1. [Architecture générale](#architecture-générale)
2. [Modules et responsabilités](#modules-et-responsabilités)
3. [Classes principales](#classes-principales)
4. [Flux de données](#flux-de-données)
5. [Choix de conception](#choix-de-conception)
6. [Tests et qualité](#tests-et-qualité)
7. [Build et déploiement](#build-et-déploiement)

---

## Architecture générale

Le projet suit une architecture en **trois couches** séparant clairement les responsabilités :

```
┌─────────────────────────────────────────────────┐
│              Interface Utilisateur               │
│                  (ui package)                    │
│           ChessUI.java - Swing GUI               │
└────────────────────┬────────────────────────────┘
                     │
                     │ Événements utilisateur
                     │ Mise à jour de l'affichage
                     ▼
┌─────────────────────────────────────────────────┐
│            Moteur de Règles                      │
│               (rules package)                    │
│  MoveValidator, SpecialMovesHandler,             │
│  GameStateChecker, MoveHistory                   │
└────────────────────┬────────────────────────────┘
                     │
                     │ Validation des coups
                     │ État du jeu
                     ▼
┌─────────────────────────────────────────────────┐
│           Modèle de Données                      │
│              (board package)                     │
│     Board, Piece (+ sous-classes), Position      │
└─────────────────────────────────────────────────┘
```

### Principes architecturaux

- **Séparation des préoccupations** : Chaque module a une responsabilité unique
- **Inversion des dépendances** : L'UI dépend des règles, les règles dépendent du modèle
- **Encapsulation** : Les détails d'implémentation sont cachés derrière des interfaces publiques
- **Testabilité** : Chaque couche peut être testée indépendamment

---

## Modules et responsabilités

### Module `board` - Modèle de données

**Responsabilité** : Représenter l'état du plateau et des pièces

**Classes principales** :
- `Board` : Plateau de jeu 8×8, gestion des pièces
- `Piece` (abstraite) : Classe de base pour toutes les pièces
- `King`, `Queen`, `Rook`, `Bishop`, `Knight`, `Pawn` : Implémentations spécifiques
- `Position` : Coordonnées sur l'échiquier (ligne, colonne)
- `Color` : Énumération (WHITE, BLACK)
- `PieceType` : Énumération des types de pièces

**Fonctionnalités** :
- Initialisation du plateau en configuration standard
- Déplacement de pièces
- Capture de pièces
- Recherche de pièces par position ou type
- Validation syntaxique des mouvements (trajectoire libre, cible valide)

### Module `rules` - Moteur de règles

**Responsabilité** : Valider les coups selon les règles officielles des échecs

**Classes principales** :

#### `MoveValidator`
- Valide si un coup est légal pour un joueur donné
- Vérifie que le coup ne met pas le roi en échec
- Génère la liste des coups valides pour une pièce

#### `SpecialMovesHandler`
- Gère le **roque** (petit et grand)
- Gère la **prise en passant**
- Gère la **promotion du pion**
- Exécute ces coups spéciaux avec toutes leurs contraintes

#### `GameStateChecker`
- Détecte l'**échec** (roi attaqué)
- Détecte l'**échec et mat** (échec sans échappatoire)
- Détecte le **pat** (pas de coup légal, mais pas en échec)
- Détecte les **nulles** :
  - Répétition triple (même position 3 fois)
  - Règle des 50 coups (50 coups sans capture ni mouvement de pion)
  - Matériel insuffisant (roi seul vs roi seul, etc.)

#### `MoveHistory`
- Enregistre l'historique complet des coups
- Suit le dernier mouvement de pion double pour l'en passant
- Compte les occurrences de positions pour la répétition triple
- Compte les coups sans capture pour la règle des 50 coups

### Module `ui` - Interface graphique

**Responsabilité** : Affichage et interactions utilisateur

**Classe principale** :

#### `ChessUI`
- Fenêtre Swing avec JFrame
- Grille 8×8 de boutons représentant les cases
- Affichage des pièces avec symboles Unicode
- Gestion des clics utilisateur (sélection/déplacement)
- Mise à jour visuelle après chaque coup
- Affichage du statut du jeu (tour du joueur, échec, mat, etc.)
- Dialogue pour la promotion du pion

**Interactions** :
1. L'utilisateur clique sur une pièce → la case est surlignée
2. L'utilisateur clique sur une destination → validation via `MoveValidator`
3. Si valide → exécution via `SpecialMovesHandler` si nécessaire
4. Mise à jour de l'affichage et vérification de l'état du jeu

---

## Classes principales

### `Board.java`

```java
public class Board {
    private Piece[][] board;           // Plateau 8x8
    private List<Piece> capturedPieces; // Pièces capturées
    private Color currentPlayer;       // Joueur actuel
    
    // Initialisation et manipulation
    public void initializeBoard();
    public Piece getPieceAt(Position position);
    public boolean movePiece(Position from, Position to);
    
    // Recherche
    public Position findKing(Color color);
    public List<Piece> getAllPieces(Color color);
    
    // État
    public Color getCurrentPlayer();
    public void switchPlayer();
}
```

**Invariants** :
- Un plateau contient toujours exactement 2 rois (un par couleur)
- Les positions sont toujours dans les limites [0-7, 0-7]
- Le joueur courant alterne après chaque coup valide

### `Piece.java` (abstraite)

```java
public abstract class Piece {
    protected Position position;
    protected Color color;
    protected boolean hasMoved;
    
    public abstract PieceType getType();
    public abstract boolean isValidMove(Position to, Board board);
    public abstract List<Position> getPossibleMoves(Board board);
    
    // Méthodes communes
    protected boolean isPathClear(Position to, Board board);
    protected boolean isOpponent(Piece other);
}
```

**Pattern** : Template Method - `isValidMove()` définit la logique de validation, implémentée différemment par chaque sous-classe.

### `MoveValidator.java`

```java
public class MoveValidator {
    private Board board;
    private MoveHistory moveHistory;
    
    public boolean isValidMove(Position from, Position to, Color playerColor);
    public List<Position> getValidMoves(Position from);
    
    // Vérification du roi en échec
    private boolean wouldKingBeInCheckAfterMove(Position from, Position to, Color playerColor);
}
```

**Logique de validation** :
1. La pièce appartient au joueur courant
2. Le mouvement est valide selon la pièce (`Piece.isValidMove()`)
3. Le coup ne met pas le roi du joueur en échec

### `SpecialMovesHandler.java`

```java
public class SpecialMovesHandler {
    private Board board;
    private MoveHistory moveHistory;
    
    // Roque
    public boolean isCastling(Position from, Position to);
    public void executeCastling(Position kingFrom, Position kingTo);
    
    // En passant
    public boolean isEnPassant(Position from, Position to);
    public void executeEnPassant(Position from, Position to);
    
    // Promotion
    public boolean isPromotion(Position from, Position to);
    public void executePromotion(Position pawnPosition, PieceType newType);
}
```

**Contraintes du roque** :
- Le roi et la tour n'ont jamais bougé
- Pas de pièces entre le roi et la tour
- Le roi n'est pas en échec
- Les cases traversées par le roi ne sont pas attaquées

### `GameStateChecker.java`

```java
public class GameStateChecker {
    private Board board;
    private MoveValidator moveValidator;
    private MoveHistory moveHistory;
    
    public boolean isInCheck(Color color);
    public boolean isCheckmate(Color color);
    public boolean isStalemate(Color color);
    public boolean isDraw();
    public String getGameState();
    
    private boolean hasValidMoves(Color color);
    private boolean isInsufficientMaterial();
    private boolean isFiftyMoveRule();
}
```

**Détection du mat** : Échec + aucun coup légal disponible

**Détection du pat** : Pas en échec + aucun coup légal disponible

---

## Flux de données

### Cycle de jeu typique

```
1. Affichage initial
   ├─> ChessUI lit Board.board[][]
   └─> Affiche les pièces sur la grille

2. Utilisateur clique sur une pièce (e2)
   ├─> ChessUI appelle MoveValidator.getValidMoves(e2)
   ├─> MoveValidator vérifie chaque destination possible
   └─> Retourne [e3, e4] (coups valides)

3. ChessUI surligne les cases valides

4. Utilisateur clique sur destination (e4)
   ├─> ChessUI appelle MoveValidator.isValidMove(e2, e4)
   ├─> Si valide → Board.movePiece(e2, e4)
   ├─> MoveHistory.addMove(e2, e4, pawn)
   └─> Board.switchPlayer()

5. Vérification règles spéciales
   ├─> SpecialMovesHandler.isPromotion() ?
   ├─> Si oui → ChessUI affiche dialogue de choix
   └─> SpecialMovesHandler.executePromotion()

6. Vérification état du jeu
   ├─> GameStateChecker.getGameState()
   ├─> Vérifie échec, mat, pat, nulle
   └─> ChessUI affiche le statut

7. Mise à jour de l'affichage
   └─> Retour à l'étape 1
```

---

## Choix de conception

### 1. Pattern Stratégie pour les pièces

Chaque pièce implémente sa propre logique de mouvement (`isValidMove()`). Cela permet :
- D'ajouter de nouvelles pièces facilement
- De tester chaque pièce indépendamment
- De respecter le principe Ouvert/Fermé

### 2. Séparation validation syntaxique / sémantique

- **Syntaxique** (`Piece.isValidMove()`) : La pièce peut-elle se déplacer ainsi ?
- **Sémantique** (`MoveValidator`) : Ce coup est-il légal dans le contexte du jeu ?

Cela simplifie la logique et améliore la testabilité.

### 3. Historique centralisé

`MoveHistory` centralise toute l'information temporelle :
- Facilite la détection de répétition
- Permet l'implémentation future de l'annulation de coups
- Simplifie les règles dépendantes de l'historique (en passant, 50 coups)

### 4. Immutabilité de Position

`Position` est immuable (pas de setters). Avantages :
- Thread-safety (pas crucial ici, mais bonne pratique)
- Pas d'effets de bord surprenants
- Utilisation sûre comme clé de Map

### 5. Enum pour les types

`Color` et `PieceType` sont des enums plutôt que des constantes :
- Type-safety : impossible de passer un type invalide
- Fonctionnalités Java intégrées (valueOf, values, switch)
- Javadoc automatique

### 6. Découplage UI / Logique

L'UI ne contient **aucune logique métier** :
- Toute validation passe par `MoveValidator`
- Toute modification d'état passe par `Board`
- L'UI ne fait que refléter l'état et transmettre les actions

Cela permet de :
- Tester la logique sans UI
- Remplacer l'UI facilement (console, web, mobile)
- Déboguer les règles indépendamment

---

## Tests et qualité

### Couverture de tests

**Total : 47 tests unitaires**

#### Module `board` (32 tests)
- `BoardTest.java` : 12 tests
  - Initialisation du plateau
  - Déplacement de pièces
  - Capture
  - Recherche de pièces
  - Alternance de joueurs

- `PieceTest.java` : 9 tests
  - Mouvements de chaque type de pièce
  - Validation de trajectoire
  - Détection d'obstruction

- `PositionTest.java` : 11 tests
  - Validité des positions
  - Égalité et hashCode
  - Calculs de distance

#### Module `rules` (15 tests)
- `MoveValidatorTest.java` : 6 tests
  - Validation de coups standards
  - Détection de mise en échec du propre roi
  - Génération de coups valides

- `SpecialMovesHandlerTest.java` : 4 tests
  - Détection et exécution du roque
  - Détection et exécution de l'en passant
  - Détection et exécution de la promotion

- `GameStateCheckerTest.java` : 5 tests
  - Détection d'échec
  - Détection d'échec et mat
  - Détection de pat
  - Détection de nulle (répétition, 50 coups, matériel)

### Stratégie de test

**Tests unitaires** : Chaque classe testée isolément
- Utilisation de l'état réel du `Board` (pas de mock)
- Scénarios de jeu réalistes
- Cas limites et cas d'erreur

**Tests d'intégration** : Via les tests de `GameStateCheckerTest`
- Testent l'interaction entre plusieurs modules
- Simulent des parties complètes

**Exécution** :
```bash
mvn test
# 47 tests exécutés, 0 échecs, ~1 seconde
```

### Qualité du code

- **Nomenclature** : CamelCase Java standard
- **Commentaires** : Javadoc sur toutes les classes publiques
- **Longueur des méthodes** : Moyenne 15 lignes, max 50 lignes
- **Complexité cyclomatique** : Maintenue basse par décomposition

---

## Build et déploiement

### Structure Maven

```xml
<project>
  <groupId>com.chess</groupId>
  <artifactId>chess-game</artifactId>
  <version>1.0.0</version>
  
  <properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
  </properties>
  
  <dependencies>
    <dependency>
      <groupId>org.junit.jupiter</groupId>
      <artifactId>junit-jupiter-engine</artifactId>
      <version>5.10.0</version>
      <scope>test</scope>
    </dependency>
  </dependencies>
</project>
```

### Processus de build

1. **Compilation**
   ```bash
   mvn clean compile
   # Compile src/main/java → target/classes
   ```

2. **Tests**
   ```bash
   mvn test
   # Compile et exécute tous les tests
   ```

3. **Packaging**
   ```bash
   mvn package
   # Crée target/chess-game-1.0.0.jar avec MANIFEST
   ```

### JAR exécutable

Le plugin `maven-jar-plugin` configure :
- **Main-Class** : `com.chess.ChessGame`
- **Class-Path** : Ajouté automatiquement

Le JAR généré peut être exécuté directement :
```bash
java -jar chess-game.jar
```

### Scripts de lancement

**Linux/Mac** (`play.sh`) :
- Configure `DISPLAY` pour X11
- Utilise Java 17 si disponible
- Lance le JAR

**Windows** (`play.bat`) :
- Lance le JAR avec la JVM par défaut
- Gère les erreurs

### Prérequis techniques

- **JDK 17+** : Utilisation de fonctionnalités Java 17 (records, pattern matching prévu)
- **Maven 3.6+** : Pour le build
- **Environnement graphique** :
  - Linux : X11 ou Wayland avec Xwayland
  - Windows : Natif
  - Mac : Natif

---

## Organisation Git

### Branches

- `main` : Version stable, prête pour la production
- `develop` : Intégration continue des fonctionnalités
- `board` : Développement du module board (mergée)
- `rules` : Développement du module rules (mergée)
- `ui` : Développement de l'interface (mergée)

### Workflow

1. Développement sur branche feature (`board`, `rules`, `ui`)
2. Tests locaux (`mvn test`)
3. Commit avec messages descriptifs
4. Merge dans `develop` via pull request (ou fast-forward)
5. Tests d'intégration sur `develop`
6. Merge dans `main` pour release

### Convention de commits

- `feat:` Nouvelle fonctionnalité
- `fix:` Correction de bug
- `docs:` Documentation
- `test:` Ajout/modification de tests
- `refactor:` Refactoring sans changement fonctionnel
- `chore:` Tâches techniques (build, config)

Exemple :
```
feat: ajout de la détection d'échec et mat

Implémente GameStateChecker.isCheckmate() qui vérifie
si un joueur est en échec sans aucun coup légal disponible.
Ajoute les tests correspondants.
```

---

## Évolutions futures possibles

### Fonctionnalités

- **Sauvegarde/Chargement** : Sérialisation de parties en PGN (Portable Game Notation)
- **IA** : Implémentation d'un adversaire artificiel (Minimax, Alpha-Beta)
- **Timer** : Gestion du temps de réflexion (blitz, rapide, classique)
- **Notation algébrique** : Affichage des coups en notation échecs standard
- **Analyse** : Suggestions de coups, évaluation de position

### Technique

- **Base de données** : Persistance des parties
- **Réseau** : Jeu en ligne entre deux joueurs distants
- **Mobile** : Port Android/iOS
- **Web** : Interface web avec WebSocket

### Architecture

- **Event Sourcing** : Reconstruire l'état à partir de l'historique
- **CQRS** : Séparer lecture et écriture pour la performance
- **Observateur** : Pattern pour notifier l'UI des changements d'état

---

## Conclusion

Ce projet implémente un jeu d'échecs complet et conforme aux règles officielles, avec une architecture propre et testée. La séparation en modules permet une évolutivité et une maintenabilité optimales.

**Points forts** :
- ✅ Architecture claire en 3 couches
- ✅ Séparation des responsabilités
- ✅ 47 tests unitaires (100% de réussite)
- ✅ Toutes les règles implémentées (y compris règles spéciales)
- ✅ Code lisible et documenté
- ✅ Build automatisé avec Maven
- ✅ JAR exécutable prêt pour la distribution

**Compétences démontrées** :
- Conception orientée objet
- Architecture logicielle
- Tests automatisés
- Gestion de projet avec Git
- Build et déploiement avec Maven
- Interface graphique Swing

---

**Auteurs** : Projet de Qualité de Développement - IUT S5  
**Date** : Octobre 2025  
**Version** : 1.0.0

