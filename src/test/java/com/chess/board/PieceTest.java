package com.chess.board;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour les classes de pièces.
 */
@DisplayName("Tests pour les classes de pièces")
class PieceTest {
    
    private Board board;
    
    @BeforeEach
    void setUp() {
        board = new Board();
        board.initializeBoard();
    }
    
    @Test
    @DisplayName("Test du pion blanc")
    void testWhitePawn() {
        Pawn pawn = new Pawn(Color.WHITE, new Position(1, 0));
        
        // Test des propriétés de base
        assertEquals(PieceType.PAWN, pawn.getType());
        assertEquals(Color.WHITE, pawn.getColor());
        assertEquals(1, pawn.getValue());
        assertEquals("P", pawn.getSymbol());
        assertEquals("Blanc Pion", pawn.getFullName());
        
        // Test des mouvements possibles
        Position[] moves = pawn.getPossibleMoves(board);
        assertEquals(2, moves.length); // 1 case vers l'avant + 2 cases vers l'avant
        
        // Test de la promotion
        assertFalse(pawn.canBePromoted());
        pawn.setPosition(new Position(7, 0));
        assertTrue(pawn.canBePromoted());
    }
    
    @Test
    @DisplayName("Test du roi blanc")
    void testWhiteKing() {
        King king = new King(Color.WHITE, new Position(0, 4));
        
        // Test des propriétés de base
        assertEquals(PieceType.KING, king.getType());
        assertEquals(Color.WHITE, king.getColor());
        assertEquals(0, king.getValue());
        assertEquals("K", king.getSymbol());
        assertEquals("Blanc Roi", king.getFullName());
        
        // Test des mouvements possibles
        Position[] moves = king.getPossibleMoves(board);
        assertEquals(0, moves.length); // Aucun mouvement possible en position initiale
        
        // Test du roque
        assertFalse(king.canCastle(board));
    }
    
    @Test
    @DisplayName("Test de la dame blanche")
    void testWhiteQueen() {
        Queen queen = new Queen(Color.WHITE, new Position(0, 3));
        
        // Test des propriétés de base
        assertEquals(PieceType.QUEEN, queen.getType());
        assertEquals(Color.WHITE, queen.getColor());
        assertEquals(9, queen.getValue());
        assertEquals("Q", queen.getSymbol());
        assertEquals("Blanc Dame", queen.getFullName());
        
        // Test des mouvements possibles
        Position[] moves = queen.getPossibleMoves(board);
        assertEquals(0, moves.length); // Aucun mouvement possible en position initiale
    }
    
    @Test
    @DisplayName("Test de la tour blanche")
    void testWhiteRook() {
        Rook rook = new Rook(Color.WHITE, new Position(0, 0));
        
        // Test des propriétés de base
        assertEquals(PieceType.ROOK, rook.getType());
        assertEquals(Color.WHITE, rook.getColor());
        assertEquals(5, rook.getValue());
        assertEquals("R", rook.getSymbol());
        assertEquals("Blanc Tour", rook.getFullName());
        
        // Test des mouvements possibles
        Position[] moves = rook.getPossibleMoves(board);
        assertEquals(0, moves.length); // Aucun mouvement possible en position initiale
    }
    
    @Test
    @DisplayName("Test du fou blanc")
    void testWhiteBishop() {
        Bishop bishop = new Bishop(Color.WHITE, new Position(0, 2));
        
        // Test des propriétés de base
        assertEquals(PieceType.BISHOP, bishop.getType());
        assertEquals(Color.WHITE, bishop.getColor());
        assertEquals(3, bishop.getValue());
        assertEquals("B", bishop.getSymbol());
        assertEquals("Blanc Fou", bishop.getFullName());
        
        // Test des mouvements possibles
        Position[] moves = bishop.getPossibleMoves(board);
        assertEquals(0, moves.length); // Aucun mouvement possible en position initiale
    }
    
    @Test
    @DisplayName("Test du cavalier blanc")
    void testWhiteKnight() {
        Knight knight = new Knight(Color.WHITE, new Position(0, 1));
        
        // Test des propriétés de base
        assertEquals(PieceType.KNIGHT, knight.getType());
        assertEquals(Color.WHITE, knight.getColor());
        assertEquals(3, knight.getValue());
        assertEquals("N", knight.getSymbol());
        assertEquals("Blanc Cavalier", knight.getFullName());
        
        // Test des mouvements possibles
        Position[] moves = knight.getPossibleMoves(board);
        assertEquals(2, moves.length); // 2 mouvements possibles en position initiale
    }
    
    @Test
    @DisplayName("Test des couleurs opposées")
    void testOppositeColors() {
        Pawn whitePawn = new Pawn(Color.WHITE, new Position(0, 0));
        Pawn blackPawn = new Pawn(Color.BLACK, new Position(0, 0));
        
        assertTrue(whitePawn.isOppositeColor(blackPawn));
        assertTrue(blackPawn.isOppositeColor(whitePawn));
        assertFalse(whitePawn.isSameColor(blackPawn));
        assertFalse(blackPawn.isSameColor(whitePawn));
    }
    
    @Test
    @DisplayName("Test du marquage de mouvement")
    void testMovedFlag() {
        Pawn pawn = new Pawn(Color.WHITE, new Position(1, 0));
        
        assertFalse(pawn.hasMoved());
        pawn.markAsMoved();
        assertTrue(pawn.hasMoved());
    }
    
    @Test
    @DisplayName("Test de l'égalité des pièces")
    void testPieceEquality() {
        Pawn pawn1 = new Pawn(Color.WHITE, new Position(1, 0));
        Pawn pawn2 = new Pawn(Color.WHITE, new Position(1, 0));
        Pawn pawn3 = new Pawn(Color.BLACK, new Position(1, 0));
        
        assertEquals(pawn1, pawn2);
        assertNotEquals(pawn1, pawn3);
        assertEquals(pawn1.hashCode(), pawn2.hashCode());
    }
}
