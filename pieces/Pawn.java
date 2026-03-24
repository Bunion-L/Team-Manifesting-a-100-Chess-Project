//03-23-2026
package chess.pieces;

import chess.position.Position;
import chess.utils.Color;
import java.util.ArrayList;
import java.util.List;

//Represents a Pawn chess piece.
public class Pawn extends Piece
{

    
    //Constructs a Pawn with given color and starting position.
    public Pawn(Color color, Position position) {
        super(color, position);
    }

    //White pawns advance toward lower row indices (row decreases)
    //Black pawns advance toward higher row indices (row increases).
    @Override
    public List<Position> possibleMoves(Piece[][] board)
    {
        List<Position> moves = new ArrayList<>();
        int row = getPosition().getRow();
        int col = getPosition().getCol();
        int dir = (getColor() == Color.WHITE) ? -1 : 1; //direction of movement

        //Single move forward
        int nextRow = row + dir;
        if(nextRow >= 0 && nextRow < 8 && board[nextRow][col] == null)
        {
            moves.add(new Position(nextRow, col));

            //Double move from starting spot
            int startRank = (getColor() == Color.WHITE) ? 6 : 1;
            if(row == startRank)
            {
                int doubleRow = row + 2 * dir;
                if(board[doubleRow][col] == null) {
                    moves.add(new Position(doubleRow, col));
                }
            }
        }

        //Diagonal captures (No en passant)
        for(int dc : new int[]{-1, 1})
        {
            int captureCol = col + dc;
            if(nextRow >= 0 && nextRow < 8 && captureCol >= 0 && captureCol < 8)
            {
                if(isEnemy(board, nextRow, captureCol)) {
                    moves.add(new Position(nextRow, captureCol));
                }
            }
        }
        return moves;
    }

    //returns "wp" for white and "bp"} for black
    @Override
    public String getSymbol() {
        return (getColor() == Color.WHITE) ? "wp" : "bp";
    }
}
