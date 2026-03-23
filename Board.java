package chess.board;

import chess.pieces.*;
import chess.position.Position;
import chess.utils.Color;
import java.util.ArrayList;
import java.util.List;

/**
 *Represents the chess board and manages game logic.
 *
 *Initialises the board with pieces in their starting positions.
 *Detects check, checkmate, and stalemate.
 */
public class Board
{

    //The 8×8 grid
    private Piece[][] grid;

    // Pieces captured during current game
    private final List<Piece> capturedPieces;

    //Makes an empty boardand calls init() to place all pieces in their starting positions
    public Board()
    {
        grid = new Piece[8][8];
        capturedPieces = new ArrayList<>();
        init();
    }

    /**
     * Places all pieces in their starting positions.
     * Row 0 = rank 8 (black back rank), row 7 = rank 1 (white back rank).
     */
    public void init()
    {
        grid = new Piece[8][8];
        capturedPieces.clear();

        //Black back rank
        placePiece(new Rook  (Color.BLACK, new Position(0, 0)));
        placePiece(new Knight(Color.BLACK, new Position(0, 1)));
        placePiece(new Bishop(Color.BLACK, new Position(0, 2)));
        placePiece(new Queen (Color.BLACK, new Position(0, 3)));
        placePiece(new King  (Color.BLACK, new Position(0, 4)));
        placePiece(new Bishop(Color.BLACK, new Position(0, 5)));
        placePiece(new Knight(Color.BLACK, new Position(0, 6)));
        placePiece(new Rook  (Color.BLACK, new Position(0, 7)));

        //Black pawns
        for(int i = 0; i < 8; i++)
        {
            placePiece(new Pawn(Color.BLACK, new Position(1, i)));
        }

        //White pawns
        for(int j = 0; j < 8; j++)
        {
            placePiece(new Pawn(Color.WHITE, new Position(6, j)));
        }

        //White back rank
        placePiece(new Rook  (Color.WHITE, new Position(7, 0)));
        placePiece(new Knight(Color.WHITE, new Position(7, 1)));
        placePiece(new Bishop(Color.WHITE, new Position(7, 2)));
        placePiece(new Queen (Color.WHITE, new Position(7, 3)));
        placePiece(new King  (Color.WHITE, new Position(7, 4)));
        placePiece(new Bishop(Color.WHITE, new Position(7, 5)));
        placePiece(new Knight(Color.WHITE, new Position(7, 6)));
        placePiece(new Rook  (Color.WHITE, new Position(7, 7)));
    }

    //Places a piece on the board at its current position.
    private void placePiece(Piece piece)
    {
        Position p = piece.getPosition();
        grid[p.getRow()][p.getCol()] = piece;
    }

    //Returns the piece at the specified position or null if empty.
    public Piece getPiece(Position pos)
    {
        return grid[pos.getRow()][pos.getCol()];
    }

    //Returns a read-only view of the internal 8×8 grid.
    public Piece[][] getGrid()
    {
        return grid;
    }

    //Returns the list of pieces captured so far (both colors).
    public List<Piece> getCapturedPieces()
    {
        return capturedPieces;
    }

    /**
     *Attempts to move the piece at "from" to "to".
     *
     *Validates:
     *There is a piece at "from"
     *The piece belongs to currentTurn
     *The destination is a legal move for that piece
     *
     *If validation passes, the board is updated and the method returns true
     *Otherwise it returns false and prints a reason.
     */
    public boolean movePiece(Position from, Position to, Color currentTurn)
    {
        Piece piece = getPiece(from);

        if(piece == null)
        {
            System.out.println("  No piece at " + from + ".");
            return false;
        }
        if(piece.getColor() != currentTurn)
        {
            System.out.println("  That is not your piece.");
            return false;
        }

        List<Position> legal = getLegalMoves(piece);
        boolean found = false;
        for(Position p : legal)
        {
            if(p.equals(to))
            {
                found = true; break;
            }
        }
        if(!found)
        {
            System.out.println("  Illegal move: " + from + " -> " + to + ".");
            return false;
        }

        executeMove(from, to);
        return true;
    }

    //Executes a move unconditionally
    //Updates the grid, moves the piece object, records a capture if any.
    private void executeMove(Position from, Position to)
    {
        Piece moving  = grid[from.getRow()][from.getCol()];
        Piece target  = grid[to.getRow()][to.getCol()];

        if(target != null)
        {
            capturedPieces.add(target);
        }

        grid[to.getRow()][to.getCol()] = moving;
        grid[from.getRow()][from.getCol()] = null;
        moving.setPosition(to);
    }

    //Returns all fully legal moves for the given piece.
    public List<Position> getLegalMoves(Piece piece)
    {
        List<Position> candidates = piece.possibleMoves(grid);
        List<Position> legal = new ArrayList<>();

        for(Position target : candidates)
        {
            if(!moveLeavesKingInCheck(piece.getPosition(), target, piece.getColor()))
            {
                legal.add(target);
            }
        }
        return legal;
    }

    //Simulates a move on a copy of the grid and checks whether the king is in check afterwards.
    private boolean moveLeavesKingInCheck(Position from, Position to, Color color)
    {
        Piece[][] copy = copyGrid();

        Piece moving = copy[from.getRow()][from.getCol()];
        copy[to.getRow()][to.getCol()]     = moving;
        copy[from.getRow()][from.getCol()] = null;

        //Temporarily update the piece's position so possibleMoves() works
        Position savedPos = moving.getPosition();
        moving.setPosition(to);
        boolean inCheck = isInCheckOnGrid(copy, color);
        moving.setPosition(savedPos);   //restore — this is the live piece object
        return inCheck;
    }

    
    //Returns whether the king is currently in check on the live board.
    //Returns true if so
    public boolean isInCheck(Color color)
    {
        return isInCheckOnGrid(grid, color);
    }

    //Checks whether the king is in check on the given grid.
    //Returns true if king is attacked
    private boolean isInCheckOnGrid(Piece[][] g, Color color)
    {
        //Finds king
        Position kingPos = null;
        outer:
        for(int r = 0; r < 8; r++)
        {
            for(int c = 0; c < 8; c++)
            {
                Piece p = g[r][c];
                if(p instanceof King && p.getColor() == color)
                {
                    kingPos = new Position(r, c);
                    break outer;
                }
            }
        }

        //Checks whether enemy piece can reach the king square
        for(int r = 0; r < 8; r++)
        {
            for(int c = 0; c < 8; c++)
            {
                Piece enemy = g[r][c];
                if(enemy != null && enemy.getColor() != color)
                {
                    //Temporarily sets its position so possibleMoves() works
                    Position savedPos = enemy.getPosition();
                    enemy.setPosition(new Position(r, c));
                    List<Position> attacks = enemy.possibleMoves(g);
                    enemy.setPosition(savedPos);  //restore
                    for (Position att : attacks)
                    {
                        if (att.equals(kingPos))
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    //Returns true if the given color has no legal moves
    public boolean hasNoLegalMoves(Color color)
    {
        for(int r = 0; r < 8; r++)
        {
            for(int c = 0; c < 8; c++)
            {
                Piece p = grid[r][c];
                if(p != null && p.getColor() == color)
                {
                    if(!getLegalMoves(p).isEmpty())
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //Returns true if the given color is in checkmate.
    public boolean isCheckmate(Color color)
    {
        return isInCheck(color) && hasNoLegalMoves(color);
    }

    //Returns true if stalemate for the given color.
    public boolean isStalemate(Color color)
    {
        return !isInCheck(color) && hasNoLegalMoves(color);
    }

    //Creates a shallow copy of the grid (same piece objects, new array).
    private Piece[][] copyGrid()
    {
        Piece[][] copy = new Piece[8][8];
        for(int r = 0; r < 8; r++)
        {
            System.arraycopy(grid[r], 0, copy[r], 0, 8);
        }
        return copy;
    }

    //Prints the current board state
    //Empty light squares are "  " and dark squares are "##"
    public void display()
    {
        System.out.println();
        System.out.println("     A    B    C    D    E    F    G    H");
        System.out.println("   +----+----+----+----+----+----+----+----+");
        for(int r = 0; r < 8; r++)
        {
            int rank = 8 - r;
            System.out.print(" " + rank + " |");
            for(int c = 0; c < 8; c++)
            {
                Piece p = grid[r][c];
                if(p != null)
                {
                    System.out.print(" " + p.getSymbol() + " |");
                }
                else
                {
                    //Checkered empty squares
                    String empty = ((r + c) % 2 == 0) ? "    " : " ## ";
                    System.out.print(empty + "|");
                }
            }
            System.out.println(" " + rank);
        }
        System.out.println("   +----+----+----+----+----+----+----+----+");
        System.out.println("     A    B    C    D    E    F    G    H");
        System.out.println();
    }
}
