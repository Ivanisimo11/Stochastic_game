package game;

import java.util.Stack;

public class Bot {
    private int[][] board;

    public Bot(int[][] board) {
        this.board = board;
    }

    public int[] getBestMove(Stack<int[][]> positions) {
        board = positions.get(1);
        double bestScore = Double.NEGATIVE_INFINITY;
        int[] bestMove = new int[]{-1, -1};
        double[][] allScores = new double[9][9];

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (board[x][y] == 0) {
                    double score = calculateScore(x, y);
                    if (score > bestScore) {
                        allScores[x][y] = score;
                        bestScore = score;
                        bestMove[0] = x;
                        bestMove[1] = y;
                    }
                }
            }
        }
        for (int i = 0; i < allScores.length; i++) {
            for (int j = 0; j < allScores[i].length; j++) {
                if (allScores[i][j] != bestScore) {
                    return bestMove;
                }
            }
        }
        int[] enemyStone = new int[2];
        int maxLibertyCounter = 0;
        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                if (board[x][y] == 1) {
                    int tempLiberty = getLibertyCount(x, y);
                    if (tempLiberty != 0) {
                        if (tempLiberty > maxLibertyCounter) {
                            maxLibertyCounter = tempLiberty;
                            enemyStone[0] = x;
                            enemyStone[1] = y;
                        }
                    }
                }
            }
        }
        if (maxLibertyCounter!=0){
            bestMove = getLiberty(enemyStone[0],enemyStone[1]);
        }
        return bestMove;
    }


    public int[] getLiberty(int x, int y) {
        int[] liberty = new int[2];
        if (x > 0 && board[x - 1][y] == 0) {
            liberty[0] = x - 1;
            liberty[1] = y;
            return liberty;
        }
        if (y > 0 && board[x][y - 1] == 0) {
            liberty[0] = x;
            liberty[1] = y - 1;
            return liberty;
        }
        if (x < 8 && board[x + 1][y] == 0) {
            liberty[0] = x + 1;
            liberty[1] = y;
            return liberty;
        }
        if (y < 8 && board[x][y + 1] == 0) {
            liberty[0] = x;
            liberty[1] = y + 1;
            return liberty;
        }

        return null;
    }


    private double calculateScore(int x, int y) {

        int[][] tempBoard = new int[9][9];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                tempBoard[i][j] = board[i][j];
            }
        }

        if (tempBoard[x][y] != 0) {
            return 0;
        }

        tempBoard[x][y] = 2;

        int botScore = calculateBotScore(tempBoard);
        int playerScore = calculatePlayerScore(tempBoard);

        return (botScore - playerScore) / (double) (botScore + playerScore + 1);
    }

    private int calculateBotScore(int[][] tempBoard) {
        int botScore = 0;
        for (int i = 0; i < tempBoard.length; i++) {
            for (int j = 0; j < tempBoard[i].length; j++) {
                if (tempBoard[i][j] == 1) {
                    botScore++;
                }
            }
        }
        return botScore;
    }

    private int calculatePlayerScore(int[][] tempBoard) {
        int playerScore = 0;
        for (int i = 0; i < tempBoard.length; i++) {
            for (int j = 0; j < tempBoard[i].length; j++) {
                if (tempBoard[i][j] == 2) {
                    playerScore++;
                }
            }
        }
        playerScore = playerScore - surroundedBlackRocks(tempBoard);
        return playerScore;
    }

    public int surroundedBlackRocks(int[][] tempBoard) {
        int count = 0;
        for (int i = 0; i < tempBoard.length; i++) {
            for (int j = 0; j < tempBoard[i].length; j++) {
                if (tempBoard[i][j] == 1) {
                    int sameColor = 0;
                    int anotherColor = 0;
                    int[] neighbors = getNeighbors(tempBoard,i, j);
                    for (int i1 = 0; i1 < neighbors.length; i1++) {
                        if (neighbors[i1] == 1) {
                            sameColor++;
                        } else if (neighbors[i1] == 2) {
                            anotherColor++;
                        }
                    }
                    if (anotherColor > sameColor) {
                        count++;
                        tempBoard[i][j] = 0;
                    }
                }
            }
        }
        return count;
    }

    private int[] getNeighbors(int[][] tempBoard, int x, int y) {
        int[] neighbors = new int[4];
        if (x > 0) {
            neighbors[0] = tempBoard[x - 1][y];
        }
        if (x < 8) {
            neighbors[1] = tempBoard[x + 1][y];
        }
        if (y > 0) {
            neighbors[2] = tempBoard[x][y - 1];
        }
        if (y < 8) {
            neighbors[3] = tempBoard[x][y + 1];
        }
        return neighbors;
    }


    private int getLibertyCount(int x, int y) {
        int count = 0;

        if (x != 8) {
            if (isEmpty(x + 1, y)) {
                count++;
            }
        }
        if (x != 0) {
            if (isEmpty(x - 1, y)) {
                count++;
            }
        }
        if (y != 8) {
            if (isEmpty(x, y + 1)) {
                count++;
            }
        }
        if (y != 0) {
            if (isEmpty(x, y - 1)) {
                count++;
            }
        }
        return count;
    }

    private boolean isEmpty(int x, int y) {
        return board[x][y] == 0;
    }


}
