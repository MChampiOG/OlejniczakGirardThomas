package com.chess.board;

/**
 * Classe abstraite représentant une pièce d'échecs.
 * Toutes les pièces héritent de cette classe.
 */
public abstract class Piece {
    protected final PieceType type;
    protected final Color color;
    protected Position position;
    protected boolean hasMoved;
    
    /**
     * Constructeur pour créer une pièce.
     * @param type le type de pièce
     * @param color la couleur de la pièce
     * @param position la position initiale de la pièce
     */
    protected Piece(PieceType type, Color color, Position position) {
        this.type = type;
        this.color = color;
        this.position = position;
        this.hasMoved = false;
    }
    
    /**
     * Retourne le type de la pièce.
     * @return le type de pièce
     */
    public PieceType getType() {
        return type;
    }
    
    /**
     * Retourne la couleur de la pièce.
     * @return la couleur
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Retourne la position actuelle de la pièce.
     * @return la position
     */
    public Position getPosition() {
        return position;
    }
    
    /**
     * Définit la position de la pièce.
     * @param position la nouvelle position
     */
    public void setPosition(Position position) {
        this.position = position;
    }
    
    /**
     * Vérifie si la pièce a déjà bougé.
     * @return true si la pièce a bougé
     */
    public boolean hasMoved() {
        return hasMoved;
    }
    
    /**
     * Marque la pièce comme ayant bougé.
     */
    public void markAsMoved() {
        this.hasMoved = true;
    }
    
    /**
     * Vérifie si cette pièce peut se déplacer vers une position donnée.
     * Cette méthode doit être implémentée par chaque type de pièce.
     * @param targetPosition la position cible
     * @param board le plateau de jeu
     * @return true si le déplacement est possible
     */
    public abstract boolean canMoveTo(Position targetPosition, Board board);
    
    /**
     * Retourne la liste des positions possibles pour cette pièce.
     * Cette méthode doit être implémentée par chaque type de pièce.
     * @param board le plateau de jeu
     * @return un tableau des positions possibles
     */
    public abstract Position[] getPossibleMoves(Board board);
    
    /**
     * Vérifie si cette pièce peut capturer une autre pièce.
     * Par défaut, utilise la même logique que canMoveTo.
     * @param targetPosition la position de la pièce à capturer
     * @param board le plateau de jeu
     * @return true si la capture est possible
     */
    public boolean canCapture(Position targetPosition, Board board) {
        return canMoveTo(targetPosition, board);
    }
    
    /**
     * Vérifie si cette pièce est de la même couleur qu'une autre pièce.
     * @param other l'autre pièce
     * @return true si les pièces sont de la même couleur
     */
    public boolean isSameColor(Piece other) {
        return this.color == other.color;
    }
    
    /**
     * Vérifie si cette pièce est de couleur opposée à une autre pièce.
     * @param other l'autre pièce
     * @return true si les pièces sont de couleurs opposées
     */
    public boolean isOppositeColor(Piece other) {
        return this.color == other.color.opposite();
    }
    
    /**
     * Retourne la valeur de la pièce.
     * @return la valeur de la pièce
     */
    public int getValue() {
        return type.getValue();
    }
    
    /**
     * Retourne le symbole de la pièce avec sa couleur.
     * @return le symbole coloré
     */
    public String getSymbol() {
        return color == Color.WHITE ? type.getSymbol().toUpperCase() : type.getSymbol().toLowerCase();
    }
    
    /**
     * Retourne le nom complet de la pièce avec sa couleur.
     * @return le nom complet
     */
    public String getFullName() {
        return color.getDisplayName() + " " + type.getDisplayName();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Piece piece = (Piece) obj;
        return type == piece.type && color == piece.color && 
               position.equals(piece.position) && hasMoved == piece.hasMoved;
    }
    
    @Override
    public int hashCode() {
        return java.util.Objects.hash(type, color, position, hasMoved);
    }
    
    @Override
    public String toString() {
        return getFullName() + " en " + position.toAlgebraicNotation();
    }
}
