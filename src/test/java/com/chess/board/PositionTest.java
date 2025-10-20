package com.chess.board;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour la classe Position.
 */
@DisplayName("Tests pour la classe Position")
class PositionTest {
    
    @Test
    @DisplayName("Création d'une position valide")
    void testValidPositionCreation() {
        Position position = new Position(0, 0);
        assertEquals(0, position.getRow());
        assertEquals(0, position.getColumn());
        assertTrue(position.isValid());
    }
    
    @Test
    @DisplayName("Création d'une position invalide doit lever une exception")
    void testInvalidPositionCreation() {
        assertThrows(IllegalArgumentException.class, () -> new Position(-1, 0));
        assertThrows(IllegalArgumentException.class, () -> new Position(0, -1));
        assertThrows(IllegalArgumentException.class, () -> new Position(8, 0));
        assertThrows(IllegalArgumentException.class, () -> new Position(0, 8));
    }
    
    @Test
    @DisplayName("Vérification de la validité des positions")
    void testPositionValidity() {
        assertTrue(Position.isValid(0, 0));
        assertTrue(Position.isValid(7, 7));
        assertTrue(Position.isValid(3, 4));
        
        assertFalse(Position.isValid(-1, 0));
        assertFalse(Position.isValid(0, -1));
        assertFalse(Position.isValid(8, 0));
        assertFalse(Position.isValid(0, 8));
    }
    
    @Test
    @DisplayName("Calcul des distances entre positions")
    void testDistanceCalculation() {
        Position pos1 = new Position(2, 3);
        Position pos2 = new Position(5, 7);
        
        assertEquals(3, pos1.getRowDistance(pos2));
        assertEquals(4, pos1.getColumnDistance(pos2));
    }
    
    @Test
    @DisplayName("Vérification des lignes et colonnes identiques")
    void testSameRowAndColumn() {
        Position pos1 = new Position(2, 3);
        Position pos2 = new Position(2, 7);
        Position pos3 = new Position(5, 3);
        Position pos4 = new Position(5, 7);
        
        assertTrue(pos1.isSameRow(pos2));
        assertFalse(pos1.isSameRow(pos3));
        
        assertTrue(pos1.isSameColumn(pos3));
        assertFalse(pos1.isSameColumn(pos2));
    }
    
    @Test
    @DisplayName("Vérification des diagonales")
    void testDiagonal() {
        Position pos1 = new Position(2, 2);
        Position pos2 = new Position(5, 5);
        Position pos3 = new Position(2, 5);
        
        assertTrue(pos1.isSameDiagonal(pos2));
        assertFalse(pos1.isSameDiagonal(pos3));
    }
    
    @Test
    @DisplayName("Ajout de décalages à une position")
    void testAddOffset() {
        Position pos = new Position(3, 4);
        
        Position newPos1 = pos.add(1, 1);
        assertNotNull(newPos1);
        assertEquals(4, newPos1.getRow());
        assertEquals(5, newPos1.getColumn());
        
        Position newPos2 = pos.add(-1, -1);
        assertNotNull(newPos2);
        assertEquals(2, newPos2.getRow());
        assertEquals(3, newPos2.getColumn());
        
        Position invalidPos = pos.add(-5, 0);
        assertNull(invalidPos);
    }
    
    @Test
    @DisplayName("Égalité et hashcode")
    void testEqualsAndHashCode() {
        Position pos1 = new Position(3, 4);
        Position pos2 = new Position(3, 4);
        Position pos3 = new Position(3, 5);
        
        assertEquals(pos1, pos2);
        assertNotEquals(pos1, pos3);
        assertEquals(pos1.hashCode(), pos2.hashCode());
    }
    
    @Test
    @DisplayName("Conversion en notation algébrique")
    void testAlgebraicNotation() {
        Position pos1 = new Position(7, 0); // a1
        Position pos2 = new Position(0, 7); // h8
        Position pos3 = new Position(3, 4); // e5
        
        assertEquals("a1", pos1.toAlgebraicNotation());
        assertEquals("h8", pos2.toAlgebraicNotation());
        assertEquals("e5", pos3.toAlgebraicNotation());
    }
    
    @Test
    @DisplayName("Création depuis la notation algébrique")
    void testFromAlgebraicNotation() {
        Position pos1 = Position.fromAlgebraicNotation("a1");
        assertEquals(7, pos1.getRow());
        assertEquals(0, pos1.getColumn());
        
        Position pos2 = Position.fromAlgebraicNotation("h8");
        assertEquals(0, pos2.getRow());
        assertEquals(7, pos2.getColumn());
        
        Position pos3 = Position.fromAlgebraicNotation("e5");
        assertEquals(3, pos3.getRow());
        assertEquals(4, pos3.getColumn());
    }
    
    @Test
    @DisplayName("Notation algébrique invalide doit lever une exception")
    void testInvalidAlgebraicNotation() {
        assertThrows(IllegalArgumentException.class, () -> Position.fromAlgebraicNotation("i1"));
        assertThrows(IllegalArgumentException.class, () -> Position.fromAlgebraicNotation("a9"));
        assertThrows(IllegalArgumentException.class, () -> Position.fromAlgebraicNotation("abc"));
        assertThrows(IllegalArgumentException.class, () -> Position.fromAlgebraicNotation(""));
        assertThrows(IllegalArgumentException.class, () -> Position.fromAlgebraicNotation(null));
    }
}
