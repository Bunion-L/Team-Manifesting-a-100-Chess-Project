//03-22-2026
package chess.pieces;

import chess.position.Position;
import chess.utils.Color;
import java.util.ArrayList;
import java.util.List;

//Represents a Queen chess piece
public class Queen extends Piece
{

    //Constructs a Queen with given color and starting position.
    public Queen(Color color, Position position) {
        super(color, position);
    }

    //Slides along all eight directions (orthogonal + diagonal) until blocked.
    @Override
    public List<Position> possibleMoves(Piece[][] board)
    {
        List<Position> moves = new ArrayList<>();
        int row = getPosition().getRow();
        int col = getPosition().getCol();

        int[][] directions = {{-1,  0}, { 1,  0}, { 0, -1}, { 0,  1}, {-1, -1}, {-1,  1}, { 1, -1}, { 1,  1}};

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
                    break;
                }
                r += d[0];
                c += d[1];
            }
        }
        return moves;
    }

    //returns "wQ" for white and "bQ" for black
    @Override
    public String getSymbol() {
        return (getColor() == Color.WHITE) ? "wQ" : "bQ";
    }
}
