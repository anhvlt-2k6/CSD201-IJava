/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkg8queenpuzzlege2;

/**
 *
 * @author ADMIN
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EightQueensChecker2 {

    // --- Constants ---
    private final int SIZE = 8;
    private final String QUEEN_CHAR = "\u265B"; // Unicode Black Queen ♛

    // --- Colors ---
    private final Color COLOR_LIGHT_SQUARE = new Color(240, 217, 181);
    private final Color COLOR_DARK_SQUARE = new Color(181, 136, 99);
    private final Color COLOR_ATTACKED = new Color(255, 120, 120); // Red for attacked squares
    private final Color COLOR_QUEEN = Color.BLACK;

    // --- Swing Components ---
    private JFrame frame;
    private JButton[][] boardButtons;
    private JTextArea infoArea;
    private JLabel statusLabel;

    // --- Logic ---
    private boolean[][] board; // true = queen present, false = empty

    public EightQueensChecker2() {
        this.board = new boolean[SIZE][SIZE];
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("8 Queens Puzzle Solver (Gemini Version)"); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setMinimumSize(new Dimension(800, 600)); 

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel("THE 8 QUEENS PROBLEM", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel boardPanel = new JPanel(new GridLayout(SIZE, SIZE));
        boardPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        boardButtons = new JButton[SIZE][SIZE];

        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(64, 64)); 
                button.setFont(new Font("Serif", Font.BOLD, 36)); 
                button.setFocusPainted(false);

                button.setBackground((row + col) % 2 == 0 ? COLOR_LIGHT_SQUARE : COLOR_DARK_SQUARE);

                final int r = row;
                final int c = col;

                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleCellClick(r, c);
                    }
                });

                boardButtons[row][col] = button;
                boardPanel.add(button);
            }
        }
        mainPanel.add(boardPanel, BorderLayout.CENTER);

        JPanel eastPanel = new JPanel(new BorderLayout(10, 10));
        eastPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        infoArea = new JTextArea(10, 25);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        infoArea.setText("WELCOME TO THE 8 QUEENS PUZZLE!\n\n"
                + "INSTRUCTIONS:\n"
                + "• Click a square to place a queen.\n"
                + "• Click a queen to remove it.\n"
                + "• RED squares are attacked.\n"
                + "• Place 8 queens so none attack\n"
                + "  each other.\n\n"
                + "Good luck!\n");

        JScrollPane scrollPane = new JScrollPane(infoArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        JPanel infoWrapper = new JPanel(new BorderLayout());
        infoWrapper.setBorder(BorderFactory.createTitledBorder("Information"));
        infoWrapper.add(scrollPane, BorderLayout.CENTER);
        eastPanel.add(infoWrapper, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel(new GridLayout(0, 1, 5, 5)); 
        controlPanel.setBorder(BorderFactory.createTitledBorder("Controls"));

        JButton clearBtn = createStyledButton("Clear Board", new Color(220, 50, 50));
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearBoard();
            }
        });

        JButton solveBtn = createStyledButton("Auto-Solve", new Color(50, 150, 50));
        solveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autoSolve();
            }
        });

        JButton aboutBtn = createStyledButton("About", new Color(70, 130, 180));
        aboutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAbout();
            }
        });

        controlPanel.add(clearBtn);
        controlPanel.add(solveBtn);
        controlPanel.add(aboutBtn);
        eastPanel.add(controlPanel, BorderLayout.SOUTH);

        mainPanel.add(eastPanel, BorderLayout.EAST);

        statusLabel = new JLabel("Ready - Click a square to begin.");
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLoweredBevelBorder(),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.add(statusLabel, BorderLayout.SOUTH);

        frame.pack();
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);

        updateBoardDisplay();
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker()),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        return button;
    }

    private void handleCellClick(int row, int col) {
        int userRow = row + 1;
        int userCol = col + 1;

        try {
            if (board[row][col]) {
                board[row][col] = false;
                infoArea.append("\n✓ Queen removed from (" + userRow + ", " + userCol + ")");
                statusLabel.setText("Queen removed from (" + userRow + ", " + userCol + ")");
            
            } else {
                if (isSafe(row, col)) {
                    board[row][col] = true;
                    infoArea.append("\n✓ Queen placed at (" + userRow + ", " + userCol + ")");
                    statusLabel.setText("Queen placed at (" + userRow + ", " + userCol + ")");
                } else {
                    infoArea.append(String.format("\n✗ CANNOT place at (%d, %d)!\n  Position is attacked.", userRow, userCol));
                    statusLabel.setText("Position (" + userRow + ", " + userCol + ") is not safe!");
                }
            }

            updateBoardDisplay();
            checkGameStatus();

        } catch (Exception ex) {
            infoArea.append("\nERROR: " + ex.getMessage() + "\n");
        }
    }

    private void updateBoardDisplay() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                JButton button = boardButtons[r][c];
                
                if (board[r][c]) {
                    button.setText(QUEEN_CHAR);
                    button.setForeground(COLOR_QUEEN);
                    button.setBackground((r + c) % 2 == 0 ? COLOR_LIGHT_SQUARE : COLOR_DARK_SQUARE);
                } else {
                    button.setText("");
                    if (!isSafe(r, c)) {
                        button.setBackground(COLOR_ATTACKED);
                    } else {
                        button.setBackground((r + c) % 2 == 0 ? COLOR_LIGHT_SQUARE : COLOR_DARK_SQUARE);
                    }
                }
            }
        }
    }

    private void clearBoard() {
        this.board = new boolean[SIZE][SIZE];
        updateBoardDisplay();
        infoArea.setText("--- Board Cleared ---\n"); 
        statusLabel.setText("Board cleared. Ready to start.");
    }

    private void autoSolve() {
        clearBoard();
        infoArea.append("\nSolving...\n");
        
        if (solve(0)) {
            infoArea.append("\n✓ Auto-solve successful!\n");
            statusLabel.setText("Auto-solve successful!");
        } else {
            infoArea.append("\n✗ No solution found.\n");
            statusLabel.setText("Auto-solve failed.");
        }
        
        updateBoardDisplay();
        checkGameStatus();
    }

    private boolean solve(int col) {
        if (col >= SIZE) {
            return true;
        }

        for (int row = 0; row < SIZE; row++) {
            if (isSafe(row, col)) {
                board[row][col] = true;
                
                if (solve(col + 1)) {
                    return true;
                }

                board[row][col] = false; 
            }
        }
        
        return false;
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(frame,
            "8 QUEENS PUZZLE SOLVER\n\n" +
            "Description: Place 8 queens on an 8x8 chessboard\n" +
            "so that no two queens threaten each other.\n\n" +
            "A queen attacks any square on the same:\n" +
            "- Row\n" +
            "- Column\n" +
            "- Diagonal\n\n" +
            "Developed for a University Project.",
            "About this Project",
            JOptionPane.INFORMATION_MESSAGE);
    }

    private void checkGameStatus() {
        int queenCount = 0;
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                if (board[r][c]) {
                    queenCount++;
                }
            }
        }

        if (queenCount == 8) {
            boolean allSafe = true;
            for (int r = 0; r < SIZE; r++) {
                for (int c = 0; c < SIZE; c++) {
                    if (board[r][c]) {
                        board[r][c] = false;
                        if (!isSafe(r, c)) {
                            allSafe = false;
                        }
                        board[r][c] = true; 
                    }
                }
            }

            if (allSafe) {
                infoArea.append("\n🎉 CONGRATULATIONS! 🎉\n You solved the 8 Queens Puzzle!\n");
                statusLabel.setText("🎉 SUCCESS! Puzzle Solved!");
                
                JOptionPane.showMessageDialog(frame,
                    "🎉 CONGRATULATIONS! 🎉\n\n" +
                    "You have successfully placed 8 queens\n" +
                    "without any attacking each other.\n\n" +
                    "Excellent work!",
                    "Puzzle Solved!",
                    JOptionPane.INFORMATION_MESSAGE);
            } else {
                 infoArea.append("\nAlmost! 8 queens are placed,\nbut some are still attacking each other.");
                 statusLabel.setText("8 queens placed, but the solution is incorrect.");
            }
        }
    }

    // --- Core Logic Methods ---

    private boolean isSafe(int row, int col) {
        // Check same row (horizontal)
        for (int c = 0; c < SIZE; c++) {
            if (board[row][c]) {
                return false;
            }
        }

        // Check same column (vertical)
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
     * Main method (for testing)
     */
    public static void main(String[] args) {
         try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Handle exception
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new EightQueensChecker2();
            }
        });
    }
}