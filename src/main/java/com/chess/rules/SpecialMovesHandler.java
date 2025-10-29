package com.chess.rules;

import com.chess.board.Board;
import com.chess.board.Color;
import com.chess.board.King;
import com.chess.board.Pawn;
import com.chess.board.Piece;
import com.chess.board.Position;
import com.chess.board.PieceType;
import com.chess.board.Queen;
import com.chess.board.Rook;
import com.chess.board.Bishop;
import com.chess.board.Knight;

import java.util.ArrayList;
import java.util.List;

/**
 * Gère les règles spéciales aux échecs : roque, prise en passant et promotion.
 */
public class SpecialMovesHandler {
    private final Board board;
    private final MoveHistory moveHistory;
    
    /**
     * Constructeur pour créer un gestionnaire de coups spéciaux.
     * @param board le plateau de jeu
     * @param moveHistory l'historique des coups
     */
    public SpecialMovesHandler(Board board, MoveHistory moveHistory) {
        this.board = board;
        this.moveHistory = moveHistory;
    }
    
    /**
     * Vérifie si un coup est un coup spécial.
     * @param from la position de départ
     * @param to la position d'arrivée
     * @return true si c'est un coup spécial
     */
    public boolean isSpecialMove(Position from, Position to) {
        Piece piece = board.getPieceAt(from);
        if (piece == null) {
            return false;
        }
        
        return isCastling(from, to) || isEnPassant(from, to) || isPromotion(from, to);
    }
    
    /**
     * Vérifie si un coup spécial est valide.
     * @param from la position de départ
     * @param to la position d'arrivée
     * @return true si le coup spécial est valide
     */
    public boolean isValidSpecialMove(Position from, Position to) {
        if (isCastling(from, to)) {
            return isValidCastling(from, to);
        }
        if (isEnPassant(from, to)) {
            return isValidEnPassant(from, to);
        }
        if (isPromotion(from, to)) {
            return isValidPromotion(from, to);
        }
        return false;
    }
    
    /**
     * Effectue un coup spécial.
     * @param from la position de départ
     * @param to la position d'arrivée
     * @param promotionPieceType le type de pièce pour la promotion (peut être null)
     * @return true si le coup a été effectué
     */
    public boolean executeSpecialMove(Position from, Position to, PieceType promotionPieceType) {
        if (!isValidSpecialMove(from, to)) {
            return false;
        }
        
        if (isCastling(from, to)) {
            return executeCastling(from, to);
        }
        if (isEnPassant(from, to)) {
            return executeEnPassant(from, to);
        }
        if (isPromotion(from, to)) {
            return executePromotion(from, to, promotionPieceType);
        }
        
        return false;
    }
    
    /**
     * Vérifie si un coup est un roque.
     * @param from la position de départ
     * @param to la position d'arrivée
     * @return true si c'est un roque
     */
    public boolean isCastling(Position from, Position to) {
        Piece piece = board.getPieceAt(from);
        if (piece == null || !piece.getType().isKing()) {
            return false;
        }
        
        King king = (King) piece;
        int rowDistance = from.getRowDistance(to);
        int columnDistance = from.getColumnDistance(to);
        
        // Le roque implique un déplacement horizontal de 2 cases
        return rowDistance == 0 && columnDistance == 2 && king.canCastle(board);
    }
    
    /**
     * Vérifie si un roque est valide.
     * @param from la position de départ du roi
     * @param to la position d'arrivée du roi
     * @return true si le roque est valide
     */
    private boolean isValidCastling(Position from, Position to) {
        Piece piece = board.getPieceAt(from);
        if (piece == null || !piece.getType().isKing()) {
            return false;
        }
        
        King king = (King) piece;
        Color color = king.getColor();
        
        // Vérifier que le roi n'est pas en échec
        if (board.isInCheck(color)) {
            return false;
        }
        
        // Déterminer le côté du roque
        boolean isKingside = to.getColumn() > from.getColumn();
        
        if (isKingside) {
            return king.canCastleKingside(board);
        } else {
            return king.canCastleQueenside(board);
        }
    }
    
    /**
     * Effectue un roque.
     * @param from la position de départ du roi
     * @param to la position d'arrivée du roi
     * @return true si le roque a été effectué
     */
    private boolean executeCastling(Position from, Position to) {
        if (!isValidCastling(from, to)) {
            return false;
        }
        
        Piece piece = board.getPieceAt(from);
        King king = (King) piece;
        Color color = king.getColor();
        int row = color.getPieceStartRow();
        
        boolean isKingside = to.getColumn() > from.getColumn();
        
        // Déplacer le roi
        board.setPieceAt(from, null);
        board.setPieceAt(to, king);
        king.setPosition(to);
        king.markAsMoved();
        
        // Déplacer la tour
        Position rookFrom = new Position(row, isKingside ? 7 : 0);
        Position rookTo = new Position(row, isKingside ? 5 : 3);
        Piece rook = board.getPieceAt(rookFrom);
        
        if (rook != null && rook.getType().isRook()) {
            board.setPieceAt(rookFrom, null);
            board.setPieceAt(rookTo, rook);
            rook.setPosition(rookTo);
            rook.markAsMoved();
        }
        
        return true;
    }
    
    /**
     * Vérifie si un coup est une prise en passant.
     * @param from la position de départ
     * @param to la position d'arrivée
     * @return true si c'est une prise en passant
     */
    public boolean isEnPassant(Position from, Position to) {
        Piece piece = board.getPieceAt(from);
        if (piece == null || !piece.getType().isPawn()) {
            return false;
        }
        
        // Vérifier avec l'historique des coups si la prise en passant est possible
        return moveHistory.canEnPassant(from, to);
    }
    
    /**
     * Vérifie si une prise en passant est valide.
     * @param from la position de départ du pion
     * @param to la position d'arrivée du pion
     * @return true si la prise en passant est valide
     */
    private boolean isValidEnPassant(Position from, Position to) {
        Piece piece = board.getPieceAt(from);
        if (piece == null || !piece.getType().isPawn()) {
            return false;
        }
        
        // Vérifier que le coup ne met pas le roi en échec
        Color color = piece.getColor();
        if (board.wouldBeInCheckAfterMove(from, to, color)) {
            return false;
        }
        
        return moveHistory.canEnPassant(from, to);
    }
    
    /**
     * Effectue une prise en passant.
     * @param from la position de départ du pion
     * @param to la position d'arrivée du pion
     * @return true si la prise en passant a été effectuée
     */
    private boolean executeEnPassant(Position from, Position to) {
        if (!isValidEnPassant(from, to)) {
            return false;
        }
        
        Piece piece = board.getPieceAt(from);
        Position capturedPawnPosition = moveHistory.getEnPassantCapturedPawnPosition(to);
        
        if (capturedPawnPosition == null) {
            return false;
        }
        
        Piece capturedPawn = board.getPieceAt(capturedPawnPosition);
        
        // Déplacer le pion
        board.setPieceAt(from, null);
        board.setPieceAt(to, piece);
        piece.setPosition(to);
        piece.markAsMoved();
        
        // Capturer le pion adverse
        if (capturedPawn != null) {
            board.setPieceAt(capturedPawnPosition, null);
            board.addCapturedPiece(capturedPawn);
        }
        
        return true;
    }
    
    /**
     * Vérifie si un coup est une promotion.
     * @param from la position de départ
     * @param to la position d'arrivée
     * @return true si c'est une promotion
     */
    public boolean isPromotion(Position from, Position to) {
        Piece piece = board.getPieceAt(from);
        if (piece == null || !piece.getType().isPawn()) {
            return false;
        }
        
        Color color = piece.getColor();
        
        // Pour les blancs, promotion si on va vers la ligne 7
        // Pour les noirs, promotion si on va vers la ligne 0
        int promotionRow = color == Color.WHITE ? 7 : 0;
        int currentRow = from.getRow();
        int targetRow = to.getRow();
        
        // Le pion doit être sur la ligne juste avant la promotion et se déplacer vers la ligne de promotion
        return currentRow == (promotionRow - color.getPawnDirection()) && targetRow == promotionRow;
    }
    
    /**
     * Vérifie si une promotion est valide.
     * @param from la position de départ du pion
     * @param to la position d'arrivée du pion
     * @return true si la promotion est valide
     */
    private boolean isValidPromotion(Position from, Position to) {
        Piece piece = board.getPieceAt(from);
        if (piece == null || !piece.getType().isPawn()) {
            return false;
        }
        
        Pawn pawn = (Pawn) piece;
        
        // Vérifier que c'est bien une promotion
        if (!isPromotion(from, to)) {
            return false;
        }
        
        // Vérifier que le mouvement de base est valide
        if (!pawn.canMoveTo(to, board)) {
            return false;
        }
        
        // Vérifier que le coup ne met pas le roi en échec
        Color color = piece.getColor();
        if (board.wouldBeInCheckAfterMove(from, to, color)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Effectue une promotion.
     * @param from la position de départ du pion
     * @param to la position d'arrivée du pion
     * @param promotionPieceType le type de pièce pour la promotion (dame par défaut)
     * @return true si la promotion a été effectuée
     */
    private boolean executePromotion(Position from, Position to, PieceType promotionPieceType) {
        if (!isValidPromotion(from, to)) {
            return false;
        }
        
        Piece piece = board.getPieceAt(from);
        if (piece == null || !piece.getType().isPawn()) {
            return false;
        }
        
        Color color = piece.getColor();
        
        // Par défaut, promouvoir en dame
        if (promotionPieceType == null) {
            promotionPieceType = PieceType.QUEEN;
        }
        
        // Créer la nouvelle pièce
        Piece newPiece = createPromotedPiece(promotionPieceType, color, to);
        
        // Capturer la pièce à la position d'arrivée si elle existe
        Piece capturedPiece = board.getPieceAt(to);
        if (capturedPiece != null) {
            board.getCapturedPieces().add(capturedPiece);
        }
        
        // Remplacer le pion par la nouvelle pièce
        board.setPieceAt(from, null);
        board.setPieceAt(to, newPiece);
        
        return true;
    }
    
    /**
     * Crée une pièce promue.
     * @param pieceType le type de pièce
     * @param color la couleur
     * @param position la position
     * @return la nouvelle pièce
     */
    private Piece createPromotedPiece(PieceType pieceType, Color color, Position position) {
        return switch (pieceType) {
            case QUEEN -> new Queen(color, position);
            case ROOK -> new Rook(color, position);
            case BISHOP -> new Bishop(color, position);
            case KNIGHT -> new Knight(color, position);
            default -> new Queen(color, position); // Par défaut, promouvoir en dame
        };
    }
    
    /**
     * Retourne tous les coups spéciaux possibles pour une pièce à une position donnée.
     * @param position la position de la pièce
     * @return un tableau des positions de coups spéciaux
     */
    public Position[] getSpecialMoves(Position position) {
        List<Position> specialMoves = new ArrayList<>();
        Piece piece = board.getPieceAt(position);
        
        if (piece == null) {
            return new Position[0];
        }
        
        // Roque
        if (piece.getType().isKing()) {
            King king = (King) piece;
            if (king.canCastleKingside(board)) {
                Position kingsideCastle = new Position(position.getRow(), position.getColumn() + 2);
                if (kingsideCastle.isValid()) {
                    specialMoves.add(kingsideCastle);
                }
            }
            if (king.canCastleQueenside(board)) {
                Position queensideCastle = new Position(position.getRow(), position.getColumn() - 2);
                if (queensideCastle.isValid()) {
                    specialMoves.add(queensideCastle);
                }
            }
        }
        
        // Prise en passant
        if (piece.getType().isPawn()) {
            Color pieceColor = piece.getColor();
            int enPassantRow = pieceColor == Color.WHITE ? 4 : 3;
            if (position.getRow() == enPassantRow) {
                int pawnDirection = pieceColor.getPawnDirection();
                for (int colOffset = -1; colOffset <= 1; colOffset += 2) {
                    Position enPassantTarget = position.add(pawnDirection, colOffset);
                    if (enPassantTarget != null && moveHistory.canEnPassant(position, enPassantTarget)) {
                        specialMoves.add(enPassantTarget);
                    }
                }
            }
        }
        
        return specialMoves.toArray(new Position[0]);
    }
}
