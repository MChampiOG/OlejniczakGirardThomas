package com.chess.ui;

import javax.swing.*;
import java.awt.*;

public class ChessUI extends JFrame {
    public ChessUI() {
        setTitle("Chess Game");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel boardPanel = new JPanel(new GridLayout(8, 8));
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                JButton button = new JButton();
                if ((i + j) % 2 == 0) {
                    button.setBackground(Color.BLACK);
                } else {
                    button.setBackground(Color.WHITE);
                }
                button.setBorderPainted(false);
                boardPanel.add(button);
            }
        }
        add(boardPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChessUI chessUI = new ChessUI();
            chessUI.setVisible(true);
        });
    }
}
