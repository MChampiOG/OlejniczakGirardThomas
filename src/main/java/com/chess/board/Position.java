package com.chess.board;

/**
 * Représente une position sur le plateau d'échecs.
 * Les coordonnées vont de 0 à 7 (0,0 étant en haut à gauche).
 */
public class Position {
    private final int row;
    private final int column;
    
    /**
     * Constructeur pour créer une position.
     * @param row la ligne (0-7)
     * @param column la colonne (0-7)
     * @throws IllegalArgumentException si les coordonnées sont invalides
     */
    public Position(int row, int column) {
        if (!isValid(row, column)) {
            throw new IllegalArgumentException("Position invalide : (" + row + ", " + column + ")");
        }
        this.row = row;
        this.column = column;
    }
    
    /**
     * Vérifie si les coordonnées sont valides sur un plateau 8x8.
     * @param row la ligne
     * @param column la colonne
     * @return true si la position est valide
     */
    public static boolean isValid(int row, int column) {
        return row >= 0 && row < 8 && column >= 0 && column < 8;
    }
    
    /**
     * Vérifie si cette position est valide.
     * @return true si la position est valide
     */
    public boolean isValid() {
        return isValid(row, column);
    }
    
    /**
     * Retourne la ligne.
     * @return la ligne (0-7)
     */
    public int getRow() {
        return row;
    }
    
    /**
     * Retourne la colonne.
     * @return la colonne (0-7)
     */
    public int getColumn() {
        return column;
    }
    
    /**
     * Calcule la distance en ligne entre cette position et une autre.
     * @param other l'autre position
     * @return la distance en ligne
     */
    public int getRowDistance(Position other) {
        return Math.abs(this.row - other.row);
    }
    
    /**
     * Calcule la distance en colonne entre cette position et une autre.
     * @param other l'autre position
     * @return la distance en colonne
     */
    public int getColumnDistance(Position other) {
        return Math.abs(this.column - other.column);
    }
    
    /**
     * Vérifie si cette position est sur la même ligne qu'une autre.
     * @param other l'autre position
     * @return true si sur la même ligne
     */
    public boolean isSameRow(Position other) {
        return this.row == other.row;
    }
    
    /**
     * Vérifie si cette position est sur la même colonne qu'une autre.
     * @param other l'autre position
     * @return true si sur la même colonne
     */
    public boolean isSameColumn(Position other) {
        return this.column == other.column;
    }
    
    /**
     * Vérifie si cette position est sur la même diagonale qu'une autre.
     * @param other l'autre position
     * @return true si sur la même diagonale
     */
    public boolean isSameDiagonal(Position other) {
        return getRowDistance(other) == getColumnDistance(other);
    }
    
    /**
     * Retourne une nouvelle position décalée par les valeurs données.
     * @param rowOffset décalage en ligne
     * @param columnOffset décalage en colonne
     * @return nouvelle position ou null si invalide
     */
    public Position add(int rowOffset, int columnOffset) {
        int newRow = this.row + rowOffset;
        int newColumn = this.column + columnOffset;
        
        if (isValid(newRow, newColumn)) {
            return new Position(newRow, newColumn);
        }
        return null;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return row == position.row && column == position.column;
    }
    
    @Override
    public int hashCode() {
        return row * 8 + column;
    }
    
    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }
    
    /**
     * Retourne la notation algébrique de la position (ex: "e4").
     * @return la notation algébrique
     */
    public String toAlgebraicNotation() {
        char columnChar = (char) ('a' + column);
        int rowNumber = 8 - row;
        return columnChar + String.valueOf(rowNumber);
    }
    
    /**
     * Crée une position à partir de la notation algébrique (ex: "e4").
     * @param algebraic la notation algébrique
     * @return la position correspondante
     * @throws IllegalArgumentException si la notation est invalide
     */
    public static Position fromAlgebraicNotation(String algebraic) {
        if (algebraic == null || algebraic.length() != 2) {
            throw new IllegalArgumentException("Notation algébrique invalide : " + algebraic);
        }
        
        char columnChar = algebraic.charAt(0);
        char rowChar = algebraic.charAt(1);
        
        if (columnChar < 'a' || columnChar > 'h' || rowChar < '1' || rowChar > '8') {
            throw new IllegalArgumentException("Notation algébrique invalide : " + algebraic);
        }
        
        int column = columnChar - 'a';
        int row = 8 - (rowChar - '0');
        
        return new Position(row, column);
    }
}
