package knighttour;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;

/**
 * GUI Class for the Knight's Tour.
 * (English Version)
 * Compatible with JDK 8.
 */
public class KnightTourGUI extends JFrame {

    private static final int N = 8;
    // 2D Array of JButtons (chess squares)
    private final JButton[][] boardSquares = new JButton[N][N];
    // The solver logic object
    private final KnightTour solver;
    
    private final JPanel boardPanel;
    private final JLabel statusLabel;
    private final JButton startButton;
    private final JButton resetButton;
    
    // UI Colors
    private final Color lightSquare = new Color(240, 217, 181); // Light square
    private final Color darkSquare = new Color(181, 136, 99); // Dark square
    private final Color highlightSquare = new Color(130, 151, 105); // Selected square
    
    private int startRow = -1;
    private int startCol = -1;

    public KnightTourGUI() {
        this.solver = new KnightTour();

        setTitle("Knight's Tour Solver");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- 1. Control Panel (SOUTH) ---
        JPanel controlPanel = new JPanel();
        statusLabel = new JLabel("Please select a square to start."); // English
        statusLabel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        startButton = new JButton("Start"); // English
        startButton.setEnabled(false); // Only enabled when a square is selected
        
        resetButton = new JButton("Reset"); // English

        controlPanel.add(statusLabel);
        controlPanel.add(startButton);
        controlPanel.add(resetButton);

        // --- 2. Board Panel (CENTER) ---
        boardPanel = new JPanel(new GridLayout(N, N));
        boardPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        
        Font buttonFont = new Font("Arial", Font.BOLD, 18);

        // Create 64 board squares
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                JButton button = new JButton();
                button.setFont(buttonFont);
                button.setPreferredSize(new Dimension(70, 70));
                button.setOpaque(true);
                button.setBorderPainted(false); 

                // Set board colors
                if ((r + c) % 2 == 0) {
                    button.setBackground(lightSquare);
                } else {
                    button.setBackground(darkSquare);
                }
                
                // Store r, c for the ActionListener
                final int finalRow = r;
                final int finalCol = c;
                
                // Add action for clicking a square
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        onSquareClick(finalRow, finalCol);
                    }
                });

                boardSquares[r][c] = button;
                boardPanel.add(button);
            }
        }

        // --- Add panels to Frame ---
        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // --- Add actions for control buttons ---
        
        // Start Button: Run the solver
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startSolver();
            }
        });

        // Reset Button: Clear the board
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetBoard();
            }
        });

        pack(); // Auto-size the frame
        setLocationRelativeTo(null); // Center the frame
        setResizable(false); // Disable resizing
    }

    /**
     * Handles the user clicking on a board square.
     */
    private void onSquareClick(int r, int c) {
        // If a start square was already selected, reset its color
        if (startRow != -1) {
            resetButtonColors();
        }

        startRow = r;
        startCol = c;

        // Highlight the new selected square and set its text to "1"
        boardSquares[r][c].setBackground(highlightSquare);
        boardSquares[r][c].setText("1");
        
        startButton.setEnabled(true);
        statusLabel.setText("Start square selected. Press 'Start' to run."); // English
    }

    /**
     * Resets the button colors to the default chessboard pattern.
     */
    private void resetButtonColors() {
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if ((r + c) % 2 == 0) {
                    boardSquares[r][c].setBackground(lightSquare);
                } else {
                    boardSquares[r][c].setBackground(darkSquare);
                }
            }
        }
    }

    /**
     * Resets the board and the solver logic.
     */
    private void resetBoard() {
        solver.clearBoard(); // Clear logic
        startRow = -1;
        startCol = -1;
        
        // Clear GUI
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                boardSquares[r][c].setText("");
                boardSquares[r][c].setEnabled(true); // Re-enable button
            }
        }
        
        resetButtonColors();
        startButton.setEnabled(false);
        resetButton.setEnabled(true);
        statusLabel.setText("Please select a square to start."); // English
    }

    /**
    * Disables all board squares and buttons while solving.
     */
    private void setBoardEnabled(boolean enabled) {
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                boardSquares[r][c].setEnabled(enabled);
            }
        }
        startButton.setEnabled(enabled);
        resetButton.setEnabled(enabled);
    }

    /**
     * Starts the solver on a background thread (SwingWorker).
     */
    private void startSolver() {
        setBoardEnabled(false); // Disable GUI
        statusLabel.setText("Solving..."); // English

        // SwingWorker runs the heavy task (backtracking) on a background thread
        // so the GUI doesn't freeze.
        SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
            
            /**
             * Runs on the background thread.
             */
            @Override
            protected Boolean doInBackground() throws Exception {
                // Call the solver logic
                return solver.solveFrom(startRow, startCol);
            }

            /**
             * Runs on the GUI thread (EDT) after doInBackground is finished.
             */
            @Override
            protected void done() {
                try {
                    boolean success = get(); // Get result from doInBackground
                    if (success) {
                        statusLabel.setText("Solution found!"); // English
                        updateBoardFromSolver();
                    } else {
                        statusLabel.setText("No solution found from this start."); // English
                        // Clear the "1"
                        boardSquares[startRow][startCol].setText("");
                    }
                } catch (Exception e) {
                    statusLabel.setText("Error: " + e.getMessage()); // English
                    e.printStackTrace();
                }
                // Only re-enable the reset button
                resetButton.setEnabled(true);
            }
        };
        
        worker.execute(); // Start the SwingWorker
    }

    /**
     * Updates the numbers on the buttons from the solver's board.
     */
    private void updateBoardFromSolver() {
        int[][] solutionBoard = solver.getBoard();
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                if (solutionBoard[r][c] != -1) {
                    boardSquares[r][c].setText(String.valueOf(solutionBoard[r][c]));
                }
            }
        }
    }
}