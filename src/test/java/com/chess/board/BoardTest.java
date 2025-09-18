package com.chess.board;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Board.
 */
@DisplayName("Tests pour la classe Board")
class BoardTest {
    
    private Board board;
    
    @BeforeEach
    void setUp() {
        board = new Board();
        board.initializeBoard();
    }
    
    @Test
    @DisplayName("Initialisation du plateau")
    void testBoardInitialization() {
        // Vérifier que le plateau est initialisé
        assertNotNull(board);
        
        // Vérifier que les pions blancs sont en position
        for (int col = 0; col < 8; col++) {
            Piece piece = board.getPieceAt(new Position(1, col));
            assertNotNull(piece);
            assertTrue(piece instanceof Pawn);
            assertEquals(Color.WHITE, piece.getColor());
        }
        
        // Vérifier que les pions noirs sont en position
        for (int col = 0; col < 8; col++) {
            Piece piece = board.getPieceAt(new Position(6, col));
            assertNotNull(piece);
            assertTrue(piece instanceof Pawn);
            assertEquals(Color.BLACK, piece.getColor());
        }
        
        // Vérifier que les pièces blanches sont en position
        assertTrue(board.getPieceAt(new Position(0, 0)) instanceof Rook);
        assertTrue(board.getPieceAt(new Position(0, 1)) instanceof Knight);
        assertTrue(board.getPieceAt(new Position(0, 2)) instanceof Bishop);
        assertTrue(board.getPieceAt(new Position(0, 3)) instanceof Queen);
        assertTrue(board.getPieceAt(new Position(0, 4)) instanceof King);
        
        // Vérifier que les pièces noires sont en position
        assertTrue(board.getPieceAt(new Position(7, 0)) instanceof Rook);
        assertTrue(board.getPieceAt(new Position(7, 1)) instanceof Knight);
        assertTrue(board.getPieceAt(new Position(7, 2)) instanceof Bishop);
        assertTrue(board.getPieceAt(new Position(7, 3)) instanceof Queen);
        assertTrue(board.getPieceAt(new Position(7, 4)) instanceof King);
    }
    
    @Test
    @DisplayName("Joueur actuel")
    void testCurrentPlayer() {
        assertEquals(Color.WHITE, board.getCurrentPlayer());
        
        board.switchPlayer();
        assertEquals(Color.BLACK, board.getCurrentPlayer());
        
        board.switchPlayer();
        assertEquals(Color.WHITE, board.getCurrentPlayer());
    }
    
    @Test
    @DisplayName("Déplacement de pièce valide")
    void testValidMove() {
        Position from = new Position(1, 0); // Pion blanc
        Position to = new Position(2, 0);
        
        assertTrue(board.movePiece(from, to));
        assertNull(board.getPieceAt(from));
        assertNotNull(board.getPieceAt(to));
        assertEquals(Color.BLACK, board.getCurrentPlayer()); // Le joueur doit changer
    }
    
    @Test
    @DisplayName("Déplacement de pièce invalide")
    void testInvalidMove() {
        Position from = new Position(1, 0); // Pion blanc
        Position to = new Position(3, 0); // Déplacement invalide (2 cases sans être en position de départ)
        
        assertFalse(board.movePiece(from, to));
        assertNotNull(board.getPieceAt(from));
        assertNull(board.getPieceAt(to));
        assertEquals(Color.WHITE, board.getCurrentPlayer()); // Le joueur ne doit pas changer
    }
    
    @Test
    @DisplayName("Déplacement avec capture")
    void testMoveWithCapture() {
        // Déplacer un pion blanc
        board.movePiece(new Position(1, 0), new Position(2, 0));
        board.switchPlayer();
        
        // Déplacer un pion noir pour permettre la capture
        board.movePiece(new Position(6, 1), new Position(4, 1));
        board.switchPlayer();
        
        // Capturer le pion noir
        Position from = new Position(2, 0);
        Position to = new Position(4, 1);
        
        assertTrue(board.movePiece(from, to));
        assertNull(board.getPieceAt(from));
        assertNotNull(board.getPieceAt(to));
        assertEquals(1, board.getCapturedPieces().size());
    }
    
    @Test
    @DisplayName("Vérification des cases vides")
    void testEmptySquares() {
        assertTrue(board.isEmpty(new Position(2, 0)));
        assertFalse(board.isEmpty(new Position(1, 0)));
    }
    
    @Test
    @DisplayName("Vérification des pièces par couleur")
    void testPiecesOfColor() {
        var whitePieces = board.getPiecesOfColor(Color.WHITE);
        var blackPieces = board.getPiecesOfColor(Color.BLACK);
        
        assertEquals(16, whitePieces.size());
        assertEquals(16, blackPieces.size());
        
        // Vérifier que toutes les pièces blanches sont bien blanches
        for (Piece piece : whitePieces) {
            assertEquals(Color.WHITE, piece.getColor());
        }
        
        // Vérifier que toutes les pièces noires sont bien noires
        for (Piece piece : blackPieces) {
            assertEquals(Color.BLACK, piece.getColor());
        }
    }
    
    @Test
    @DisplayName("Recherche du roi")
    void testFindKing() {
        King whiteKing = board.findKing(Color.WHITE);
        King blackKing = board.findKing(Color.BLACK);
        
        assertNotNull(whiteKing);
        assertNotNull(blackKing);
        assertEquals(Color.WHITE, whiteKing.getColor());
        assertEquals(Color.BLACK, blackKing.getColor());
        assertEquals(new Position(0, 4), whiteKing.getPosition());
        assertEquals(new Position(7, 4), blackKing.getPosition());
    }
    
    @Test
    @DisplayName("Vérification de l'échec")
    void testCheck() {
        // Dans la position initiale, aucun roi n'est en échec
        assertFalse(board.isInCheck(Color.WHITE));
        assertFalse(board.isInCheck(Color.BLACK));
    }
    
    @Test
    @DisplayName("Représentation textuelle du plateau")
    void testToString() {
        String boardString = board.toString();
        assertNotNull(boardString);
        assertTrue(boardString.contains("a b c d e f g h"));
        assertTrue(boardString.contains("8"));
        assertTrue(boardString.contains("1"));
    }
    
    @Test
    @DisplayName("Taille du plateau")
    void testBoardSize() {
        assertEquals(8, Board.getBoardSize());
    }
    
    @Test
    @DisplayName("Position invalide")
    void testInvalidPosition() {
        assertNull(board.getPieceAt(new Position(-1, 0)));
        assertNull(board.getPieceAt(new Position(0, -1)));
        assertNull(board.getPieceAt(new Position(8, 0)));
        assertNull(board.getPieceAt(new Position(0, 8)));
        assertNull(board.getPieceAt(null));
    }
}
