package chess.game;

import chess.board.Board;
import chess.player.Player;
import chess.utils.Color;
import java.util.Scanner;

/**
 *Manages a single chess game between two Player instances
 *
 *Responsibilities:
 *Initialises the board and players with start()
 *Runs the main game loop with play()
 *Alternates turns,
 *Displays the board
 *Checks for check/checkmate/stalemate
 *Declares the result and cleans up with end(String)
 */
public class Game
{

    private Board board;
    private Player whitePlayer;
    private Player blackPlayer;

    private Color currentTurn;
    private int moveNumber;
    private final Scanner scanner;

    //New game
    public Game(Scanner scanner)
    {
        this.scanner = scanner;
    }

    //Initialises the game: creates the board, places pieces, creates players, sets the turn to white
    public void start()
    {
        board = new Board();
        whitePlayer = new Player(Color.WHITE);
        blackPlayer = new Player(Color.BLACK);
        currentTurn = Color.WHITE;
        moveNumber = 1;

        System.out.println();
        System.out.println("----------------------------------------");
        System.out.println("|        Chess Project Part 1          |");
        System.out.println("|______________________________________|");
        System.out.println("|  Enter moves like this: E2 E4        |");
        System.out.println("|  Type 'quit' or 'resign' to give up  |");
        System.out.println("----------------------------------------");
    }

    /**
     *Runs the main game loop until checkmate, stalemate, or a player resigns.
     *Alternates turns between white and black.
     */
    public void play()
    {
        while(true)
        {
            board.display();
            printStatus();

            Player active = (currentTurn == Color.WHITE) ? whitePlayer : blackPlayer;
            Player passive = (currentTurn == Color.WHITE) ? blackPlayer : whitePlayer;

            //Prompt for and execute a move
            boolean moved = active.makeMove(board, scanner);
            if(!moved)
            {
                //Player resigned
                end(passive.getName() + " wins by resignation!");
                return;
            }

            //After the move: check game-ending conditions for the opponent
            Color opponent = currentTurn.opposite();

            if(board.isCheckmate(opponent))
            {
                board.display();
                end(active.getName() + " wins by checkmate!");
                return;
            }

            if(board.isStalemate(opponent))
            {
                board.display();
                end("Draw by stalemate!");
                return;
            }

            //Says if opponent is in check
            if(board.isInCheck(opponent))
            {
                System.out.println("  *** " + passive.getName() + " is in check! ***");
            }

            // Advance turn
            if(currentTurn == Color.BLACK)
            {
                moveNumber++;
            }
            currentTurn = opponent;
        }
    }

    //Prints a game-over message and offers to play again.
    public void end(String result)
    {
        System.out.println();
        System.out.println("  *** GAME OVER ***");
        System.out.println("  " + result);
        System.out.println();
    }

    
    //Prints the current turn indicator and check warning
    private void printStatus()
    {
        String turnLabel = (currentTurn == Color.WHITE) ? "White" : "Black";
        System.out.println("  Move " + moveNumber + " — " + turnLabel + " to play.");
        if(board.isInCheck(currentTurn))
        {
            System.out.println("  *** You are in CHECK! You must escape check. ***");
        }
    }

    //Returns this game board
    public Board getBoard()
    {
        return board;
    }

    //Returns the color whose turn it currently is
    public Color getCurrentTurn()
    {
        return currentTurn;
    }
}
