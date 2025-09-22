package com.chess.board;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente un pion aux échecs.
 */
public class Pawn extends Piece {
    
    /**
     * Constructeur pour créer un pion.
     * @param color la couleur du pion
     * @param position la position initiale du pion
     */
    public Pawn(Color color, Position position) {
        super(PieceType.PAWN, color, position);
    }
    
    @Override
    public boolean canMoveTo(Position targetPosition, Board board) {
        if (targetPosition == null || !targetPosition.isValid()) {
            return false;
        }
        
        // Le pion ne peut pas rester sur la même position
        if (position.equals(targetPosition)) {
            return false;
        }
        
        // Vérifier si la case cible contient une pièce de la même couleur
        Piece targetPiece = board.getPieceAt(targetPosition);
        if (targetPiece != null && isSameColor(targetPiece)) {
            return false;
        }
        
        int rowDistance = targetPosition.getRow() - position.getRow();
        int columnDistance = Math.abs(targetPosition.getColumn() - position.getColumn());
        
        // Le pion se déplace vers l'avant selon sa couleur
        int expectedRowDirection = color.getPawnDirection();
        
        // Mouvement simple vers l'avant
        if (columnDistance == 0 && rowDistance == expectedRowDirection) {
            return targetPiece == null; // Ne peut se déplacer que si la case est vide
        }
        
        // Mouvement de deux cases depuis la position de départ
        if (columnDistance == 0 && rowDistance == 2 * expectedRowDirection && !hasMoved) {
            // Vérifier que les deux cases sont vides
            Position intermediatePosition = position.add(expectedRowDirection, 0);
            return targetPiece == null && board.getPieceAt(intermediatePosition) == null;
        }
        
        // Capture en diagonale
        if (columnDistance == 1 && rowDistance == expectedRowDirection) {
            return targetPiece != null && isOppositeColor(targetPiece);
        }
        
        return false;
    }
    
    @Override
    public Position[] getPossibleMoves(Board board) {
        List<Position> possibleMoves = new ArrayList<>();
        
        int rowDirection = color.getPawnDirection();
        
        // Mouvement simple vers l'avant
        Position forwardPosition = position.add(rowDirection, 0);
        if (forwardPosition != null && canMoveTo(forwardPosition, board)) {
            possibleMoves.add(forwardPosition);
        }
        
        // Mouvement de deux cases depuis la position de départ
        if (!hasMoved) {
            Position doubleForwardPosition = position.add(2 * rowDirection, 0);
            if (doubleForwardPosition != null && canMoveTo(doubleForwardPosition, board)) {
                possibleMoves.add(doubleForwardPosition);
            }
        }
        
        // Captures en diagonale
        Position leftCapture = position.add(rowDirection, -1);
        if (leftCapture != null && canMoveTo(leftCapture, board)) {
            possibleMoves.add(leftCapture);
        }
        
        Position rightCapture = position.add(rowDirection, 1);
        if (rightCapture != null && canMoveTo(rightCapture, board)) {
            possibleMoves.add(rightCapture);
        }
        
        return possibleMoves.toArray(new Position[0]);
    }
    
    /**
     * Vérifie si le pion peut effectuer une prise en passant.
     * @param targetPosition la position cible
     * @param board le plateau de jeu
     * @return true si la prise en passant est possible
     */
    public boolean canEnPassant(Position targetPosition, Board board) {
        if (targetPosition == null || !targetPosition.isValid()) {
            return false;
        }
        
        // La prise en passant n'est possible que si le pion est sur la 5ème rangée (blancs) ou 4ème rangée (noirs)
        int enPassantRow = color == Color.WHITE ? 4 : 3;
        if (position.getRow() != enPassantRow) {
            return false;
        }
        
        // La position cible doit être sur la rangée suivante
        int expectedRow = position.getRow() + color.getPawnDirection();
        if (targetPosition.getRow() != expectedRow) {
            return false;
        }
        
        // La position cible doit être sur une colonne adjacente
        int columnDistance = Math.abs(targetPosition.getColumn() - position.getColumn());
        if (columnDistance != 1) {
            return false;
        }
        
        // Il doit y avoir un pion adverse sur la colonne cible à la même rangée
        Position enemyPawnPosition = new Position(position.getRow(), targetPosition.getColumn());
        Piece enemyPawn = board.getPieceAt(enemyPawnPosition);
        if (enemyPawn == null || !enemyPawn.getType().isPawn() || !isOppositeColor(enemyPawn)) {
            return false;
        }
        
        // Le pion adverse doit avoir fait un mouvement de deux cases au coup précédent
        // (Cette vérification nécessiterait l'historique des coups, on l'implémentera plus tard)
        
        return true;
    }
    
    /**
     * Vérifie si le pion peut être promu.
     * @return true si le pion peut être promu
     */
    public boolean canBePromoted() {
        int promotionRow = color == Color.WHITE ? 7 : 0;
        return position.getRow() == promotionRow;
    }
    
    /**
     * Retourne les positions possibles pour la promotion du pion.
     * @param board le plateau de jeu
     * @return un tableau des positions de promotion possibles
     */
    public Position[] getPromotionMoves(Board board) {
        List<Position> promotionMoves = new ArrayList<>();
        
        if (!canBePromoted()) {
            return new Position[0];
        }
        
        int rowDirection = color.getPawnDirection();
        int promotionRow = position.getRow() + rowDirection;
        
        // Vérifier les trois colonnes possibles (même colonne et diagonales)
        for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
            Position promotionPosition = new Position(promotionRow, position.getColumn() + columnOffset);
            if (promotionPosition.isValid() && canMoveTo(promotionPosition, board)) {
                promotionMoves.add(promotionPosition);
            }
        }
        
        return promotionMoves.toArray(new Position[0]);
    }
}
