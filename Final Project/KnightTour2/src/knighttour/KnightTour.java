package knighttour;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp logic chính cho giải thuật Knight's Tour.
 * Đã loại bỏ phần giao diện Console (UserInterface và Scanner)
 * để cho phép tích hợp với GUI.
 */
public class KnightTour {

    /** Size of the chessboard (8x8). */
    private static final int N = 8;

    /** The board: -1 means unvisited, otherwise contains the move number (1..64). */
    private final int[][] board = new int[N][N];

    /** Knight moves (8 possibilities). */
    private static final int[] DX = {2, 1, -1, -2, -2, -1, 1, 2};
    private static final int[] DY = {1, 2, 2, 1, -1, -2, -2, -1};

    /**
     * Default constructor initializes the board to unvisited (-1).
     */
    public KnightTour() {
        clearBoard(); // Khởi tạo bàn cờ
    }

    /**
     * Giải Knight's Tour từ vị trí bắt đầu.
     * (Đã đổi thành public để GUI có thể gọi)
     *
     * @param startRow zero-based row index (0..7)
     * @param startCol zero-based column index (0..7)
     * @return true if a full tour was found, false otherwise
     */
    public boolean solveFrom(int startRow, int startCol) {
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
     * Sort candidate moves using Warnsdorff's heuristic.
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
     */
    private boolean isValid(int r, int c) {
        return r >= 0 && r < N && c >= 0 && c < N;
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
     * Getter để GUI có thể đọc trạng thái bàn cờ.
     * @return mảng 2D của bàn cờ
     */
    public int[][] getBoard() {
        return board;
    }
}