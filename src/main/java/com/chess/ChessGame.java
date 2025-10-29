package com.chess;

import com.chess.ui.ChessUI;

/**
 * Point d'entrée principal du jeu d'échecs.
 */
public class ChessGame {
    public static void main(String[] args) {
        // Lancer l'interface graphique
        javax.swing.SwingUtilities.invokeLater(() -> {
            ChessUI chessUI = new ChessUI();
            chessUI.setVisible(true);
        });
    }
}
