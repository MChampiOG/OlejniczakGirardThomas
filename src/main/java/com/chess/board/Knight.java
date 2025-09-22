package com.chess.board;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un cavalier aux échecs.
 */
public class Knight extends Piece {
    
    /**
     * Constructeur pour créer un cavalier.
     * @param color la couleur du cavalier
     * @param position la position initiale du cavalier
     */
    public Knight(Color color, Position position) {
        super(PieceType.KNIGHT, color, position);
    }
    
    @Override
    public boolean canMoveTo(Position targetPosition, Board board) {
        if (targetPosition == null || !targetPosition.isValid()) {
            return false;
        }
        
        // Le cavalier ne peut pas rester sur la même position
        if (position.equals(targetPosition)) {
            return false;
        }
        
        // Vérifier si la case cible contient une pièce de la même couleur
        Piece targetPiece = board.getPieceAt(targetPosition);
        if (targetPiece != null && isSameColor(targetPiece)) {
            return false;
        }
        
        // Le cavalier se déplace en L : 2 cases dans une direction, puis 1 case perpendiculairement
        int rowDistance = position.getRowDistance(targetPosition);
        int columnDistance = position.getColumnDistance(targetPosition);
        
        return (rowDistance == 2 && columnDistance == 1) || (rowDistance == 1 && columnDistance == 2);
    }
    
    @Override
    public Position[] getPossibleMoves(Board board) {
        List<Position> possibleMoves = new ArrayList<>();
        
        // Les 8 mouvements possibles du cavalier
        int[][] knightMoves = {
            {-2, -1}, {-2, 1},  // 2 cases vers le haut, 1 case gauche/droite
            {-1, -2}, {-1, 2},  // 1 case vers le haut, 2 cases gauche/droite
            {1, -2},  {1, 2},   // 1 case vers le bas, 2 cases gauche/droite
            {2, -1},  {2, 1}    // 2 cases vers le bas, 1 case gauche/droite
        };
        
        for (int[] move : knightMoves) {
            int rowOffset = move[0];
            int columnOffset = move[1];
            
            Position targetPosition = position.add(rowOffset, columnOffset);
            if (targetPosition != null && canMoveTo(targetPosition, board)) {
                possibleMoves.add(targetPosition);
            }
        }
        
        return possibleMoves.toArray(new Position[0]);
    }
}
