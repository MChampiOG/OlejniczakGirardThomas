package com.chess.rules;

import com.chess.board.Board;
import com.chess.board.Color;
import com.chess.board.Piece;
import com.chess.board.PieceType;

/**
 * Vérifie l'état du jeu d'échecs : échec, mat, pat et nulle.
 */
public class GameStateChecker {
    private final Board board;
    private final MoveValidator moveValidator;
    
    /**
     * Constructeur pour créer un vérificateur d'état de jeu.
     * @param board le plateau de jeu
     * @param moveValidator le validateur de coups
     */
    public GameStateChecker(Board board, MoveValidator moveValidator) {
        this.board = board;
        this.moveValidator = moveValidator;
    }
    
    /**
     * Vérifie si un joueur est en échec.
     * @param color la couleur du joueur
     * @return true si le joueur est en échec
     */
    public boolean isInCheck(Color color) {
        return board.isInCheck(color);
    }
    
    /**
     * Vérifie si un joueur est en échec et mat.
     * @param color la couleur du joueur
     * @return true si le joueur est en échec et mat
     */
    public boolean isCheckmate(Color color) {
        // Le mat nécessite d'être en échec ET de ne pas avoir de coups valides
        if (!isInCheck(color)) {
            return false;
        }
        
        return !hasValidMoves(color);
    }
    
    /**
     * Vérifie si un joueur est en situation de pat (stalemate).
     * @param color la couleur du joueur
     * @return true si le joueur est en pat
     */
    public boolean isStalemate(Color color) {
        // Le pat nécessite de ne pas être en échec ET de ne pas avoir de coups valides
        if (isInCheck(color)) {
            return false;
        }
        
        return !hasValidMoves(color);
    }
    
    /**
     * Vérifie si un joueur a des coups valides disponibles.
     * @param color la couleur du joueur
     * @return true si le joueur a au moins un coup valide
     */
    public boolean hasValidMoves(Color color) {
        return !moveValidator.getAllValidMoves(color).isEmpty();
    }
    
    /**
     * Vérifie si la partie est nulle.
     * @param moveHistory l'historique des coups
     * @return true si la partie est nulle
     */
    public boolean isDraw(MoveHistory moveHistory) {
        // Vérifier différentes conditions de nulle
        return isStalemate(board.getCurrentPlayer()) ||
               isInsufficientMaterial() ||
               moveHistory.isThreefoldRepetition(board) ||
               isFiftyMoveRule(moveHistory);
    }
    
    /**
     * Vérifie s'il y a un matériel insuffisant pour faire échec et mat.
     * @return true si le matériel est insuffisant
     */
    private boolean isInsufficientMaterial() {
        // Compter les pièces restantes (excluant les rois qui sont toujours présents)
        java.util.List<Piece> whitePieces = board.getPiecesOfColor(Color.WHITE);
        java.util.List<Piece> blackPieces = board.getPiecesOfColor(Color.BLACK);
        
        // Exclure les rois du comptage
        long whiteNonKingPieces = whitePieces.stream().filter(p -> !p.getType().isKing()).count();
        long blackNonKingPieces = blackPieces.stream().filter(p -> !p.getType().isKing()).count();
        
        // Si les deux joueurs n'ont que leur roi
        if (whiteNonKingPieces == 0 && blackNonKingPieces == 0) {
            return true; // Roi contre roi
        }
        
        // Si un joueur n'a que son roi et l'autre a un fou ou un cavalier
        if ((whiteNonKingPieces == 0 && blackNonKingPieces == 1) || 
            (blackNonKingPieces == 0 && whiteNonKingPieces == 1)) {
            // Vérifier si l'autre a un fou ou un cavalier
            java.util.List<Piece> otherPieces = whiteNonKingPieces == 0 ? blackPieces : whitePieces;
            for (Piece p : otherPieces) {
                if (!p.getType().isKing() && (p.getType() == PieceType.BISHOP || p.getType() == PieceType.KNIGHT)) {
                    // Roi et fou/cavalier contre roi seul - matériel insuffisant pour mat
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Vérifie la règle des 50 coups.
     * @param moveHistory l'historique des coups
     * @return true si la règle des 50 coups s'applique
     */
    private boolean isFiftyMoveRule(MoveHistory moveHistory) {
        // La règle des 50 coups : si 50 coups ont été joués sans capture ni mouvement de pion
        // Pour simplifier, on vérifie simplement si 50 coups ont été joués
        // Une implémentation complète nécessiterait de suivre les captures et mouvements de pion
        return moveHistory.getMoveCount() >= 50;
    }
    
    /**
     * Retourne l'état actuel du jeu.
     * @param moveHistory l'historique des coups
     * @return l'état du jeu
     */
    public GameState getGameState(MoveHistory moveHistory) {
        Color currentPlayer = board.getCurrentPlayer();
        
        // Vérifier l'échec et mat en premier
        if (isCheckmate(currentPlayer)) {
            return GameState.CHECKMATE;
        }
        
        // Vérifier le pat
        if (isStalemate(currentPlayer)) {
            return GameState.STALEMATE;
        }
        
        // Vérifier l'échec avant la nulle
        if (isInCheck(currentPlayer)) {
            return GameState.CHECK;
        }
        
        // Vérifier la nulle (matériel insuffisant, etc.) uniquement si pas d'échec
        if (isDraw(moveHistory)) {
            return GameState.DRAW;
        }
        
        return GameState.ONGOING;
    }
    
    /**
     * Énumération représentant les différents états du jeu.
     */
    public enum GameState {
        ONGOING("Partie en cours"),
        CHECK("Échec"),
        CHECKMATE("Échec et mat"),
        STALEMATE("Pat"),
        DRAW("Nulle");
        
        private final String description;
        
        GameState(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
        
        @Override
        public String toString() {
            return description;
        }
    }
}
