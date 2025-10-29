package com.chess.rules;

import com.chess.board.Board;
import com.chess.board.Color;
import com.chess.board.Piece;
import com.chess.board.Position;

/**
 * Valide les coups dans une partie d'échecs.
 * Vérifie que les coups respectent les règles et n'exposent pas le roi à un échec.
 */
public class MoveValidator {
    private final Board board;
    private final MoveHistory moveHistory;
    
    /**
     * Constructeur pour créer un validateur de coups.
     * @param board le plateau de jeu
     * @param moveHistory l'historique des coups
     */
    public MoveValidator(Board board, MoveHistory moveHistory) {
        this.board = board;
        this.moveHistory = moveHistory;
    }
    
    /**
     * Vérifie si un coup est valide.
     * @param from la position de départ
     * @param to la position d'arrivée
     * @return true si le coup est valide
     */
    public boolean isValidMove(Position from, Position to) {
        // Vérifier que les positions sont valides
        if (from == null || to == null || !from.isValid() || !to.isValid()) {
            return false;
        }
        
        // Vérifier qu'il y a une pièce à la position de départ
        Piece piece = board.getPieceAt(from);
        if (piece == null) {
            return false;
        }
        
        // Vérifier que c'est le tour du bon joueur
        if (piece.getColor() != board.getCurrentPlayer()) {
            return false;
        }
        
        // Vérifier que la case d'arrivée ne contient pas une pièce de la même couleur
        Piece targetPiece = board.getPieceAt(to);
        if (targetPiece != null && piece.isSameColor(targetPiece)) {
            return false;
        }
        
        // Vérifier que le mouvement de base de la pièce est valide
        if (!piece.canMoveTo(to, board)) {
            return false;
        }
        
        // Vérifier les règles spéciales (roque, en passant)
        SpecialMovesHandler specialMovesHandler = new SpecialMovesHandler(board, moveHistory);
        if (specialMovesHandler.isSpecialMove(from, to)) {
            return specialMovesHandler.isValidSpecialMove(from, to);
        }
        
        // Vérifier que le coup ne met pas le roi en échec
        if (wouldPutKingInCheck(from, to, piece.getColor())) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Vérifie si un coup mettrait le roi en échec.
     * @param from la position de départ
     * @param to la position d'arrivée
     * @param color la couleur du joueur
     * @return true si le coup mettrait le roi en échec
     */
    public boolean wouldPutKingInCheck(Position from, Position to, Color color) {
        return board.wouldBeInCheckAfterMove(from, to, color);
    }
    
    /**
     * Retourne tous les coups valides pour une pièce à une position donnée.
     * @param position la position de la pièce
     * @return un tableau des positions valides
     */
    public Position[] getValidMoves(Position position) {
        Piece piece = board.getPieceAt(position);
        if (piece == null || piece.getColor() != board.getCurrentPlayer()) {
            return new Position[0];
        }
        
        Position[] possibleMoves = piece.getPossibleMoves(board);
        java.util.List<Position> validMoves = new java.util.ArrayList<>();
        
        for (Position to : possibleMoves) {
            if (isValidMove(position, to)) {
                validMoves.add(to);
            }
        }
        
        // Vérifier les coups spéciaux
        SpecialMovesHandler specialMovesHandler = new SpecialMovesHandler(board, moveHistory);
        Position[] specialMoves = specialMovesHandler.getSpecialMoves(position);
        for (Position specialMove : specialMoves) {
            if (isValidMove(position, specialMove)) {
                validMoves.add(specialMove);
            }
        }
        
        return validMoves.toArray(new Position[0]);
    }
    
    /**
     * Retourne tous les coups valides pour toutes les pièces d'une couleur.
     * @param color la couleur
     * @return un tableau des coups valides (de la forme [from, to, from, to, ...])
     */
    public java.util.List<MovePair> getAllValidMoves(Color color) {
        java.util.List<MovePair> validMoves = new java.util.ArrayList<>();
        
        java.util.List<Piece> pieces = board.getPiecesOfColor(color);
        for (Piece piece : pieces) {
            Position[] moves = getValidMoves(piece.getPosition());
            for (Position to : moves) {
                validMoves.add(new MovePair(piece.getPosition(), to));
            }
        }
        
        return validMoves;
    }
    
    /**
     * Classe interne représentant une paire de positions (from, to).
     */
    public static class MovePair {
        private final Position from;
        private final Position to;
        
        public MovePair(Position from, Position to) {
            this.from = from;
            this.to = to;
        }
        
        public Position getFrom() {
            return from;
        }
        
        public Position getTo() {
            return to;
        }
        
        @Override
        public String toString() {
            return from.toAlgebraicNotation() + "-" + to.toAlgebraicNotation();
        }
    }
}
