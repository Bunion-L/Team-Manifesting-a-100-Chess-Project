import javax.swing.*;
import java.awt.*;
// Phase 1 classes
import chess.board.Board;
import chess.pieces.*;

public class ChessBoard extends JFrame
{
    private JPanel boardPanel;
    private JPanel[][] squares = new JPanel[8][8];
    //phase 1 logic
    private Board gameLogic; 

    public ChessBoard()
    {
        //Initializes Phase 1 board 
        gameLogic = new Board();

        setTitle("Chess Game - Phase 2");
        //Ensures program stops running
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        
        boardPanel = new JPanel(new GridLayout(8, 8));
        
        //Loopes through the 8x8 grid
        for(int row = 0; row < 8; row++)
        {
            for(int col = 0; col < 8; col++)
            {
                squares[row][col] = new JPanel();
                //Centers the image
                squares[row][col].setLayout(new BorderLayout());
                
                //Colors the squares green and beige
                if((row + col) % 2 == 0)
                {
                    squares[row][col].setBackground(new Color(235, 235, 208)); 
                }
                else
                {
                    squares[row][col].setBackground(new Color(119, 148, 85));  
                }

                //Checks if piece is present
                Piece currentPiece = gameLogic.getGrid()[row][col];
                
                if(currentPiece != null)
                {
                    //Extracts the color and the piece type to find the right image
                    //e.g., "WHITE" or "BLACK"
                    String color = currentPiece.getColor().toString();
                    //e.g., "Pawn"
                    String type = currentPiece.getClass().getSimpleName();
                    
                    //Builds the file path to png
                    String imagePath = "resources/" + color + "_" + type + ".png";
                    
                    //Creates a label holding the image, and add it to the square
                    JLabel pieceIcon = new JLabel(new ImageIcon(imagePath));
                    squares[row][col].add(pieceIcon);

                }
                
                boardPanel.add(squares[row][col]);
            }
        }

        add(boardPanel);
        setVisible(true);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new ChessBoard());
    }
}