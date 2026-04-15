package chess.position;

import java.io.Serializable;


//Represents a square on the chess board using (row, col) coordinates.
public class Position implements Serializable
{

    //0-based row index (0 = rank 8, 7 = rank 1)
    private final int row;

    //0-based column index (0 = file A, 7 = file H)
    private final int col;

    //Constructs a Position with the given row and column.
    public Position(int row, int col)
    {
        this.row = row;
        this.col = col;
    }

    //Returns the 0-based row index
    public int getRow()
    {
        return row;
    }

    //Returns the 0-based column index.
    public int getCol()
    {
        return col;
    }

    //Checks whether this position lies within the 8×8 board.
    public boolean isValid()
    {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    //Parses a standard chess notation string like "E2" into a Position
    //throws IllegalArgumentException if the string is not valid notation
    public static Position fromNotation(String notation)
    {
        if(notation == null || notation.trim().length() < 2)
        {
            throw new IllegalArgumentException("Invalid notation: " + notation);
        }
        String s = notation.trim().toUpperCase();
        char file = s.charAt(0);
        char rank = s.charAt(1);
        if(file < 'A' || file > 'H' || rank < '1' || rank > '8')
        {
            throw new IllegalArgumentException("Invalid notation: " + notation);
        }
        int col = file - 'A';
        int row = '8' - rank; //rank '8' -> row 0, rank '1' -> row 7
        return new Position(row, col);
    }

    //Converts this position to standard algebraic notation like "E2"
    public String toNotation()
    {
        char file = (char) ('A' + col);
        char rank = (char) ('8' - row);
        return "" + file + rank;
    }

    //Positions are equal when they share the same row and column
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(!(obj instanceof Position))
        {
            return false;
        }
        Position other = (Position) obj;
        return this.row == other.row && this.col == other.col;
    }

    @Override
    public int hashCode()
    {
        return 31 * row + col;
    }

    @Override
    public String toString()
    {
        return toNotation();
    }
}
