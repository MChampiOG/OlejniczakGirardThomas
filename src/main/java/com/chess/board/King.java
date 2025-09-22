package com.chess.board;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un roi aux échecs.
 */
public class King extends Piece {
    
    /**
     * Constructeur pour créer un roi.
     * @param color la couleur du roi
     * @param position la position initiale du roi
     */
    public King(Color color, Position position) {
        super(PieceType.KING, color, position);
    }
    
    @Override
    public boolean canMoveTo(Position targetPosition, Board board) {
        if (targetPosition == null || !targetPosition.isValid()) {
            return false;
        }
        
        // Le roi ne peut pas rester sur la même position
        if (position.equals(targetPosition)) {
            return false;
        }
        
        // Vérifier si la case cible contient une pièce de la même couleur
        Piece targetPiece = board.getPieceAt(targetPosition);
        if (targetPiece != null && isSameColor(targetPiece)) {
            return false;
        }
        
        // Le roi peut se déplacer d'une case dans n'importe quelle direction
        int rowDistance = position.getRowDistance(targetPosition);
        int columnDistance = position.getColumnDistance(targetPosition);
        
        return rowDistance <= 1 && columnDistance <= 1;
    }
    
    @Override
    public Position[] getPossibleMoves(Board board) {
        List<Position> possibleMoves = new ArrayList<>();
        
        // Vérifier les 8 directions possibles
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                if (rowOffset == 0 && columnOffset == 0) {
                    continue; // Ignorer la position actuelle
                }
                
                Position targetPosition = position.add(rowOffset, columnOffset);
                if (targetPosition != null && canMoveTo(targetPosition, board)) {
                    possibleMoves.add(targetPosition);
                }
            }
        }
        
        return possibleMoves.toArray(new Position[0]);
    }
    
    /**
     * Vérifie si le roi peut effectuer un roque.
     * @param board le plateau de jeu
     * @return true si le roque est possible
     */
    public boolean canCastle(Board board) {
        // Le roi ne peut pas roquer s'il a déjà bougé
        if (hasMoved) {
            return false;
        }
        
        // Le roi ne peut pas roquer s'il est en échec
        if (board.isInCheck(color)) {
            return false;
        }
        
        return canCastleKingside(board) || canCastleQueenside(board);
    }
    
    /**
     * Vérifie si le roque côté roi est possible.
     * @param board le plateau de jeu
     * @return true si le roque côté roi est possible
     */
    public boolean canCastleKingside(Board board) {
        if (hasMoved || board.isInCheck(color)) {
            return false;
        }
        
        // Vérifier que la tour côté roi n'a pas bougé
        Position rookPosition = new Position(position.getRow(), 7);
        Piece rook = board.getPieceAt(rookPosition);
        if (rook == null || !rook.getType().isRook() || rook.hasMoved()) {
            return false;
        }
        
        // Vérifier que les cases entre le roi et la tour sont vides
        Position f1 = new Position(position.getRow(), 5);
        Position g1 = new Position(position.getRow(), 6);
        
        if (board.getPieceAt(f1) != null || board.getPieceAt(g1) != null) {
            return false;
        }
        
        // Vérifier que le roi ne passe pas par une case en échec
        return !board.wouldBeInCheckAfterMove(position, f1, color) &&
               !board.wouldBeInCheckAfterMove(position, g1, color);
    }
    
    /**
     * Vérifie si le roque côté dame est possible.
     * @param board le plateau de jeu
     * @return true si le roque côté dame est possible
     */
    public boolean canCastleQueenside(Board board) {
        if (hasMoved || board.isInCheck(color)) {
            return false;
        }
        
        // Vérifier que la tour côté dame n'a pas bougé
        Position rookPosition = new Position(position.getRow(), 0);
        Piece rook = board.getPieceAt(rookPosition);
        if (rook == null || !rook.getType().isRook() || rook.hasMoved()) {
            return false;
        }
        
        // Vérifier que les cases entre le roi et la tour sont vides
        Position b1 = new Position(position.getRow(), 1);
        Position c1 = new Position(position.getRow(), 2);
        Position d1 = new Position(position.getRow(), 3);
        
        if (board.getPieceAt(b1) != null || board.getPieceAt(c1) != null || board.getPieceAt(d1) != null) {
            return false;
        }
        
        // Vérifier que le roi ne passe pas par une case en échec
        return !board.wouldBeInCheckAfterMove(position, c1, color) &&
               !board.wouldBeInCheckAfterMove(position, d1, color);
    }
}
