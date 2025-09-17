package com.chess.board;

/**
 * Représente les types de pièces d'échecs.
 */
public enum PieceType {
    KING("Roi", "K"),
    QUEEN("Dame", "Q"),
    ROOK("Tour", "R"),
    BISHOP("Fou", "B"),
    KNIGHT("Cavalier", "N"),
    PAWN("Pion", "P");
    
    private final String displayName;
    private final String symbol;
    
    /**
     * Constructeur pour l'énumération PieceType.
     * @param displayName le nom d'affichage de la pièce
     * @param symbol le symbole de la pièce en notation algébrique
     */
    PieceType(String displayName, String symbol) {
        this.displayName = displayName;
        this.symbol = symbol;
    }
    
    /**
     * Retourne le nom d'affichage de la pièce.
     * @return le nom d'affichage
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Retourne le symbole de la pièce en notation algébrique.
     * @return le symbole
     */
    public String getSymbol() {
        return symbol;
    }
    
    /**
     * Vérifie si cette pièce est un pion.
     * @return true si c'est un pion
     */
    public boolean isPawn() {
        return this == PAWN;
    }
    
    /**
     * Vérifie si cette pièce est un roi.
     * @return true si c'est un roi
     */
    public boolean isKing() {
        return this == KING;
    }
    
    /**
     * Vérifie si cette pièce est une dame.
     * @return true si c'est une dame
     */
    public boolean isQueen() {
        return this == QUEEN;
    }
    
    /**
     * Vérifie si cette pièce est une tour.
     * @return true si c'est une tour
     */
    public boolean isRook() {
        return this == ROOK;
    }
    
    /**
     * Vérifie si cette pièce est un fou.
     * @return true si c'est un fou
     */
    public boolean isBishop() {
        return this == BISHOP;
    }
    
    /**
     * Vérifie si cette pièce est un cavalier.
     * @return true si c'est un cavalier
     */
    public boolean isKnight() {
        return this == KNIGHT;
    }
    
    /**
     * Retourne la valeur relative de la pièce (pour l'évaluation).
     * @return la valeur de la pièce
     */
    public int getValue() {
        return switch (this) {
            case PAWN -> 1;
            case KNIGHT, BISHOP -> 3;
            case ROOK -> 5;
            case QUEEN -> 9;
            case KING -> 0; // Le roi n'a pas de valeur car il ne peut pas être capturé
        };
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
