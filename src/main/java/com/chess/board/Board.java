package com.chess.board;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente le plateau d'échecs 8x8.
 * Gère l'état du jeu, les pièces et leurs positions.
 */
public class Board {
    private static final int BOARD_SIZE = 8;
    private Piece[][] squares;
    private Color currentPlayer;
    private List<Piece> capturedPieces;
    
    /**
     * Constructeur pour créer un plateau vide.
     */
    public Board() {
        this.squares = new Piece[BOARD_SIZE][BOARD_SIZE];
        this.currentPlayer = Color.WHITE;
        this.capturedPieces = new ArrayList<>();
    }
    
    /**
     * Initialise le plateau avec les pièces en position de départ.
     */
    public void initializeBoard() {
        // Vider le plateau
        clearBoard();
        
        // Placer les pièces blanches
        placeWhitePieces();
        
        // Placer les pièces noires
        placeBlackPieces();
    }
    
    /**
     * Vide le plateau.
     */
    private void clearBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                squares[row][col] = null;
            }
        }
    }
    
    /**
     * Place les pièces blanches en position de départ.
     */
    private void placeWhitePieces() {
        // Pions blancs
        for (int col = 0; col < BOARD_SIZE; col++) {
            setPieceAt(new Position(1, col), new Pawn(Color.WHITE, new Position(1, col)));
        }
        
        // Autres pièces blanches
        setPieceAt(new Position(0, 0), new Rook(Color.WHITE, new Position(0, 0)));
        setPieceAt(new Position(0, 1), new Knight(Color.WHITE, new Position(0, 1)));
        setPieceAt(new Position(0, 2), new Bishop(Color.WHITE, new Position(0, 2)));
        setPieceAt(new Position(0, 3), new Queen(Color.WHITE, new Position(0, 3)));
        setPieceAt(new Position(0, 4), new King(Color.WHITE, new Position(0, 4)));
        setPieceAt(new Position(0, 5), new Bishop(Color.WHITE, new Position(0, 5)));
        setPieceAt(new Position(0, 6), new Knight(Color.WHITE, new Position(0, 6)));
        setPieceAt(new Position(0, 7), new Rook(Color.WHITE, new Position(0, 7)));
    }
    
    /**
     * Place les pièces noires en position de départ.
     */
    private void placeBlackPieces() {
        // Pions noirs
        for (int col = 0; col < BOARD_SIZE; col++) {
            setPieceAt(new Position(6, col), new Pawn(Color.BLACK, new Position(6, col)));
        }
        
        // Autres pièces noires
        setPieceAt(new Position(7, 0), new Rook(Color.BLACK, new Position(7, 0)));
        setPieceAt(new Position(7, 1), new Knight(Color.BLACK, new Position(7, 1)));
        setPieceAt(new Position(7, 2), new Bishop(Color.BLACK, new Position(7, 2)));
        setPieceAt(new Position(7, 3), new Queen(Color.BLACK, new Position(7, 3)));
        setPieceAt(new Position(7, 4), new King(Color.BLACK, new Position(7, 4)));
        setPieceAt(new Position(7, 5), new Bishop(Color.BLACK, new Position(7, 5)));
        setPieceAt(new Position(7, 6), new Knight(Color.BLACK, new Position(7, 6)));
        setPieceAt(new Position(7, 7), new Rook(Color.BLACK, new Position(7, 7)));
    }
    
    /**
     * Retourne la pièce à une position donnée.
     * @param position la position
     * @return la pièce à cette position, ou null si la case est vide
     */
    public Piece getPieceAt(Position position) {
        if (position == null || !position.isValid()) {
            return null;
        }
        return squares[position.getRow()][position.getColumn()];
    }
    
    /**
     * Place une pièce à une position donnée.
     * @param position la position
     * @param piece la pièce à placer
     */
    public void setPieceAt(Position position, Piece piece) {
        if (position != null && position.isValid()) {
            squares[position.getRow()][position.getColumn()] = piece;
            if (piece != null) {
                piece.setPosition(position);
            }
        }
    }
    
    /**
     * Retourne le joueur dont c'est le tour.
     * @return la couleur du joueur actuel
     */
    public Color getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     * Change le joueur actuel.
     */
    public void switchPlayer() {
        currentPlayer = currentPlayer.opposite();
    }
    
    /**
     * Déplace une pièce d'une position à une autre.
     * @param from la position de départ
     * @param to la position d'arrivée
     * @return true si le déplacement a été effectué
     */
    public boolean movePiece(Position from, Position to) {
        Piece piece = getPieceAt(from);
        if (piece == null) {
            return false;
        }
        
        // Vérifier que c'est le tour du bon joueur
        if (piece.getColor() != currentPlayer) {
            return false;
        }
        
        // Vérifier que le déplacement est valide
        if (!piece.canMoveTo(to, this)) {
            return false;
        }
        
        // Capturer la pièce à la position d'arrivée si elle existe
        Piece capturedPiece = getPieceAt(to);
        if (capturedPiece != null) {
            capturedPieces.add(capturedPiece);
        }
        
        // Effectuer le déplacement
        setPieceAt(from, null);
        setPieceAt(to, piece);
        piece.setPosition(to);
        piece.markAsMoved();
        
        return true;
    }
    
    /**
     * Vérifie si une position est vide.
     * @param position la position à vérifier
     * @return true si la position est vide
     */
    public boolean isEmpty(Position position) {
        return getPieceAt(position) == null;
    }
    
    /**
     * Vérifie si une position contient une pièce d'une couleur donnée.
     * @param position la position à vérifier
     * @param color la couleur à vérifier
     * @return true si la position contient une pièce de cette couleur
     */
    public boolean containsPieceOfColor(Position position, Color color) {
        Piece piece = getPieceAt(position);
        return piece != null && piece.getColor() == color;
    }
    
    /**
     * Retourne toutes les pièces d'une couleur donnée.
     * @param color la couleur des pièces
     * @return la liste des pièces de cette couleur
     */
    public List<Piece> getPiecesOfColor(Color color) {
        List<Piece> pieces = new ArrayList<>();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = squares[row][col];
                if (piece != null && piece.getColor() == color) {
                    pieces.add(piece);
                }
            }
        }
        return pieces;
    }
    
    /**
     * Trouve le roi d'une couleur donnée.
     * @param color la couleur du roi
     * @return le roi de cette couleur, ou null si non trouvé
     */
    public King findKing(Color color) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = squares[row][col];
                if (piece instanceof King && piece.getColor() == color) {
                    return (King) piece;
                }
            }
        }
        return null;
    }
    
    /**
     * Vérifie si un roi est en échec.
     * @param color la couleur du roi à vérifier
     * @return true si le roi est en échec
     */
    public boolean isInCheck(Color color) {
        King king = findKing(color);
        if (king == null) {
            return false;
        }
        
        Color opponentColor = color.opposite();
        List<Piece> opponentPieces = getPiecesOfColor(opponentColor);
        
        for (Piece piece : opponentPieces) {
            if (piece.canCapture(king.getPosition(), this)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Vérifie si un roi serait en échec après un déplacement.
     * @param from la position de départ
     * @param to la position d'arrivée
     * @param color la couleur du roi
     * @return true si le roi serait en échec
     */
    public boolean wouldBeInCheckAfterMove(Position from, Position to, Color color) {
        // Sauvegarder l'état actuel
        Piece originalPiece = getPieceAt(from);
        Piece capturedPiece = getPieceAt(to);
        
        // Effectuer le déplacement temporaire
        setPieceAt(from, null);
        setPieceAt(to, originalPiece);
        
        // Vérifier l'échec
        boolean inCheck = isInCheck(color);
        
        // Restaurer l'état
        setPieceAt(from, originalPiece);
        setPieceAt(to, capturedPiece);
        
        return inCheck;
    }
    
    /**
     * Retourne la liste des pièces capturées.
     * @return la liste des pièces capturées
     */
    public List<Piece> getCapturedPieces() {
        return new ArrayList<>(capturedPieces);
    }
    
    /**
     * Retourne une représentation textuelle du plateau.
     * @return la représentation du plateau
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("  a b c d e f g h\n");
        
        for (int row = 0; row < BOARD_SIZE; row++) {
            sb.append(8 - row).append(" ");
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = squares[row][col];
                if (piece == null) {
                    sb.append(". ");
                } else {
                    sb.append(piece.getSymbol()).append(" ");
                }
            }
            sb.append(8 - row).append("\n");
        }
        
        sb.append("  a b c d e f g h\n");
        return sb.toString();
    }
    
    /**
     * Retourne la taille du plateau.
     * @return la taille du plateau
     */
    public static int getBoardSize() {
        return BOARD_SIZE;
    }
}
