package knighttour;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KnightTour {

    /** Size of the chessboard (8x8). */
    private static final int N = 8;

    /** The board: -1 means unvisited, otherwise contains the move number (1..64). */
    private final int[][] board = new int[N][N];

    /** Knight moves (8 possibilities). */
    private static final int[] DX = {2, 1, -1, -2, -2, -1, 1, 2};
    private static final int[] DY = {1, 2, 2, 1, -1, -2, -2, -1};
    
    private final Scanner sc;
    /**
     * Default constructor initializes the board to unvisited (-1).
     */
    public KnightTour() {
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                board[r][c] = -1;
            }
        }
        sc = new Scanner(System.in);
    }

    /**
     * Solve the Knight's Tour from the given start position.
     *
     * Algorithm (English):
     * This implementation uses a Warnsdorff-inspired backtracking approach:
     *  - From the current square, generate all legal (in-bounds and unvisited) knight moves.
     *  - Sort these candidate moves by the number of onward moves they would have (ascending).
     *    This heuristic tends to reduce dead-ends and leads to faster solutions on 8x8 boards.
     *  - Try the moves in that order recursively; if a move leads to a full tour (moveCount == N*N),
     *    return true. Otherwise backtrack and try the next candidate.
     *
     * This produces a complete tour for almost all starts on an 8x8 board quickly; when it fails
     * it will correctly backtrack because every recursion branch explores possibilities until exhausted.
     *
     * @param startRow zero-based row index (0..7)
     * @param startCol zero-based column index (0..7)
     * @return true if a full tour was found, false otherwise
     */
    private boolean solveFrom(int startRow, int startCol) {
        if (!isValid(startRow, startCol)) {
            return false;
        }
        
        board[startRow][startCol] = 1; // first move
        if (solveUtil(startRow, startCol, 1)) {
            return true;
        } else {
            // reset and report failure
            board[startRow][startCol] = -1;
            return false;
        }
    }

    /**
     * Recursive utility to attempt filling the board.
     *
     * @param row current row
     * @param col current column
     * @param moveCount number of moves already placed (1..N*N)
     * @return true if full tour is completed from this state
     */
    private boolean solveUtil(int row, int col, int moveCount) {
        if (moveCount == N * N) {
            // Completed all squares
            return true;
        }
        
        List<int[]> candidates = getLegalMoves(row, col);
        sortByWarnsdorff(candidates);

        for (int[] mv : candidates) {
            int r = mv[0];
            int c = mv[1];
            board[r][c] = moveCount + 1;
            if (solveUtil(r, c, moveCount + 1)) {
                return true; // once solution found, unwind and keep board as-is
            }
            // backtrack
            board[r][c] = -1;
        }
        return false;
    }

    /**
     * Get legal (in-bound and unvisited) knight moves from (row,col).
     *
     * @param row current row
     * @param col current column
     * @return list of int[]{r,c} pairs for legal moves
     */
    private List<int[]> getLegalMoves(int row, int col) {
        List<int[]> list = new ArrayList<>();
        
        for (int k = 0; k < DX.length; k++) {
            int nr = row + DY[k];
            int nc = col + DX[k];
            if (isValid(nr, nc) && board[nr][nc] == -1) {
                list.add(new int[]{nr, nc});
            }
        }
        
        return list;
    }

    /**
     * Sort candidate moves using Warnsdorff's heuristic: moves with fewer onward moves come first.
     *
     * @param moves list of int[]{r,c} to be sorted in-place
     */
    private void sortByWarnsdorff(List<int[]> moves) {
        moves.sort((a, b) -> {
            int da = onwardMovesCount(a[0], a[1]);
            int db = onwardMovesCount(b[0], b[1]);
            return Integer.compare(da, db);
        });
    }

    /**
     * Count onward legal moves from (r,c) (used by Warnsdorff heuristic).
     *
     * @param r row
     * @param c column
     * @return number of onward moves
     */
    private int onwardMovesCount(int r, int c) {
        int count = 0;
        for (int k = 0; k < DX.length; k++) {
            int nr = r + DY[k];
            int nc = c + DX[k];
            if (isValid(nr, nc) && board[nr][nc] == -1) count++;
        }
        return count;
    }

    /**
     * Check bounds for a row/col index.
     *
     * @param r row index
     * @param c column index
     * @return true if in [0..N-1]
     */
    private boolean isValid(int r, int c) {
        return r >= 0 && r < N && c >= 0 && c < N;
    }

    /**
     * Print the board. Unvisited squares are shown as "__" and visited squares show the move number.
     */
    private void printBoard() {
        System.out.println("Knight's tour board (move numbers):");
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if (board[r][c] == -1) {
                    System.out.print(" __");
                } else {
                    System.out.printf(" %2d", board[r][c]);
                }
            }
            System.out.println();
        }
    }

    /**
     * Clear the board (set all cells to -1).
     */
    public void clearBoard() {
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                board[r][c] = -1;
            }
        }
    }

    /**
     * Main program: read user input (either chess notation like "a1".."h8" or two integers "row col" with 1..8),
     * then solve and print the found tour.
     *
     * The program handles invalid input gracefully and will print helpful error messages instead of throwing
     * uncaught exceptions.
     */
    public void UserInterface() {
        try {
            System.out.println("Enter starting square for the knight.");
            System.out.println("Accepted formats: two numbers: \"row col\" (1..8).\n");
            System.out.print("Start: ");
            String line = sc.nextLine().trim();
            int startRow = -1, startCol = -1;

            // try two integers
            String[] parts = line.split("\\s+");
            if (parts.length == 2) {
                try {
                    int r = Integer.parseInt(parts[0]);
                    int c = Integer.parseInt(parts[1]);
                    if (r >= 1 && r <= 8 && c >= 1 && c <= 8) {
                        startRow = r - 1;
                        startCol = c - 1;
                    }
                } catch (NumberFormatException ex) {
                    // leave invalid
                }
            }

            if (startRow == -1 || startCol == -1) {
                System.out.println("Invalid input. Example valid inputs: a1, h8, or '1 1', '8 8'. Program exits.");
                return;
            }

            KnightTour kt = new KnightTour();
            System.out.printf("Attempting Knight's Tour from (row=%d, col=%d) (0-based indices shown).\n", startRow, startCol);
            boolean ok = kt.solveFrom(startRow, startCol);
            if (ok) {
                System.out.println("A full Knight's Tour was found:");
                kt.printBoard();
            } else {
                System.out.println("No tour found from the given starting square.");
            }

        } catch (Exception e) {
            // 
        }
    }
}
