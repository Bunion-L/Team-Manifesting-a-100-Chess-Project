//03-23-2026
package chess.pieces;

import chess.position.Position;
import chess.utils.Color;
import java.util.ArrayList;
import java.util.List;

//Represents a Knight chess piece.
public class Knight extends Piece
{

    //All eight legal L-shaped moves a knight can make
    private static final int[][] OFFSETS = {{-2, -1}, {-2, 1}, {-1, -2}, {-1, 2}, { 1, -2}, { 1, 2}, { 2, -1}, { 2,  1}};

    //Constructs a Knight with the given color and starting position.
    public Knight(Color color, Position position) {
        super(color, position);
    }

    //Tests all eight L-shaped offsets and adds those that land on an empty or enemy occupied square
    @Override
    public List<Position> possibleMoves(Piece[][] board)
    {
        List<Position> moves = new ArrayList<>();
        int row = getPosition().getRow();
        int col = getPosition().getCol();

        for(int[] offset : OFFSETS)
        {
            int r = row + offset[0];
            int c = col + offset[1];
            if(r >= 0 && r < 8 && c >= 0 && c < 8)
            {
                if(!isFriendly(board, r, c)) {
                    moves.add(new Position(r, c));
                }
            }
        }
        return moves;
    }

    //returns "wN" for white and "bN" for black
    @Override
    public String getSymbol() {
        return (getColor() == Color.WHITE) ? "wN" : "bN";
    }
}
