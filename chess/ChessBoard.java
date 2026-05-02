package chess;

import chess.board.Board;
import chess.pieces.Piece;
import chess.position.Position; //abstract used for recieving mouse clicks
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Stack;
import javax.swing.*;

/**
 * The main Graphical User Interface (GUI) class for the Chess Game.
 * The class extends JFrame and handles the display of the chessboard,
 * piece movement interactions, game state saving/loading, and move history
 */
public class ChessBoard extends JFrame {
    private JPanel boardPanel;
    private JPanel[][] squares = new JPanel[8][8];
    private Board gameLogic;// phase 1 logic

    // Variables to track the first click in GUI
    private int sourceRow = -1;
    private int sourceCol = -1;

    //Added for phase 3
    private chess.utils.Color currentTurn = chess.utils.Color.WHITE;
    private int moveNumber = 1;

    // Feature 3 Components 
    private JTextArea moveHistoryArea;
    private JPanel historyPanel;
    private Stack<MoveRecord> moveStack = new Stack<>(); // The stack to hold moves
    private JPanel capturedWhitePanel;
    private JPanel capturedBlackPanel;

    /**
     * Constructs the ChessBoard GUI.
     * Initializes the underlying game logic, sets up the main application window size and layout,
     * and assembles the menu bar, board panel, and move history panel.
     */
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
    /**
     * Initializes and configures the top menu bar (Feature 1).
     * Includes controls to start a new game, save the current game state,
     * and load a previously saved game via serialization
     */
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game Controls");

        JMenuItem newGameItem = new JMenuItem("New Game");
        JMenuItem saveGameItem = new JMenuItem("Save Game");
        JMenuItem loadGameItem = new JMenuItem("Load Game");

        //"New Game" Logic: Resets the board and clear history
        //UPDATE FOR PART 3, Now calls resetGame()
        newGameItem.addActionListener(e -> resetGame());

        // Logic for saving current board
        saveGameItem.addActionListener(e -> {
            try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("chess_save.dat"))) {
                // Write the entire Board object (and all its pieces) to a file
                out.writeObject(gameLogic);
                JOptionPane.showMessageDialog(this, "Game Saved Successfully!", "Save Game", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving game: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Logic for loading save file
        loadGameItem.addActionListener(e -> {
            try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("chess_save.dat"))) {
                // Read the file and cast it back into a Board object
                gameLogic = (Board) in.readObject();
                
                // Clear the UI history and stacks so old moves don't conflict
                moveStack.clear();
                moveHistoryArea.setText("--- Game Loaded ---\n");
                
                // Redraw the visual board with the newly loaded pieces!
                refreshBoardGUI();
                
                JOptionPane.showMessageDialog(this, "Game Loaded Successfully!", "Load Game", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "No save file found, or error loading game.", "Error", JOptionPane.WARNING_MESSAGE);
            }
        });
        gameMenu.add(newGameItem);
        gameMenu.add(saveGameItem);
        gameMenu.add(loadGameItem);
        menuBar.add(gameMenu);

        setJMenuBar(menuBar); // Attaches menu to the window
    }

    // Feature 3: History Panel
    /**
     * Initializes and configures the Game History panel (Feature 3).
     * Sets up a text area to track moves and captures, as well as an Undo button
     * to revert the game state to the previous move
     */
    private void setupHistoryPanel() {
        historyPanel = new JPanel();
        historyPanel.setLayout(new BorderLayout());
        historyPanel.setPreferredSize(new Dimension(250, 600)); // Made slightly wider for icons
        historyPanel.setBorder(BorderFactory.createTitledBorder("Game Info"));

        // --- NEW: Captured Pieces Visual Area ---
        capturedWhitePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        capturedWhitePanel.setBorder(BorderFactory.createTitledBorder("Captured White Pieces"));
        
        capturedBlackPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        capturedBlackPanel.setBorder(BorderFactory.createTitledBorder("Captured Black Pieces"));

        JPanel capturedContainer = new JPanel(new GridLayout(2, 1));
        capturedContainer.add(capturedWhitePanel);
        capturedContainer.add(capturedBlackPanel);
        // ----------------------------------------

        // Text area for move history
        moveHistoryArea = new JTextArea();
        moveHistoryArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(moveHistoryArea);
        
        // Undo Button
        JButton undoButton = new JButton("Undo Last Move");
        undoButton.addActionListener(e -> {
            // Check if there is a move to undo
            if (!moveStack.isEmpty()) {
                MoveRecord lastMove = moveStack.pop();
                Piece[][] grid = gameLogic.getGrid();
                
                grid[lastMove.from.getRow()][lastMove.from.getCol()] = lastMove.movedPiece;
                lastMove.movedPiece.setPosition(lastMove.from); 
                
                grid[lastMove.to.getRow()][lastMove.to.getCol()] = lastMove.capturedPiece;
                if (lastMove.capturedPiece != null) {
                    gameLogic.getCapturedPieces().remove(lastMove.capturedPiece);
                }

                try {
                    int end = moveHistoryArea.getDocument().getLength();
                    int start = moveHistoryArea.getLineStartOffset(moveHistoryArea.getLineCount() - 2); 
                    moveHistoryArea.getDocument().remove(start, end - start);
                } catch (Exception ex) {
                    ex.printStackTrace(); 
                }

                refreshBoardGUI();
                //Added for Part 3, Now reverses turn as well 
                if (currentTurn == chess.utils.Color.BLACK) moveNumber--;
                currentTurn = currentTurn.opposite();
            } else {
                JOptionPane.showMessageDialog(this, "No moves to undo!", "Undo", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Add everything to the history panel
        historyPanel.add(capturedContainer, BorderLayout.NORTH); // Added to the top!
        historyPanel.add(scrollPane, BorderLayout.CENTER);
        historyPanel.add(undoButton, BorderLayout.SOUTH);
    }

    /**
     * Initializes the central 8x8 chessboard panel.
     * Sets up the alternating light and dark squares and attaches mouse click
     * listeners to each square to handle piece selection and movement
     */
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

    /**
     * Clears all GUI visual elements from the board and completely redraws them
     * based on the current state of the backend game logic.
     */
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

        updateCapturedPieces();
    }

    /**
     * Reads the captured pieces list from the Phase 1 backend and updates 
     * the GUI panels to visually display the icons of captured pieces.
     */
    private void updateCapturedPieces() {

        //failsafe
        if (capturedWhitePanel == null || capturedBlackPanel == null) {
            return; // Exit early if the panels haven't been built yet!
        }
        
        // Clear the panels first
        capturedWhitePanel.removeAll();
        capturedBlackPanel.removeAll();

        // Loop through all captured pieces
        for (Piece p : gameLogic.getCapturedPieces()) {
            String color = p.getColor().toString(); 
            String type = p.getClass().getSimpleName(); 
            String imagePath = "resources/" + color + "_" + type + ".png";
            
            // Create the icon, but scale it down so it fits nicely in the side panel!
            ImageIcon originalIcon = new ImageIcon(imagePath);
            Image scaledImage = originalIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            JLabel pieceIcon = new JLabel(new ImageIcon(scaledImage));

            // Add to the correct panel based on piece color
            if (color.equals("WHITE")) {
                capturedWhitePanel.add(pieceIcon);
            } else {
                capturedBlackPanel.add(pieceIcon);
            }
        }

        // Tell the UI to redraw the panels
        capturedWhitePanel.revalidate();
        capturedWhitePanel.repaint();
        capturedBlackPanel.revalidate();
        capturedBlackPanel.repaint();
    }

    /**
     * UPDATED FOR PART 3, Now tracks turns and enforces legal moves
     * Handles the logic when a square on the chessboard is clicked.
     * Implements a two-click system: the first click selects a valid piece,
     * and the second click executes the move. Also checks for an endgame
     * condition if a King is captured.
     * @param row The row index of the clicked square (0-7).
     * @param col The column index of the clicked square (0-7).
     */
    private void handleSquareClick(int row, int col)
    {
        if(sourceRow == -1 && sourceCol == -1)
        {
            //FIRST CLICK: Select a piece belonging to the current player
            Piece clicked = gameLogic.getGrid()[row][col];
            if(clicked != null && clicked.getColor() == currentTurn)
            {
                sourceRow = row;
                sourceCol = col;
                squares[row][col].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 4));

                //Highlight legal moves in blue
                Position from = new Position(row, col);
                for(Position legal : gameLogic.getLegalMoves(clicked))
                {
                    squares[legal.getRow()][legal.getCol()].setBorder(BorderFactory.createLineBorder(Color.BLUE, 3));
                }
            }
        }
        else
        {
            //SECOND CLICK: Attempt the move
            Position from = new Position(sourceRow, sourceCol);
            Position to   = new Position(row, col);

            Piece movingPiece  = gameLogic.getGrid()[sourceRow][sourceCol];
            Piece targetPiece  = gameLogic.getGrid()[row][col];

            //Use Board.movePiece — enforces ownership + legality + check filtering
            boolean moved = gameLogic.movePiece(from, to, currentTurn);

            if(moved)
            {
                //Record for undo
                moveStack.push(new MoveRecord(from, to, movingPiece, targetPiece));

                //History text
                String side = (currentTurn == chess.utils.Color.WHITE) ? "White" : "Black";
                String moveText = moveNumber + ". " + side + " "
                    + movingPiece.getClass().getSimpleName()
                    + " " + getAlgebraic(from.getRow(), from.getCol())
                    + " → " + getAlgebraic(to.getRow(), to.getCol());
                if(targetPiece != null)
                {
                    moveText += " x" + targetPiece.getClass().getSimpleName();
                }

                //Switch turn
                chess.utils.Color opponent = currentTurn.opposite();

                //Check/Checkmate/Stalemate detection
                if(gameLogic.isCheckmate(opponent))
                {
                    moveText += " #";
                    moveHistoryArea.append(moveText + "\n");
                    refreshBoardGUI();
                    JOptionPane.showMessageDialog(this,
                        side + " wins by checkmate!",
                        "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    resetGame();
                    return;
                }
                else if(gameLogic.isStalemate(opponent))
                {
                    moveHistoryArea.append(moveText + "\n");
                    refreshBoardGUI();
                    JOptionPane.showMessageDialog(this,
                        "Draw by stalemate!",
                        "Game Over", JOptionPane.INFORMATION_MESSAGE);
                    resetGame();
                    return;
                }
                else if(gameLogic.isInCheck(opponent))
                {
                    moveText += " +";
                    String oppName = (opponent == chess.utils.Color.WHITE) ? "White" : "Black";
                    JOptionPane.showMessageDialog(this,
                        oppName + " is in check!",
                        "Check", JOptionPane.WARNING_MESSAGE);
                }

                moveHistoryArea.append(moveText + "\n");

                // Advance turn and move number
                if(currentTurn == chess.utils.Color.BLACK) moveNumber++;
                currentTurn = opponent;

                refreshBoardGUI();
            }

            //Whether the move succeeded or failed, clear selection
            sourceRow = -1;
            sourceCol = -1;
            refreshBoardGUI(); //clears blue highlights too
        }
    }

    /**
     * Converts a 0-indexed row and column coordinate into standard
     * algebraic chess notation (e.g., column 0, row 4 is "a4").
     * @param row The row index of the square.
     * @param col The column index of the square.
     * @return A String representing the algebraic notation of the square.
     */
    private String getAlgebraic(int row, int col) {
        char file = (char) ('a' + col);
        int rank = 8 - row;
        return "" + file + rank;
    }

    /**
     * A helper class used to record the state of the board before a move is executed.
     * This snapshot data is required to allow players to successfully undo a move
     */
    private class MoveRecord {
        Position from;
        Position to;
        Piece movedPiece;
        Piece capturedPiece;

        /**
         * Constructs a new MoveRecord snapshot
         * @param from          The starting position of the moved piece.
         * @param to            The destination position of the moved piece.
         * @param movedPiece    The actual Piece object that was moved.
         * @param capturedPiece The Piece object that was captured during the move (can be null).
         */
        public MoveRecord(Position from, Position to, Piece movedPiece, Piece capturedPiece) {
            this.from = from;
            this.to = to;
            this.movedPiece = movedPiece;
            this.capturedPiece = capturedPiece;
        }
    }

    //Called after checkmate or stalemate so a new game doesn't carry over duplicate logic
    private void resetGame()
    {
        gameLogic = new Board();
        currentTurn = chess.utils.Color.WHITE;
        moveNumber = 1;
        moveStack.clear();
        moveHistoryArea.setText("");
        sourceRow = -1;
        sourceCol = -1;
        refreshBoardGUI();
    }

    /**
     * Main entry point for the Chess Game application.
     * Initializes and displays the GUI asynchronously using SwingUtilities.
     * @param args Command-line arguments (not utilized).
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessBoard());
    }
}
