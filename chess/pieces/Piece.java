//03-22-2026
package chess.pieces;

import chess.position.Position;
import chess.utils.Color;
import java.util.List;

import java.io.Serializable;

/**
 *Abstract base class for all chess piece implemention.
 *
 *Every concrete piece must provide its own implementation of
 *possibleMoves(chess.pieces.Piece[][])} that returns the squares it
 *can legally reach ignoring check (check filtering is handled by
 *the Board.
 */
public abstract class Piece implements Serializable
{

    //The color the piece belongs to
    private final Color color;

    //Current board position. null means the piece has been captured
    private Position position;

    //Constructs a piece with the given color and starting position.
    //color WHITE or BLACK
    //position initial board position
    protected Piece(Color color, Position position)
    {
        this.color    = color;
        this.position = position;
    }

    //Computes all squares this piece can move to from its current position,
    public abstract List<Position> possibleMoves(Piece[][] board);

    //Returns the two-character symbol for the piece
    public abstract String getSymbol();

    //Returns the color of this piece
    public Color getColor() {
        return color;
    }

    //Returns the current position
    public Position getPosition() {
        return position;
    }

    //Updates the position of this piece
    public void setPosition(Position newPosition) {
        this.position = newPosition;
    }

    //Returns true if the piece at board[r][c] belongs to the opponent of this piece
    protected boolean isEnemy(Piece[][] board, int r, int c)
    {
        Piece target = board[r][c];
        return target != null && target.getColor() != this.color;
    }

    //Returns true if board[r][c] is occupied by a friendly piece
    protected boolean isFriendly(Piece[][] board, int r, int c)
    {
        Piece target = board[r][c];
        return target != null && target.getColor() == this.color;
    }

    //Returns the symbol for quick debugging
    @Override
    public String toString() {
        return getSymbol();
    }
}
