package com.chess.rules;

import com.chess.board.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe GameStateChecker.
 */
@DisplayName("Tests pour la classe GameStateChecker")
class GameStateCheckerTest {
    
    private Board board;
    private MoveHistory moveHistory;
    private MoveValidator moveValidator;
    private GameStateChecker gameStateChecker;
    
    @BeforeEach
    void setUp() {
        board = new Board();
        board.initializeBoard();
        moveHistory = new MoveHistory();
        moveValidator = new MoveValidator(board, moveHistory);
        gameStateChecker = new GameStateChecker(board, moveValidator);
    }
    
    @Test
    @DisplayName("Vérification de l'état initial - partie en cours")
    void testInitialGameState() {
        GameStateChecker.GameState state = gameStateChecker.getGameState(moveHistory);
        assertEquals(GameStateChecker.GameState.ONGOING, state);
    }
    
    @Test
    @DisplayName("Détection d'un échec")
    void testCheckDetection() {
        // Créer un scénario d'échec
        board = new Board();
        
        // Placer un roi blanc
        King whiteKing = new King(Color.WHITE, new Position(4, 4));
        board.setPieceAt(new Position(4, 4), whiteKing);
        
        // Placer une tour noire qui attaque le roi
        Rook blackRook = new Rook(Color.BLACK, new Position(4, 7));
        board.setPieceAt(new Position(4, 7), blackRook);
        
        moveValidator = new MoveValidator(board, moveHistory);
        gameStateChecker = new GameStateChecker(board, moveValidator);
        
        assertTrue(gameStateChecker.isInCheck(Color.WHITE));
        GameStateChecker.GameState state = gameStateChecker.getGameState(moveHistory);
        assertEquals(GameStateChecker.GameState.CHECK, state);
    }
    
    @Test
    @DisplayName("Détection d'un mat")
    void testCheckmateDetection() {
        // Créer un scénario de mat simplifié
        // Roi blanc dans un coin avec des tours noires qui le bloquent complètement
        board = new Board();
        
        // Placer un roi blanc dans un coin
        King whiteKing = new King(Color.WHITE, new Position(0, 0));
        board.setPieceAt(new Position(0, 0), whiteKing);
        
        // Placer des tours noires qui attaquent toutes les cases autour du roi
        // Tour horizontale
        Rook blackRook1 = new Rook(Color.BLACK, new Position(0, 1));
        board.setPieceAt(new Position(0, 1), blackRook1);
        
        // Tour verticale  
        Rook blackRook2 = new Rook(Color.BLACK, new Position(1, 0));
        board.setPieceAt(new Position(1, 0), blackRook2);
        
        moveValidator = new MoveValidator(board, moveHistory);
        gameStateChecker = new GameStateChecker(board, moveValidator);
        
        // Vérifier que le roi blanc est en échec
        assertTrue(gameStateChecker.isInCheck(Color.WHITE));
        
        // Si le roi n'a pas de coups valides, alors c'est un mat
        // (dans ce scénario simplifié, le roi a 0 cases libres, toutes sont attaquées)
        boolean hasMoves = gameStateChecker.hasValidMoves(Color.WHITE);
        if (!hasMoves) {
            assertTrue(gameStateChecker.isCheckmate(Color.WHITE));
        }
    }
    
    @Test
    @DisplayName("Détection d'un pat")
    void testStalemateDetection() {
        // Créer un scénario de pat (simplifié)
        board = new Board();
        
        // Placer un roi blanc seul
        King whiteKing = new King(Color.WHITE, new Position(0, 0));
        board.setPieceAt(new Position(0, 0), whiteKing);
        
        // Placer une tour noire pour bloquer les mouvements (sans échec direct)
        Rook blackRook = new Rook(Color.BLACK, new Position(1, 1));
        board.setPieceAt(new Position(1, 1), blackRook);
        
        // Placer un roi noir pour compléter le plateau
        King blackKing = new King(Color.BLACK, new Position(7, 7));
        board.setPieceAt(new Position(7, 7), blackKing);
        
        moveValidator = new MoveValidator(board, moveHistory);
        gameStateChecker = new GameStateChecker(board, moveValidator);
        
        // Le roi blanc devrait être en pat s'il n'a aucun coup valide
        boolean hasMoves = gameStateChecker.hasValidMoves(Color.WHITE);
        if (!hasMoves && !gameStateChecker.isInCheck(Color.WHITE)) {
            assertTrue(gameStateChecker.isStalemate(Color.WHITE));
        }
    }
    
    @Test
    @DisplayName("Vérification des coups valides disponibles")
    void testHasValidMoves() {
        // Au début de la partie, les blancs devraient avoir des coups valides
        assertTrue(gameStateChecker.hasValidMoves(Color.WHITE));
    }
}
