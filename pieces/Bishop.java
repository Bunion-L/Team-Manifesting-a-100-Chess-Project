//03-22-2026
package chess.pieces;

import chess.position.Position;
import chess.utils.Color;
import java.util.ArrayList;
import java.util.List;

//Represents a Bishop chess piece.
public class Bishop extends Piece
{

    //Constructs a Bishop with given color and starting position
    public Bishop(Color color, Position position) {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves(Piece[][] board) {

        List<Position> moves = new ArrayList<>();
        int row = getPosition().getRow();
        int col = getPosition().getCol();

        int[][] directions = {{-1, -1}, {-1, 1}, {1, -1}, {1, 1}};
        for(int[] d : directions)
        {
            int r = row + d[0];
            int c = col + d[1];
            while(r >= 0 && r < 8 && c >= 0 && c < 8)
            {
                if(board[r][c] == null)
                {
                    moves.add(new Position(r, c));
                }
                else
                {
                    if(isEnemy(board, r, c))
                    {
                        moves.add(new Position(r, c));
                    }
                    break;
                }
                r += d[0];
                c += d[1];
            }
        }
        return moves;
    }

    //returns "wB" for white piece and "bB" for black piece
    @Override
    public String getSymbol() {
        return (getColor() == Color.WHITE) ? "wB" : "bB";
    }
}
