package com.chess.ui;

import com.chess.board.Board;
import com.chess.board.Color;
import com.chess.board.Piece;
import com.chess.board.PieceType;
import com.chess.board.Position;
import com.chess.rules.GameStateChecker;
import com.chess.rules.MoveHistory;
import com.chess.rules.MoveValidator;
import com.chess.rules.SpecialMovesHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessUI extends JFrame {
    private Board board;
    private MoveHistory moveHistory;
    private MoveValidator moveValidator;
    private SpecialMovesHandler specialMovesHandler;
    private GameStateChecker gameStateChecker;
    private JPanel boardPanel;
    private JButton[][] buttons;
    private Position selectedPosition;
    private JLabel statusLabel;

    public ChessUI() {
        setTitle("Chess Game - Déplacement Simple");
        setSize(800, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Créer le panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Créer le plateau
        boardPanel = new JPanel(new GridLayout(8, 8));
        buttons = new JButton[8][8];
        
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(80, 80));
                button.setFont(new Font("Arial", Font.BOLD, 24));
                
                // Couleurs alternées
                if ((i + j) % 2 == 0) {
                    button.setBackground(new java.awt.Color(240, 217, 181)); // Cases claires
                } else {
                    button.setBackground(new java.awt.Color(181, 136, 99)); // Cases sombres
                }
                
                button.setBorderPainted(false);
                button.setFocusPainted(false);
                
                final int row = i;
                final int col = j;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleSquareClick(row, col);
                    }
                });
                
                buttons[i][j] = button;
                boardPanel.add(button);
            }
        }
        
        // Créer le label de statut
        statusLabel = new JLabel("Cliquez sur une pièce pour la sélectionner", JLabel.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 16));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(statusLabel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Initialiser le plateau et les règles
        board = new Board();
        board.initializeBoard();
        moveHistory = new MoveHistory();
        moveValidator = new MoveValidator(board, moveHistory);
        specialMovesHandler = new SpecialMovesHandler(board, moveHistory);
        gameStateChecker = new GameStateChecker(board, moveValidator);
        
        updateBoardUI();
        updateGameStatus();
    }

    private void updateBoardUI() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position position = new Position(i, j);
                Piece piece = board.getPieceAt(position);
                JButton button = buttons[i][j];
                
                if (piece != null) {
                    button.setText(piece.getSymbol());
                    // Couleur du texte selon la couleur de la pièce
                    if (piece.getColor() == com.chess.board.Color.WHITE) {
                        button.setForeground(java.awt.Color.WHITE);
                    } else {
                        button.setForeground(java.awt.Color.BLACK);
                    }
                } else {
                    button.setText("");
                }
                
                // Réinitialiser la couleur de fond
                if ((i + j) % 2 == 0) {
                    button.setBackground(new java.awt.Color(240, 217, 181));
                } else {
                    button.setBackground(new java.awt.Color(181, 136, 99));
                }
            }
        }
    }
    
    private void handleSquareClick(int row, int col) {
        Position clickedPosition = new Position(row, col);
        Piece clickedPiece = board.getPieceAt(clickedPosition);
        
        // Si aucune pièce n'est sélectionnée
        if (selectedPosition == null) {
            // Si on clique sur une pièce
            if (clickedPiece != null) {
                // Vérifier que c'est le tour du joueur qui possède cette pièce
                if (clickedPiece.getColor() == board.getCurrentPlayer()) {
                    selectedPosition = clickedPosition;
                    highlightSelectedSquare();
                    updateStatus("Pièce sélectionnée: " + clickedPiece.getFullName() + 
                               " - Cliquez sur une case de destination");
                } else {
                    updateStatus("Ce n'est pas votre tour ! C'est le tour des " + 
                               board.getCurrentPlayer().getDisplayName());
                }
            } else {
                updateStatus("Aucune pièce sur cette case. Cliquez sur une pièce.");
            }
        } else {
            // Une pièce est déjà sélectionnée
            if (selectedPosition.equals(clickedPosition)) {
                // Désélectionner la pièce
                clearHighlights();
                selectedPosition = null;
                updateStatus("Pièce désélectionnée. Cliquez sur une pièce pour la sélectionner.");
            } else {
                // Déplacer la pièce
                movePiece(selectedPosition, clickedPosition);
            }
        }
    }
    
    private void movePiece(Position from, Position to) {
        Piece piece = board.getPieceAt(from);
        if (piece == null) {
            clearHighlights();
            selectedPosition = null;
            updateStatus("Erreur : Aucune pièce à déplacer.");
            return;
        }
        
        // Vérifier si c'est un coup spécial
        if (specialMovesHandler.isSpecialMove(from, to)) {
            PieceType promotionType = null;
            if (specialMovesHandler.isPromotion(from, to)) {
                // Demander au joueur quelle pièce choisir pour la promotion
                promotionType = askForPromotion();
                if (promotionType == null) {
                    updateStatus("Promotion annulée.");
                    return;
                }
            }
            
            // Récupérer la pièce capturée avant le mouvement (si prise en passant, elle est à une position différente)
            Piece capturedPiece = null;
            if (specialMovesHandler.isEnPassant(from, to)) {
                Position capturedPos = moveHistory.getEnPassantCapturedPawnPosition(to);
                if (capturedPos != null) {
                    capturedPiece = board.getPieceAt(capturedPos);
                }
            } else {
                capturedPiece = board.getPieceAt(to);
            }
            
            if (specialMovesHandler.executeSpecialMove(from, to, promotionType)) {
                // Enregistrer le coup dans l'historique
                moveHistory.addMove(from, to, piece, capturedPiece, board);
                board.switchPlayer();
                
                clearHighlights();
                selectedPosition = null;
                updateBoardUI();
                updateGameStatus();
                return;
            } else {
                updateStatus("Coup spécial invalide !");
                return;
            }
        }
        
        // Valider le coup normal avec MoveValidator
        if (!moveValidator.isValidMove(from, to)) {
            updateStatus("Coup invalide ! Le roi ne peut pas être mis en échec.");
            return;
        }
        
        // Effectuer le déplacement
        Piece capturedPiece = board.getPieceAt(to);
        board.movePiece(from, to);
        
        // Enregistrer le coup dans l'historique
        moveHistory.addMove(from, to, piece, capturedPiece, board);
        
        // Mettre à jour l'affichage
        clearHighlights();
        selectedPosition = null;
        updateBoardUI();
        updateGameStatus();
    }
    
    private PieceType askForPromotion() {
        String[] options = {"Dame", "Tour", "Fou", "Cavalier"};
        int choice = JOptionPane.showOptionDialog(
            this,
            "En quelle pièce souhaitez-vous promouvoir votre pion ?",
            "Promotion du pion",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        
        return switch (choice) {
            case 0 -> PieceType.QUEEN;
            case 1 -> PieceType.ROOK;
            case 2 -> PieceType.BISHOP;
            case 3 -> PieceType.KNIGHT;
            default -> null;
        };
    }
    
    private void updateGameStatus() {
        GameStateChecker.GameState state = gameStateChecker.getGameState(moveHistory);
        Color currentPlayer = board.getCurrentPlayer();
        
        switch (state) {
            case CHECKMATE:
                updateStatus("Échec et mat ! Les " + currentPlayer.opposite().getDisplayName() + " ont gagné !");
                break;
            case STALEMATE:
                updateStatus("Pat ! La partie est nulle.");
                break;
            case DRAW:
                updateStatus("Nulle ! " + state.getDescription());
                break;
            case CHECK:
                updateStatus("Échec ! Tour des " + currentPlayer.getDisplayName());
                break;
            default:
                updateStatus("Tour des " + currentPlayer.getDisplayName());
                break;
        }
    }
    
    private void highlightSelectedSquare() {
        if (selectedPosition != null) {
            JButton button = buttons[selectedPosition.getRow()][selectedPosition.getColumn()];
            button.setBackground(java.awt.Color.YELLOW);
        }
    }
    
    private void clearHighlights() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton button = buttons[i][j];
                if ((i + j) % 2 == 0) {
                    button.setBackground(new java.awt.Color(240, 217, 181));
                } else {
                    button.setBackground(new java.awt.Color(181, 136, 99));
                }
            }
        }
    }
    
    private void updateStatus(String message) {
        statusLabel.setText(message);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChessUI chessUI = new ChessUI();
            chessUI.setVisible(true);
        });
    }
}