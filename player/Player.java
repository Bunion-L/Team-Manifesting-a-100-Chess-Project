package chess.player;

import chess.board.Board;
import chess.pieces.Piece;
import chess.position.Position;
import chess.utils.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//Represents the chess player
//Input format: "E2 E4"
public class Player
{

    //The color this player controls
    private final Color color;

    //Display name from color
    private final String name;

    //Constructs a player with the given color
    public Player(Color color)
    {
        this.color = color;
        this.name = (color == Color.WHITE) ? "White" : "Black";
    }

    //Returns the color of this player
    public Color getColor()
    {
        return color;
    }

    //Returns the display name of this player
    public String getName()
    {
        return name;
    }

    //Returns all pieces currently on the board that belong to this player
    public List<Piece> getActivePieces(Board board)
    {
        List<Piece> pieces = new ArrayList<>();
        for(Piece[] row : board.getGrid())
        {
            for(Piece p : row)
            {
                if(p != null && p.getColor() == color)
                {
                    pieces.add(p);
                }
            }
        }
        return pieces;
    }

    //Prompts this player to enter a move and attempts the move
    public boolean makeMove(Board board, Scanner scanner)
    {
        while(true)
        {
            System.out.print("  " + name + "'s move (e.g. E2 E4) or 'quit': ");
            String line = scanner.nextLine().trim();

            if(line.equalsIgnoreCase("quit") || line.equalsIgnoreCase("resign"))
            {
                return false; //signals resignation
            }

            //Parses the move
            Position[] parsed = parseMove(line);
            if(parsed == null)
            {
                System.out.println("  Invalid format. Use: <FROM> <TO>  e.g. E2 E4");
                continue;
            }

            Position from = parsed[0];
            Position to = parsed[1];

            if(board.movePiece(from, to, color))
            {
                return true; //move accepted
            }
            //loop back
        }
    }

    //Parses a raw input string into [from, to]
    //Accepts:
    //Tokens separated by one or more spaces: "E2  E3" "E2E3"
    //The input is normalised to uppercase before parsing.
    public static Position[] parseMove(String input)
    {
        if(input == null)
        {
            return null;
        }
        String s = input.trim().toUpperCase();

        String fromStr, toStr;

        //Tries splitting on whitespace
        String[] tokens = s.split("\\s+");
        if(tokens.length >= 2)
        {
            fromStr = tokens[0];
            //"E7 E8=Q" — strips promotion suffix, promotion is not supported
            toStr = tokens[1].replaceAll("=.*", "");
        }
        else if(s.length() >= 4)
        {
            //Compact: "E2E4"
            fromStr = s.substring(0, 2);
            toStr   = s.substring(2, 4).replaceAll("=.*", "");
        }
        else
        {
            return null;
        }

        try
        {
            Position from = Position.fromNotation(fromStr);
            Position to   = Position.fromNotation(toStr);
            return new Position[]{from, to};
        }
        catch(IllegalArgumentException e)
        {
            return null;
        }
    }
}
