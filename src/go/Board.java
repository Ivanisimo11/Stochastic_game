package go;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class Board extends JPanel implements MouseListener {
    private final int size = 9;
    private final int[][] board = new int[size][size];
    private final int currentPlayer = 1;

    public Board() {
        setPreferredSize(new Dimension(360, 360));
        setBackground(new Color(200, 160, 60));
        addMouseListener(this);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                g.drawRect(i * 40, j * 40, 40, 40);
                if (board[i][j] == 1) {
                    g.setColor(Color.BLACK);
                    g.fillOval(i * 40 + 5, j * 40 + 5, 30, 30);
                } else if (board[i][j] == 2) {
                    g.setColor(Color.WHITE);
                    g.fillOval(i * 40 + 5, j * 40 + 5, 30, 30);
                }
            }
        }
    }

    public void mouseClicked(MouseEvent e) {
        int x = e.getX() / 40;
        int y = e.getY() / 40;

        if (board[x][y] == 0) {
            board[x][y] = currentPlayer;
            repaint();
        }
    }

    public void paintBotStone(int x, int y) {
        int[][] board = getBoard();
        board[x][y] = 2;

        repaint();
    }

    public int[][] getBoard() {
        return board;
    }


    public void mousePressed(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}
