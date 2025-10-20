package com.chess.board;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente une dame aux échecs.
 * La dame combine les mouvements de la tour et du fou.
 */
public class Queen extends Piece {
    
    /**
     * Constructeur pour créer une dame.
     * @param color la couleur de la dame
     * @param position la position initiale de la dame
     */
    public Queen(Color color, Position position) {
        super(PieceType.QUEEN, color, position);
    }
    
    @Override
    public boolean canMoveTo(Position targetPosition, Board board) {
        if (targetPosition == null || !targetPosition.isValid()) {
            return false;
        }
        
        // La dame ne peut pas rester sur la même position
        if (position.equals(targetPosition)) {
            return false;
        }
        
        // Vérifier si la case cible contient une pièce de la même couleur
        Piece targetPiece = board.getPieceAt(targetPosition);
        if (targetPiece != null && isSameColor(targetPiece)) {
            return false;
        }
        
        // La dame peut se déplacer en ligne droite ou en diagonale
        boolean isStraightLine = position.isSameRow(targetPosition) || position.isSameColumn(targetPosition);
        boolean isDiagonal = position.isSameDiagonal(targetPosition);
        
        if (!isStraightLine && !isDiagonal) {
            return false;
        }
        
        // Vérifier qu'il n'y a pas de pièce sur le chemin
        return isPathClear(position, targetPosition, board);
    }
    
    @Override
    public Position[] getPossibleMoves(Board board) {
        List<Position> possibleMoves = new ArrayList<>();
        
        // Directions : 8 directions (lignes, colonnes et diagonales)
        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},  // Diagonales et ligne du haut
            {0, -1},           {0, 1},   // Colonnes gauche et droite
            {1, -1},  {1, 0},  {1, 1}    // Diagonales et ligne du bas
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
