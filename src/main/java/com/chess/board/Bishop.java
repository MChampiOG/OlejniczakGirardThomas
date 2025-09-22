package com.chess.board;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un fou aux échecs.
 */
public class Bishop extends Piece {
    
    /**
     * Constructeur pour créer un fou.
     * @param color la couleur du fou
     * @param position la position initiale du fou
     */
    public Bishop(Color color, Position position) {
        super(PieceType.BISHOP, color, position);
    }
    
    @Override
    public boolean canMoveTo(Position targetPosition, Board board) {
        if (targetPosition == null || !targetPosition.isValid()) {
            return false;
        }
        
        // Le fou ne peut pas rester sur la même position
        if (position.equals(targetPosition)) {
            return false;
        }
        
        // Vérifier si la case cible contient une pièce de la même couleur
        Piece targetPiece = board.getPieceAt(targetPosition);
        if (targetPiece != null && isSameColor(targetPiece)) {
            return false;
        }
        
        // Le fou ne peut se déplacer qu'en diagonale
        if (!position.isSameDiagonal(targetPosition)) {
            return false;
        }
        
        // Vérifier qu'il n'y a pas de pièce sur le chemin
        return isPathClear(position, targetPosition, board);
    }
    
    @Override
    public Position[] getPossibleMoves(Board board) {
        List<Position> possibleMoves = new ArrayList<>();
        
        // Directions : 4 diagonales
        int[][] directions = {
            {-1, -1},  // Diagonale haut-gauche
            {-1, 1},   // Diagonale haut-droite
            {1, -1},   // Diagonale bas-gauche
            {1, 1}     // Diagonale bas-droite
        };
        
        for (int[] direction : directions) {
            int rowOffset = direction[0];
            int columnOffset = direction[1];
            
            Position currentPosition = position.add(rowOffset, columnOffset);
            while (currentPosition != null) {
                if (canMoveTo(currentPosition, board)) {
                    possibleMoves.add(currentPosition);
                }
                
                // Arrêter si on rencontre une pièce (même si on peut la capturer)
                Piece pieceAtPosition = board.getPieceAt(currentPosition);
                if (pieceAtPosition != null) {
                    break;
                }
                
                currentPosition = currentPosition.add(rowOffset, columnOffset);
            }
        }
        
        return possibleMoves.toArray(new Position[0]);
    }
    
    /**
     * Vérifie si le chemin entre deux positions est libre.
     * @param from la position de départ
     * @param to la position d'arrivée
     * @param board le plateau de jeu
     * @return true si le chemin est libre
     */
    private boolean isPathClear(Position from, Position to, Board board) {
        int rowDirection = Integer.compare(to.getRow(), from.getRow());
        int columnDirection = Integer.compare(to.getColumn(), from.getColumn());
        
        Position currentPosition = from.add(rowDirection, columnDirection);
        while (currentPosition != null && !currentPosition.equals(to)) {
            if (board.getPieceAt(currentPosition) != null) {
                return false;
            }
            currentPosition = currentPosition.add(rowDirection, columnDirection);
        }
        
        return true;
    }
}
