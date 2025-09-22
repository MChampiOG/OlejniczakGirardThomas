package com.chess.board;

/**
 * Représente les couleurs des pièces d'échecs.
 */
public enum Color {
    WHITE("Blanc"),
    BLACK("Noir");
    
    private final String displayName;
    
    /**
     * Constructeur pour l'énumération Color.
     * @param displayName le nom d'affichage de la couleur
     */
    Color(String displayName) {
        this.displayName = displayName;
    }
    
    /**
     * Retourne le nom d'affichage de la couleur.
     * @return le nom d'affichage
     */
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Retourne la couleur opposée.
     * @return la couleur opposée
     */
    public Color opposite() {
        return this == WHITE ? BLACK : WHITE;
    }
    
    /**
     * Retourne la direction de déplacement des pions pour cette couleur.
     * @return 1 pour les blancs (vers le bas), -1 pour les noirs (vers le haut)
     */
    public int getPawnDirection() {
        return this == WHITE ? 1 : -1;
    }
    
    /**
     * Retourne la ligne de départ des pions pour cette couleur.
     * @return 1 pour les blancs, 6 pour les noirs
     */
    public int getPawnStartRow() {
        return this == WHITE ? 1 : 6;
    }
    
    /**
     * Retourne la ligne de départ des autres pièces pour cette couleur.
     * @return 0 pour les blancs, 7 pour les noirs
     */
    public int getPieceStartRow() {
        return this == WHITE ? 0 : 7;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
