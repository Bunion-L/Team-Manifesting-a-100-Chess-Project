import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter; //abstract used for recieving mouse clicks
import java.awt.event.MouseEvent;
// Phase 1 classes
import chess.board.Board;
import chess.pieces.*;
import chess.position.Position;

public class ChessBoard extends JFrame {
    private JPanel boardPanel;
    private JPanel[][] squares = new JPanel[8][8];
    private Board gameLogic; // phase 1 logic

    // NEW: Variables to track the first click in GUI
    private int sourceRow = -1;
    private int sourceCol = -1;

    public ChessBoard() {
        // Initialize your Phase 1 board 
        gameLogic = new Board();

        setTitle("Chess Game - Phase 2");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //ensure program stops running
        setSize(600, 600);
        
        boardPanel = new JPanel(new GridLayout(8, 8));
        
        // Looping through the 8x8 grid
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col] = new JPanel();
                squares[row][col].setLayout(new BorderLayout()); // centers the image
                
                // Color the squares green and beige
                if ((row + col) % 2 == 0) {
                    squares[row][col].setBackground(new Color(235, 235, 208)); 
                } else {
                    squares[row][col].setBackground(new Color(119, 148, 85));  
                }

                // checks if piece is present
                Piece currentPiece = gameLogic.getGrid()[row][col];
                
                if (currentPiece != null) {
                    // Extract the color and the piece type to find the right image
                    String color = currentPiece.getColor().toString(); // e.g., "WHITE" or "BLACK"
                    String type = currentPiece.getClass().getSimpleName(); // e.g., "Pawn"
                    
                    // Builds the file path to png
                    String imagePath = "resources/" + color + "_" + type + ".png";
                    
                    // Create a label holding the image, and add it to the square
                    JLabel pieceIcon = new JLabel(new ImageIcon(imagePath));
                    squares[row][col].add(pieceIcon);

                }
                // NEW: Add a click listener to the square
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

        add(boardPanel);
        setVisible(true);
    }

    // NEW: Method to handle the two-click logic
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
            
            // 1. Update the Phase 1 backend (moves piece, records capture) 
            gameLogic.executeMove(from, to);

            // 2. Updates the GUI visually
            squares[row][col].removeAll(); // Clears any existing piece (handles captures)
            
            // Move the image from source to destination
            if (squares[sourceRow][sourceCol].getComponentCount() > 0) {
                Component pieceIcon = squares[sourceRow][sourceCol].getComponent(0);
                squares[row][col].add(pieceIcon);
            }
            
            squares[sourceRow][sourceCol].removeAll(); // Clear source visually
            squares[sourceRow][sourceCol].setBorder(null); // Remove highlight
            
            // Tell Swing to redraw the board to show the changes
            boardPanel.revalidate();
            boardPanel.repaint();
            
            // Reset for the next turn
            sourceRow = -1;
            sourceCol = -1;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ChessBoard());
    }
}
