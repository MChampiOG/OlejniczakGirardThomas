package com.chess.rules;

import com.chess.board.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe MoveValidator.
 */
@DisplayName("Tests pour la classe MoveValidator")
class MoveValidatorTest {
    
    private Board board;
    private MoveHistory moveHistory;
    private MoveValidator moveValidator;
    
    @BeforeEach
    void setUp() {
        board = new Board();
        board.initializeBoard();
        moveHistory = new MoveHistory();
        moveValidator = new MoveValidator(board, moveHistory);
    }
    
    @Test
    @DisplayName("Validation d'un coup de pion valide")
    void testValidPawnMove() {
        Position from = new Position(1, 0); // Pion blanc
        Position to = new Position(2, 0);
        
        assertTrue(moveValidator.isValidMove(from, to));
    }
    
    @Test
    @DisplayName("Rejet d'un coup de pion invalide")
    void testInvalidPawnMove() {
        Position from = new Position(1, 0); // Pion blanc
        Position to = new Position(3, 0); // Deux cases en avant sans avoir bougé
        
        // Le pion ne peut pas faire deux cases s'il a déjà bougé
        board.movePiece(from, new Position(2, 0)); // Premier mouvement
        board.switchPlayer(); // Retour au joueur blanc
        
        assertFalse(moveValidator.isValidMove(from, to));
    }
    
    @Test
    @DisplayName("Rejet d'un coup qui met le roi en échec")
    void testMoveThatPutsKingInCheck() {
        // Créer un plateau personnalisé pour tester l'échec
        board = new Board();
        
        // Placer un roi blanc
        King whiteKing = new King(Color.WHITE, new Position(4, 4));
        board.setPieceAt(new Position(4, 4), whiteKing);
        
        // Placer une tour noire qui attaque le roi
        Rook blackRook = new Rook(Color.BLACK, new Position(4, 7));
        board.setPieceAt(new Position(4, 7), blackRook);
        
        // Le roi ne peut pas se déplacer vers une position en échec
        Position kingNewPos = new Position(4, 5);
        assertFalse(moveValidator.isValidMove(whiteKing.getPosition(), kingNewPos));
    }
    
    @Test
    @DisplayName("Rejet d'un coup avec une pièce de la mauvaise couleur")
    void testMoveWithWrongColor() {
        Position from = new Position(6, 0); // Pion noir
        Position to = new Position(5, 0);
        
        // C'est le tour des blancs
        assertFalse(moveValidator.isValidMove(from, to));
    }
    
    @Test
    @DisplayName("Récupération des coups valides pour une pièce")
    void testGetValidMoves() {
        Position pawnPosition = new Position(1, 0); // Pion blanc
        Position[] validMoves = moveValidator.getValidMoves(pawnPosition);
        
        assertTrue(validMoves.length > 0);
        // Le pion devrait pouvoir avancer d'une case
        assertTrue(java.util.Arrays.asList(validMoves).contains(new Position(2, 0)));
    }
    
    @Test
    @DisplayName("Validation d'un coup de capture")
    void testCaptureMove() {
        // Créer un scénario de capture
        board = new Board();
        
        // Placer un pion blanc
        Pawn whitePawn = new Pawn(Color.WHITE, new Position(3, 3));
        board.setPieceAt(new Position(3, 3), whitePawn);
        
        // Placer un pion noir à capturer
        Pawn blackPawn = new Pawn(Color.BLACK, new Position(4, 4));
        board.setPieceAt(new Position(4, 4), blackPawn);
        
        moveValidator = new MoveValidator(board, moveHistory);
        
        // Le pion blanc devrait pouvoir capturer le pion noir
        assertTrue(moveValidator.isValidMove(new Position(3, 3), new Position(4, 4)));
    }
}
