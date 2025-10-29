# Jeu d'Échecs en Java

Application de jeu d'échecs complète avec interface graphique développée en Java 17.

## Fonctionnalités

- **Plateau d'échecs complet** avec toutes les pièces
- **Validation des coups** selon les règles officielles
- **Règles spéciales** : roque, prise en passant, promotion
- **Détection de l'état du jeu** : échec, échec et mat, pat, nulle
- **Interface graphique** intuitive avec Swing
- **47 tests unitaires** (100% de réussite)

## Lancement rapide

### Option 1 : Script simple (recommandé)

```bash
./play.sh     # Sur Linux/Mac
play.bat      # Sur Windows
```

### Option 2 : Utiliser le JAR exécutable

```bash
java -jar chess-game.jar
```

**Note** : Sur Linux, si vous avez des problèmes graphiques, utilisez Java 17 :
```bash
/usr/lib/jvm/java-17-openjdk-amd64/bin/java -jar chess-game.jar
```

### Option 3 : Compiler et lancer avec Maven

```bash
mvn clean compile
mvn exec:java -Dexec.mainClass="com.chess.ChessGame"
```

## Prérequis

- **Java 17** ou supérieur
- **Maven 3.6+** (pour la compilation)
- Environnement graphique (X11 sur Linux, natif sur Windows/Mac)

## Tests

Exécuter tous les tests :

```bash
mvn test
```

Les tests couvrent :
- Validation des mouvements des pièces
- Règles spéciales (roque, en passant, promotion)
- Détection d'échec, mat et pat
- Logique du plateau

## Structure du projet

```
src/
├── main/java/com/chess/
│   ├── ChessGame.java              # Point d'entrée
│   ├── board/                      # Logique du plateau et des pièces
│   │   ├── Board.java
│   │   ├── Piece.java
│   │   ├── Position.java
│   │   └── [King, Queen, Rook, Bishop, Knight, Pawn].java
│   ├── rules/                      # Moteur de règles
│   │   ├── MoveValidator.java
│   │   ├── SpecialMovesHandler.java
│   │   ├── GameStateChecker.java
│   │   └── MoveHistory.java
│   └── ui/                         # Interface graphique
│       └── ChessUI.java
└── test/java/com/chess/            # Tests unitaires
```

## Comment jouer

1. Lancez l'application
2. Cliquez sur une pièce pour la sélectionner
3. Cliquez sur une case de destination pour déplacer la pièce
4. Les coups invalides sont rejetés automatiquement
5. Le statut du jeu s'affiche en haut (tour du joueur, échec, mat, etc.)

## Règles implémentées

- ✅ Mouvements standards de toutes les pièces
- ✅ Roque (petit et grand)
- ✅ Prise en passant
- ✅ Promotion du pion (choix de la pièce)
- ✅ Détection d'échec
- ✅ Détection d'échec et mat
- ✅ Détection de pat
- ✅ Nulle par répétition triple
- ✅ Règle des 50 coups
- ✅ Matériel insuffisant

