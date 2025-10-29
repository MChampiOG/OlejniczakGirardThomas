package com.chess.rules;

import com.chess.board.Board;
import com.chess.board.Color;
import com.chess.board.Piece;
import com.chess.board.Position;

import java.util.ArrayList;
import java.util.List;

/**
 * Représente l'historique des coups dans une partie d'échecs.
 * Utilisé pour détecter les répétitions (nulle) et gérer la prise en passant.
 */
public class MoveHistory {
    private final List<Move> moves;
    private Piece lastMovedPawn;
    private Position lastPawnDoubleMoveTo;
    
    /**
     * Constructeur pour créer un historique vide.
     */
    public MoveHistory() {
        this.moves = new ArrayList<>();
        this.lastMovedPawn = null;
        this.lastPawnDoubleMoveTo = null;
    }
    
    /**
     * Ajoute un coup à l'historique.
     * @param from la position de départ
     * @param to la position d'arrivée
     * @param piece la pièce déplacée
     * @param capturedPiece la pièce capturée (peut être null)
     * @param board le plateau après le coup
     */
    public void addMove(Position from, Position to, Piece piece, Piece capturedPiece, Board board) {
        Move move = new Move(from, to, piece, capturedPiece, board.getCurrentPlayer());
        moves.add(move);
        
        // Enregistrer les informations pour la prise en passant
        if (piece.getType().isPawn() && from.getColumnDistance(to) == 0 
            && from.getRowDistance(to) == 2) {
            // Le pion a fait un mouvement de deux cases
            lastMovedPawn = piece;
            lastPawnDoubleMoveTo = to;
        } else {
            // Réinitialiser si ce n'est pas un mouvement double de pion
            lastMovedPawn = null;
            lastPawnDoubleMoveTo = null;
        }
    }
    
    /**
     * Retourne le dernier pion ayant fait un mouvement de deux cases.
     * @return le pion ou null
     */
    public Piece getLastMovedPawn() {
        return lastMovedPawn;
    }
    
    /**
     * Retourne la position du dernier mouvement double de pion.
     * @return la position ou null
     */
    public Position getLastPawnDoubleMoveTo() {
        return lastPawnDoubleMoveTo;
    }
    
    /**
     * Vérifie si un pion peut être capturé en passant.
     * @param pawnPosition la position du pion attaquant
     * @param targetPosition la position cible pour la prise en passant
     * @return true si la prise en passant est possible
     */
    public boolean canEnPassant(Position pawnPosition, Position targetPosition) {
        if (lastMovedPawn == null || lastPawnDoubleMoveTo == null || pawnPosition == null) {
            return false;
        }
        
        // Le pion attaquant doit être sur la même ligne que le pion qui a fait le double mouvement
        if (!pawnPosition.isSameRow(lastPawnDoubleMoveTo)) {
            return false;
        }
        
        // Le pion attaquant et le pion attaqué doivent être de couleurs opposées
        Color attackerColor = lastMovedPawn.getColor().opposite();
        
        // La position cible doit être en diagonale : même colonne que le pion qui a fait le double mouvement
        // et une ligne en avant dans la direction du pion attaquant
        if (targetPosition.getColumn() != lastPawnDoubleMoveTo.getColumn()) {
            return false;
        }
        
        // La position cible doit être une case en avant du pion attaquant
        int expectedRow = pawnPosition.getRow() + (attackerColor == Color.WHITE ? 1 : -1);
        return targetPosition.getRow() == expectedRow;
    }
    
    /**
     * Retourne la position du pion qui peut être capturé en passant.
     * @param targetPosition la position cible pour la prise en passant
     * @return la position du pion pouvant être capturé ou null
     */
    public Position getEnPassantCapturedPawnPosition(Position targetPosition) {
        if (!canEnPassant(null, targetPosition)) {
            return null;
        }
        
        // Le pion capturé est sur la même colonne mais la même ligne que le pion attaquant
        return lastPawnDoubleMoveTo;
    }
    
    /**
     * Retourne le nombre total de coups.
     * @return le nombre de coups
     */
    public int getMoveCount() {
        return moves.size();
    }
    
    /**
     * Retourne la liste de tous les coups.
     * @return la liste des coups
     */
    public List<Move> getMoves() {
        return new ArrayList<>(moves);
    }
    
    /**
     * Vérifie si la position actuelle s'est répétée trois fois.
     * @param currentBoard le plateau actuel
     * @return true si la position s'est répétée trois fois
     */
    public boolean isThreefoldRepetition(Board currentBoard) {
        // Pour simplifier, on compare les derniers coups
        // Une implémentation complète nécessiterait de stocker l'état complet du plateau
        if (moves.size() < 6) {
            return false;
        }
        
        // Vérifier les 6 derniers coups (3 de chaque joueur)
        int count = 0;
        String currentState = boardToString(currentBoard);
        
        for (int i = moves.size() - 2; i >= 0; i -= 2) {
            if (i < moves.size() && moves.get(i).boardState != null) {
                if (moves.get(i).boardState.equals(currentState)) {
                    count++;
                }
            }
        }
        
        return count >= 2; // La position actuelle + 2 répétitions précédentes
    }
    
    /**
     * Convertit le plateau en une représentation textuelle pour la comparaison.
     * @param board le plateau
     * @return la représentation textuelle
     */
    private String boardToString(Board board) {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPieceAt(new Position(row, col));
                if (piece == null) {
                    sb.append(".");
                } else {
                    sb.append(piece.getSymbol());
                }
            }
        }
        sb.append(board.getCurrentPlayer());
        return sb.toString();
    }
    
    /**
     * Classe interne représentant un coup.
     */
    public static class Move {
        private final Position from;
        private final Position to;
        private final Piece piece;
        private final Piece capturedPiece;
        private final Color player;
        private final String boardState;
        
        /**
         * Constructeur pour créer un coup.
         */
        public Move(Position from, Position to, Piece piece, Piece capturedPiece, Color player) {
            this.from = from;
            this.to = to;
            this.piece = piece;
            this.capturedPiece = capturedPiece;
            this.player = player;
            this.boardState = null; // Pourra être utilisé pour la répétition triple
        }
        
        public Position getFrom() {
            return from;
        }
        
        public Position getTo() {
            return to;
        }
        
        public Piece getPiece() {
            return piece;
        }
        
        public Piece getCapturedPiece() {
            return capturedPiece;
        }
        
        public Color getPlayer() {
            return player;
        }
        
        @Override
        public String toString() {
            String capture = capturedPiece != null ? "x" : "";
            return piece.getType().getSymbol() + from.toAlgebraicNotation() + capture + to.toAlgebraicNotation();
        }
    }
}
