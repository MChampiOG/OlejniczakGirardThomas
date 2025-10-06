package com.chess.ui;

import com.chess.board.Board;
import com.chess.board.Piece;
import com.chess.board.Position;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChessUI extends JFrame {
    private Board board;
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
                    button.setBackground(new Color(240, 217, 181)); // Cases claires
                } else {
                    button.setBackground(new Color(181, 136, 99)); // Cases sombres
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
        
        board = new Board();
        board.initializeBoard();
        updateBoardUI();
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
                        button.setForeground(Color.WHITE);
                    } else {
                        button.setForeground(Color.BLACK);
                    }
                } else {
                    button.setText("");
                }
                
                // Réinitialiser la couleur de fond
                if ((i + j) % 2 == 0) {
                    button.setBackground(new Color(240, 217, 181));
                } else {
                    button.setBackground(new Color(181, 136, 99));
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
                selectedPosition = clickedPosition;
                highlightSelectedSquare();
                updateStatus("Pièce sélectionnée: " + clickedPiece.getFullName() + 
                           " - Cliquez sur une case de destination");
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
        
        // Déplacer la pièce simplement (sans validation des règles)
        Piece capturedPiece = board.getPieceAt(to);
        
        // Capturer la pièce à la position d'arrivée si elle existe
        if (capturedPiece != null) {
            board.getCapturedPieces().add(capturedPiece);
        }
        
        // Effectuer le déplacement
        board.setPieceAt(from, null);
        board.setPieceAt(to, piece);
        piece.setPosition(to);
        piece.markAsMoved();
        
        // Changer de joueur
        board.switchPlayer();
        
        // Mettre à jour l'affichage
        clearHighlights();
        selectedPosition = null;
        updateBoardUI();
        updateStatus("Pièce déplacée ! Tour des " + board.getCurrentPlayer().getDisplayName());
    }
    
    private void highlightSelectedSquare() {
        if (selectedPosition != null) {
            JButton button = buttons[selectedPosition.getRow()][selectedPosition.getColumn()];
            button.setBackground(Color.YELLOW);
        }
    }
    
    private void clearHighlights() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton button = buttons[i][j];
                if ((i + j) % 2 == 0) {
                    button.setBackground(new Color(240, 217, 181));
                } else {
                    button.setBackground(new Color(181, 136, 99));
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