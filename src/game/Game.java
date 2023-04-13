package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.Stack;

class Game extends JFrame {
    private final Board board;
    private final Bot bot;
    private final int[][] state;
    private int currentPlayer;
    private boolean gameOver;
    private int previousHash;
    private final Stack<int[][]> positions;
    private int whitePointCounter = 0;
    private int blackPointCounter = 0;

    public Game() {
        state = new int[9][9];
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state.length; j++) {
                state[i][j] = 0;
            }
        }
        setTitle("Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        board = new Board();
        bot = new Bot(state);
        currentPlayer = 1;
        gameOver = false;

        add(board);
        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        positions = new Stack<>();

        int[][] initialPosition = new int[9][9];
        positions.push(initialPosition);
    }

    public boolean isMoveValid(int x, int y) {
        if (x < 0 || x >= 9 || y < 0 || y >= 9) {
            gameOver = true;
        }
        return state[x][y] == 0;
    }

    private int getBoardHash() {
        int hash = 0;
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                hash += state[i][j];
            }
        }
        return hash;
    }

    public void makeMove(int x, int y) {
        if (!isMoveValid(x, y)) {
            throw new IllegalArgumentException("You can't do it");
        }
        if (previousHash == -1) {
            previousHash = getBoardHash();
        }
        state[x][y] = currentPlayer;
        positions.push((state));
        if (currentPlayer == 2) {
            board.paintBotStone(x, y);
        }
        checkGameStatus();
        repaint();
    }

    private void drawGameOver() {
        JLabel gameOverLabel = new JLabel("Game Over", JLabel.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setBounds(board.getWidth() / 2 - 100, board.getHeight() / 2 - 12, 200, 24);
        JLabel blackScoreLabel = new JLabel("Black:" + blackPointCounter, JLabel.CENTER);
        blackScoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        blackScoreLabel.setForeground(Color.RED);
        blackScoreLabel.setBounds(board.getWidth() / 2 - 100, board.getHeight() / 2 + 12, 200, 24);
        JLabel whiteScoreLabel = new JLabel("White:" + whitePointCounter, JLabel.CENTER);
        whiteScoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        whiteScoreLabel.setForeground(Color.RED);
        whiteScoreLabel.setBounds(board.getWidth() / 2 - 100, board.getHeight() / 2 + 36, 200, 24);
        board.add(gameOverLabel);
        board.add(blackScoreLabel);
        board.add(whiteScoreLabel);
    }


    private void checkGameStatus() {
        int counter = 0;
        for (int[] ints : state) {
            for (int anInt : ints) {
                if (anInt == 0) {
                    counter++;
                }
            }
        }
        if (counter == 0) {
            gameOver = true;
        }
    }

    private void playerMove(int x, int y) {
        if (!gameOver && currentPlayer == 1 && isMoveValid(x, y)) {
            makeMove(x, y);
            if (!isGameOver()) {
                currentPlayer = 2;
                int[] botMove = bot.getBestMove(positions);
                makeMove(botMove[0], botMove[1]);
                currentPlayer = 1;
            }
            if (isGameOver()) {
                winnerDetermination();
                drawGameOver();
            }
        }
    }

    public void winnerDetermination() {
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                int temp = state[i][j];
                if (temp == 1) {
                    blackPointCounter++;
                } else if (temp == 2) {
                    int[] neighbors = getNeighbors(i, j);
                    int counter = 0;
                    for (int k = 0; k < neighbors.length; k++) {
                        if (neighbors[k] == 2) {
                            counter++;
                        }
                    }
                    if (counter > 3) {
                        whitePointCounter = whitePointCounter + 2;
                    }
                    whitePointCounter++;
                }
            }
        }
        blackPointCounter = (blackPointCounter - surroundedBlackRocks()) * 2;
    }

    public int surroundedBlackRocks() {
        int count = 0;
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                if (state[i][j] == 1) {
                    int sameColor = 0;
                    int anotherColor = 0;
                    int[] neighbors = getNeighbors(i, j);
                    for (int i1 = 0; i1 < neighbors.length; i1++) {
                        if (neighbors[i1] == 1) {
                            sameColor++;
                        } else if (neighbors[i1] == 2) {
                            anotherColor++;
                        }
                    }
                    if (anotherColor > sameColor) {
                        count++;
                        state[i][j] = 0;
                    }
                }
            }
        }
        return count;
    }

    private int[] getNeighbors(int x, int y) {
        int[] neighbors = new int[4];
        if (x > 0) {
            neighbors[0] = state[x - 1][y];
        }
        if (x < 8) {
            neighbors[1] = state[x + 1][y];
        }
        if (y > 0) {
            neighbors[2] = state[x][y - 1];
        }
        if (y < 8) {
            neighbors[3] = state[x][y + 1];
        }
        return neighbors;
    }


    public boolean isGameOver() {
        return gameOver;
    }

    public void start() {
        board.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (!gameOver) {
                    playerMove(e.getX() / 40, e.getY() / 40);
                }
            }
        });
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();
    }
}
