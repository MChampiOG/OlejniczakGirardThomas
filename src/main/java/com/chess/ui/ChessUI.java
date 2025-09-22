package com.chess.ui;

import com.chess.board.Board;
import com.chess.board.Piece;
import com.chess.board.Position;

import javax.swing.*;
import java.awt.*;

public class ChessUI extends JFrame {
    private Board board;
    private JPanel boardPanel;

    public ChessUI() {
        setTitle("Chess Game");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        boardPanel = new JPanel(new GridLayout(8, 8));
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
        board = new Board();
        board.initializeBoard();
        updateBoardUI();
    }

    private void updateBoardUI() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position position = new Position(i, j);
                Piece piece = board.getPieceAt(position);
                JButton button = (JButton) boardPanel.getComponent(i * 8 + j);
                if (piece != null) {
                    button.setText(piece.getSymbol());
                } else {
                    button.setText("");
                }
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ChessUI chessUI = new ChessUI();
            chessUI.setVisible(true);
        });
    }
}
