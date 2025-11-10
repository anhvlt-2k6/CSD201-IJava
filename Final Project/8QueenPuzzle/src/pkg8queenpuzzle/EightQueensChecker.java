package pkg8queenpuzzle;

import java.util.Scanner;

public class EightQueensChecker {

    private final int SIZE = 8;
    private final boolean[][] board; // true = queen present, false = empty

    private final Scanner scanner;

    /**
     * Default constructor initializes an empty 8x8 board.
     */
    public EightQueensChecker() {
        this.board = new boolean[SIZE][SIZE];
        scanner = new Scanner(System.in);
    }

    /**
     * Validate that provided indices are within [0, SIZE-1].
     *
     * @param row 0-based row
     * @param col 0-based column
     * @throws IllegalArgumentException if indices are invalid
     */
    private void validateIndices(int row, int col) {
        if (row < 0 || row >= SIZE || col < 0 || col >= SIZE) {
            throw new IllegalArgumentException("Row and column must be between 0 and " + (SIZE - 1));
        }
    }

    /**
     * Place a queen at the given row and column (0-based).
     *
     * @param row 0-based row index (0..7)
     * @param col 0-based column index (0..7)
     * @throws IllegalArgumentException if row/col out of range or there's
     * already a queen
     */
    private void placeQueen(int row, int col) {
        validateIndices(row, col);
        if (board[row][col]) {
            throw new IllegalArgumentException("There is already a queen at that position.");
        }
        board[row][col] = true;
    }

    /**
     * Check whether it is safe to place a queen at (row, col) given the current
     * board state.
     *
     * Algorithm (English): - A queen attacks along the same row, same column,
     * and both diagonals. - To determine safety, check: 1) No queen exists in
     * the same row. 2) No queen exists in the same column. 3) No queen exists
     * on the four diagonals that pass through (row, col). - Return true only if
     * all checks find no existing queen.
     *
     * Complexity: O(N) where N = SIZE (checks scan at most 7 cells per
     * direction).
     *
     * @param row 0-based row index
     * @param col 0-based column index
     * @return true if safe to place a queen at (row, col); false otherwise
     */
    private boolean isSafe(int row, int col) {
        validateIndices(row, col);

        // If there is already a queen at the target, we consider "placing" invalid.
        if (board[row][col]) {
            return false;
        }

        // Check same row
        for (int c = 0; c < SIZE; c++) {
            if (board[row][c]) {
                return false;
            }
        }

        // Check same column
        for (int r = 0; r < SIZE; r++) {
            if (board[r][col]) {
                return false;
            }
        }

        // Check diagonals
        // top-left
        for (int r = row - 1, c = col - 1; r >= 0 && c >= 0; r--, c--) {
            if (board[r][c]) {
                return false;
            }
        }
        // top-right
        for (int r = row - 1, c = col + 1; r >= 0 && c < SIZE; r--, c++) {
            if (board[r][c]) {
                return false;
            }
        }
        // bottom-left
        for (int r = row + 1, c = col - 1; r < SIZE && c >= 0; r++, c--) {
            if (board[r][c]) {
                return false;
            }
        }
        // bottom-right
        for (int r = row + 1, c = col + 1; r < SIZE && c < SIZE; r++, c++) {
            if (board[r][c]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Print a map of safe positions (S = safe, X = not safe, Q = existing
     * queen).
     */
    private void printSafeMap() {
        boolean[][] safe = new boolean[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                safe[r][c] = isSafe(r, c);
            }
        }

        System.out.println("Safe map ('S' = safe to place, 'X' = not safe, 'Q' = queen):");
        for (int r = 0; r < SIZE; r++) {
            StringBuilder sb = new StringBuilder();
            for (int c = 0; c < SIZE; c++) {
                if (board[r][c]) {
                    sb.append('Q');
                } else {
                    sb.append(safe[r][c] ? 'S' : 'X');
                }
                sb.append(' ');
            }

            System.out.printf("%d: %s%n", r + 1, sb.toString().trim());
        }
        System.out.println("   1 2 3 4 5 6 7 8");
    }

    private void placeNewQueen() {
        try {
            while (true) {
                System.out.println("How many initial queens would you like to place? (0..8)");
                String line = scanner.nextLine().trim();
                int count = 0;
                try {
                    count = Integer.parseInt(line);
                } catch (NumberFormatException e) {
                    System.out.println("Please enter an integer between 0 and 8.");
                    continue;
                }
                if (count < 0 || count > 8) {
                    System.out.println("Please enter a number between 0 and 8.");
                    continue;
                }
                for (int i = 0; i < count; i++) {
                    while (true) {
                        System.out.printf("Enter queen #%d position as \"row col\" (1..8):%n", i + 1);
                        String pos = scanner.nextLine().trim();
                        String[] toks = pos.split("\\s+");
                        if (toks.length != 2) {
                            System.out.println("Invalid input. Please provide two integers separated by space.");
                            continue;
                        }
                        int r, c;
                        try {
                            r = Integer.parseInt(toks[0]);
                            c = Integer.parseInt(toks[1]);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid numbers. Use integers 1..8.");
                            continue;
                        }
                        if (r < 1 || r > 8 || c < 1 || c > 8) {
                            System.out.println("Coordinates must be between 1 and 8.");
                            continue;
                        }

                        int rr = r - 1, cc = c - 1;
                        // soft recursive
                        if (!this.isSafe(rr, cc)) {
                            System.out.println("Warning: placing a queen here conflicts with existing queens. Try another position.");
                            continue;
                        }

                        try {
                            this.placeQueen(rr, cc);
                        } catch (IllegalArgumentException ex) {
                            System.out.println("Error placing queen: " + ex.getMessage());
                            continue;
                        }
                        break;
                    }
                }
                break;
            }
        } catch (Exception e) {

        }
    }

    public void UserInterface() {
        try {
            System.out.println("Welcome to 8-Queens interactive checker.");
            System.out.println("Would you like to place some initial queens? (y/n)");
            String resp = scanner.nextLine().trim().toLowerCase();
            if (resp.equals("y") || resp.equals("yes")) {
                this.placeNewQueen();
            }

            System.out.println();
            this.printSafeMap();
            System.out.println();
            System.out.println("Now you can check positions. Enter commands:");
            System.out.println("  - \"r c\"  (e.g. 3 5) to check if placing queen at row r, col c is valid");
            System.out.println("  - \"all\"  to show safe positions map");
            System.out.println("  - \"placeNew\" to place new queen");
            System.out.println("  - \"exit\" to quit");

            while (true) {
                System.out.print("> ");
                String line = scanner.nextLine().trim();

                if (line.equalsIgnoreCase("exit")) {
                    System.out.println("Goodbye.");
                    break;
                } else if (line.equalsIgnoreCase("all")) {
                    this.printSafeMap();
                } else if (line.equalsIgnoreCase("placeNew")) {
                    this.placeNewQueen();
                } else if (line.equalsIgnoreCase("fillQueen")) {
                    this.tryFillQueen(0, 0);
                } else if (line.isEmpty()) {
                    // Do nothing
                } else {
                    String[] toks = line.split("\\s+");
                    if (toks.length != 2) {
                        System.out.println("Invalid command. Enter two integers 'row col', or 'all', or 'exit'.");
                        continue;
                    }
                    int r, c;
                    try {
                        r = Integer.parseInt(toks[0]);
                        c = Integer.parseInt(toks[1]);
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid numbers. Use integers 1..8.");
                        continue;
                    }
                    if (r < 1 || r > 8 || c < 1 || c > 8) {
                        System.out.println("Coordinates must be between 1 and 8.");
                        continue;
                    }
                    int rr = r - 1, cc = c - 1;
                    boolean safe = this.isSafe(rr, cc);
                    if (safe) {
                        System.out.printf("Position (%d,%d) is SAFE to place a queen.%n", r, c);
                    } else {
                        if (this.board[rr][cc]) {
                            System.out.printf("There is already a queen at (%d,%d).%n", r, c);
                        } else {
                            System.out.printf("Position (%d,%d) is NOT safe: a queen there would be attacked by an existing queen.%n", r, c);
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    // Real recursive is here
    private void tryFillQueen(int row, int col) {
        if (row < 0 || row > 7 || col < 0 || col > 7) {
            System.out.println("Coordinates must be between 0 and 7.");
            return;
        }

        System.out.println(String.format("Now try placing for row %d and col %d", row + 1, col + 1));
        if (this.isSafe(row, col)) {
            this.placeQueen(row, col);
        } else {
            if (col == 7) {
                this.tryFillQueen(row + 1, 0);
            } else {
                this.tryFillQueen(row, col + 1);
            }
        }
    }
}
