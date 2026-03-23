package chess.utils;

//Represents the color of a chess piece or player.
public enum Color
{

    //White pieces/player
    WHITE,

    //Black pieces/player
    BLACK;

    //Returns the opposite color
    public Color opposite()
    {
        return this == WHITE ? BLACK : WHITE;
    }
}
