import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter; //abstract used for recieving mouse clicks
import java.awt.event.MouseEvent;
import java.util.Stack;
// Phase 1 classes
import chess.board.Board;
import chess.pieces.Piece;
import chess.position.Position;

public class ChessBoard extends JFrame {
    private JPanel boardPanel;
    private JPanel[][] squares = new JPanel[8][8];
    private Board gameLogic;// phase 1 logic

    // Variables to track the first click in GUI
    private int sourceRow = -1;
    private int sourceCol = -1;

    // Feature 3 Components 
    private JTextArea moveHistoryArea;
    private JPanel historyPanel;
    private Stack<MoveRecord> moveStack = new Stack<>(); // The stack to hold moves

    public ChessBoard() {
        // Initialize Phase 1 board 
        gameLogic = new Board();
        setTitle("Chess Game - Phase 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //ensure program stops running
        setSize(800, 600); // Made the window wider to fit the history panel!
        setLayout(new BorderLayout()); // Use BorderLayout for the main frame

        // 1. Initialize Menu Bar (Feature 1)
        setupMenuBar();

        // 2. Initialize Board Panel (Center)
        setupBoardPanel();
        add(boardPanel, BorderLayout.CENTER);

        // 3. Initialize History Panel (Feature 3 - Right Side)
        setupHistoryPanel();
        add(historyPanel, BorderLayout.EAST);

        setVisible(true);
    }

    // Feature 1: Menu
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game Controls");

        JMenuItem newGameItem = new JMenuItem("New Game");
        JMenuItem saveGameItem = new JMenuItem("Save Game");
        JMenuItem loadGameItem = new JMenuItem("Load Game");

        // "New Game" Logic: Resets the board and clear history
        newGameItem.addActionListener(e -> {
            gameLogic = new Board(); // Phase 1 board creates a fresh game!
            moveHistoryArea.setText(""); // Clear history
            refreshBoardGUI(); // Updates the visuals
        });

        // Placeholders for Save/Load (add later)
        saveGameItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Save Game clicked!"));
        loadGameItem.addActionListener(e -> JOptionPane.showMessageDialog(this, "Load Game clicked!"));

        gameMenu.add(newGameItem);
        gameMenu.add(saveGameItem);
        gameMenu.add(loadGameItem);
        menuBar.add(gameMenu);

        setJMenuBar(menuBar); // Attaches menu to the window
    }

    // Feature 3: History Panel
    private void setupHistoryPanel() {
        historyPanel = new JPanel();
        historyPanel.setLayout(new BorderLayout());
        historyPanel.setPreferredSize(new Dimension(200, 600));
        historyPanel.setBorder(BorderFactory.createTitledBorder("Game Info"));

        // Text area for move history
        moveHistoryArea = new JTextArea();
        moveHistoryArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(moveHistoryArea);
        
        // Undo Button
        JButton undoButton = new JButton("Undo Last Move");
        undoButton.addActionListener(e -> {
            // Check if there is a move to undo
            if (!moveStack.isEmpty()) {
                // Pop the last move off the stack
                MoveRecord lastMove = moveStack.pop();
                
                // Restore the backend game logic (Board.java)
                Piece[][] grid = gameLogic.getGrid();
                
                // Move the piece back to its original spot
                grid[lastMove.from.getRow()][lastMove.from.getCol()] = lastMove.movedPiece;
                lastMove.movedPiece.setPosition(lastMove.from); // Update internal piece position
                
                // Put the captured piece back (or make the square null if nothing was captured)
                grid[lastMove.to.getRow()][lastMove.to.getCol()] = lastMove.capturedPiece;
                if (lastMove.capturedPiece != null) {
                    // Remove it from the captured list in Phase 1's backend
                    gameLogic.getCapturedPieces().remove(lastMove.capturedPiece);
                }

                // Removes the last line from the History Text Area
                try {
                    int end = moveHistoryArea.getDocument().getLength();
                    // Get the start of the previous line (-2 accounts for the newline character)
                    int start = moveHistoryArea.getLineStartOffset(moveHistoryArea.getLineCount() - 2); 
                    moveHistoryArea.getDocument().remove(start, end - start);
                } catch (Exception ex) {
                    ex.printStackTrace(); // Failsafe in case of text area errors
                }

                // Visually refresh the board to show the undone state
                refreshBoardGUI();
                
            } else {
                JOptionPane.showMessageDialog(this, "No moves to undo!", "Undo", JOptionPane.WARNING_MESSAGE);
            }
        });

        historyPanel.add(scrollPane, BorderLayout.CENTER);
        historyPanel.add(undoButton, BorderLayout.SOUTH);
    }

    // New Board setup
    private void setupBoardPanel() {

        boardPanel = new JPanel(new GridLayout(8, 8));

        // Looping through 8x8 grid
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = new JPanel(new BorderLayout()); //centers image
                
                // color green and beige
                if ((row + col) % 2 == 0) {
                    squares[row][col].setBackground(new Color(235, 235, 208)); 
                } else {
                    squares[row][col].setBackground(new Color(119, 148, 85));  
                }

                //click listener for square
                final int r = row;
                final int c = col;
                squares[row][col].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleSquareClick(r, c);
                    }
                });

                boardPanel.add(squares[row][col]);
            }
        }
        refreshBoardGUI(); // Draw pieces initially
    }

    // Clears all icons and redraws them based on gameLogic's current state
    private void refreshBoardGUI() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].removeAll(); // Clear existing icon
                Piece currentPiece = gameLogic.getGrid()[row][col];
                
                if (currentPiece != null) {
                    String color = currentPiece.getColor().toString(); 
                    String type = currentPiece.getClass().getSimpleName(); 
                    String imagePath = "resources/" + color + "_" + type + ".png";
                    
                    JLabel pieceIcon = new JLabel(new ImageIcon(imagePath));
                    squares[row][col].add(pieceIcon);
                }
                squares[row][col].setBorder(null); // Remove any highlights
            }
        }
        boardPanel.revalidate();
        boardPanel.repaint();
    }

    // Method to handle two-click logic
    private void handleSquareClick(int row, int col) {
        if (sourceRow == -1 && sourceCol == -1) {
            // FIRST CLICK: Selects a piece
            if (gameLogic.getGrid()[row][col] != null) {
                sourceRow = row;
                sourceCol = col;
                // Highlights the selected square in yellow
                squares[row][col].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));
            }
        } else {
            // SECOND CLICK: Moves the piece
            Position from = new Position(sourceRow, sourceCol);
            Position to = new Position(row, col);
            
            // Look at the destination square before we move
            Piece movingPiece = gameLogic.getGrid()[sourceRow][sourceCol];
            Piece targetPiece = gameLogic.getGrid()[row][col];
            boolean isGameOver = false;
            String winner = "";

            if (targetPiece != null && targetPiece.getClass().getSimpleName().equals("King")) {
                isGameOver = true;
                winner = movingPiece.getColor().toString(); 
            }

            // Save the exact state of the move before we execute it
            moveStack.push(new MoveRecord(from, to, movingPiece, targetPiece));
            
            // Execute Move
            gameLogic.executeMove(from, to);

            // feature 3: add move to history
            String moveText = movingPiece.getColor() + " " + movingPiece.getClass().getSimpleName() + 
                              " moved from " + getAlgebraic(sourceRow, sourceCol) + 
                              " to " + getAlgebraic(row, col);
            
            if (targetPiece != null) {
                moveText += " (Captured " + targetPiece.getClass().getSimpleName() + ")";
            }
            moveHistoryArea.append(moveText + "\n");

            refreshBoardGUI(); // Update visuals

            if (isGameOver) {
                JOptionPane.showMessageDialog(this, 
                    "Game Over! " + winner + " captured the King and wins!", 
                    "Endgame Notification", 
                    JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }

            sourceRow = -1;
            sourceCol = -1;
        }
    }

    // Helper method for Feature 3 to convert (row, col) to e.g., "e4"
    private String getAlgebraic(int row, int col) {
        char file = (char) ('a' + col);
        int rank = 8 - row;
        return "" + file + rank;
    }

    // undo class
    private class MoveRecord {
        Position from;
        Position to;
        Piece movedPiece;
        Piece capturedPiece;

        public MoveRecord(Position from, Position to, Piece movedPiece, Piece capturedPiece) {
            this.from = from;
            this.to = to;
            this.movedPiece = movedPiece;
            this.capturedPiece = capturedPiece;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessBoard());
    }
}

