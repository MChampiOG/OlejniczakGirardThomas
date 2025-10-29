package com.chess.rules;

import com.chess.board.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe SpecialMovesHandler.
 */
@DisplayName("Tests pour la classe SpecialMovesHandler")
class SpecialMovesHandlerTest {
    
    private Board board;
    private MoveHistory moveHistory;
    private SpecialMovesHandler specialMovesHandler;
    
    @BeforeEach
    void setUp() {
        board = new Board();
        moveHistory = new MoveHistory();
        specialMovesHandler = new SpecialMovesHandler(board, moveHistory);
    }
    
    @Test
    @DisplayName("Détection d'un roque côté roi")
    void testKingsideCastling() {
        board = new Board();
        
        // Placer un roi blanc qui n'a pas bougé
        King whiteKing = new King(Color.WHITE, new Position(0, 4));
        board.setPieceAt(new Position(0, 4), whiteKing);
        
        // Placer une tour côté roi qui n'a pas bougé
        Rook whiteRook = new Rook(Color.WHITE, new Position(0, 7));
        board.setPieceAt(new Position(0, 7), whiteRook);
        
        specialMovesHandler = new SpecialMovesHandler(board, moveHistory);
        
        // Vérifier que le roque côté roi est détecté
        Position from = whiteKing.getPosition();
        Position to = new Position(0, 6); // Position du roi après roque côté roi
        
        assertTrue(specialMovesHandler.isCastling(from, to));
    }
    
    @Test
    @DisplayName("Détection d'une promotion de pion")
    void testPawnPromotion() {
        board = new Board();
        
        // Placer un pion blanc sur la 7ème rangée (prêt à être promu)
        Pawn whitePawn = new Pawn(Color.WHITE, new Position(6, 0));
        board.setPieceAt(new Position(6, 0), whitePawn);
        
        specialMovesHandler = new SpecialMovesHandler(board, moveHistory);
        
        Position from = whitePawn.getPosition();
        Position to = new Position(7, 0); // Dernière rangée
        
        assertTrue(specialMovesHandler.isPromotion(from, to));
    }
    
    @Test
    @DisplayName("Exécution d'une promotion de pion")
    void testExecutePromotion() {
        board = new Board();
        
        // Placer un pion blanc sur la 7ème rangée
        Pawn whitePawn = new Pawn(Color.WHITE, new Position(6, 0));
        board.setPieceAt(new Position(6, 0), whitePawn);
        
        specialMovesHandler = new SpecialMovesHandler(board, moveHistory);
        
        Position from = whitePawn.getPosition();
        Position to = new Position(7, 0);
        
        // Exécuter la promotion en dame
        boolean success = specialMovesHandler.executeSpecialMove(from, to, PieceType.QUEEN);
        
        assertTrue(success);
        Piece promotedPiece = board.getPieceAt(to);
        assertNotNull(promotedPiece);
        assertTrue(promotedPiece instanceof Queen);
        assertEquals(Color.WHITE, promotedPiece.getColor());
    }
    
    @Test
    @DisplayName("Détection d'une prise en passant")
    void testEnPassant() {
        board = new Board();
        
        // Placer un pion blanc sur la 4ème rangée
        Pawn whitePawn = new Pawn(Color.WHITE, new Position(4, 3));
        board.setPieceAt(new Position(4, 3), whitePawn);
        
        // Simuler un mouvement double d'un pion noir dans l'historique
        Pawn blackPawn = new Pawn(Color.BLACK, new Position(6, 4));
        board.setPieceAt(new Position(6, 4), blackPawn);
        
        // Simuler le mouvement double du pion noir
        Position blackFrom = new Position(6, 4);
        Position blackTo = new Position(4, 4);
        moveHistory.addMove(blackFrom, blackTo, blackPawn, null, board);
        
        specialMovesHandler = new SpecialMovesHandler(board, moveHistory);
        
        Position from = whitePawn.getPosition();
        Position to = new Position(5, 4); // Position pour la prise en passant
        
        // Vérifier que la prise en passant est détectée
        assertTrue(specialMovesHandler.isEnPassant(from, to));
    }
}
