//03-23-2026
package chess.pieces;

import chess.position.Position;
import chess.utils.Color;
import java.util.ArrayList;
import java.util.List;


//Represents a King chess piece.
public class King extends Piece
{

    //Constructs a King with the given color and starting position
    public King(Color color, Position position) {
        super(color, position);
    }

    /**
     *Returns all squares reachable by a single-step king move that are
     *either empty or occupied by an enemy piece.  Squares attacked by the
     *opponent are filtered with Board.getLegalMoves()
     */
    @Override
    public List<Position> possibleMoves(Piece[][] board)
    {
        List<Position> moves = new ArrayList<>();
        int row = getPosition().getRow();
        int col = getPosition().getCol();

        for(int dr = -1; dr <= 1; dr++)
        {
            for(int dc = -1; dc <= 1; dc++)
            {
                if(dr == 0 && dc == 0)
                {
                    continue;
                }

                int r = row + dr;
                int c = col + dc;

                if(r >= 0 && r < 8 && c >= 0 && c < 8)
                {
                    if(!isFriendly(board, r, c))
                    {
                        moves.add(new Position(r, c));
                    }
                }
            }
        }
        return moves;
    }

    //returns "wK" for white and "bK" for black
    @Override
    public String getSymbol() {
        return (getColor() == Color.WHITE) ? "wK" : "bK";
    }
}
