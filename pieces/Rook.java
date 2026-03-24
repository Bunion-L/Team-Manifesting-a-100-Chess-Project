//03-22-2026
package chess.pieces;

import chess.position.Position;
import chess.utils.Color;
import java.util.ArrayList;
import java.util.List;

//Represents a Rook chess piece
public class Rook extends Piece
{

    //Constructs a Rook with the given color and starting position
    public Rook(Color color, Position position)
    {
        super(color, position);
    }

    @Override
    public List<Position> possibleMoves(Piece[][] board)
    {
        List<Position> moves = new ArrayList<>();
        int row = getPosition().getRow();
        int col = getPosition().getCol();

        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for(int[] d : directions)
        {
            int r = row + d[0];
            int c = col + d[1];
            while(r >= 0 && r < 8 && c >= 0 && c < 8)
            {
                if(board[r][c] == null) {
                    moves.add(new Position(r, c));
                }
                else
                {
                    if(isEnemy(board, r, c)) {
                        moves.add(new Position(r, c));
                    }
                    break; //blocked by piece stop sliding
                }
                r += d[0];
                c += d[1];
            }
        }
        return moves;
    }

    //returns "wR" for white and "bR" for black
    @Override
    public String getSymbol() {
        return (getColor() == Color.WHITE) ? "wR" : "bR";
    }
}
