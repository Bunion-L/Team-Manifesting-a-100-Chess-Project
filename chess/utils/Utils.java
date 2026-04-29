package chess.utils;

//Utility class with static helper methods
//Cannot be instantiated
public final class Utils
{

    //Private constructor prevents instantiation
    private Utils()
    {

    }

    //Checks if the row and column are inside the board
    public static boolean inBounds(int row, int col)
    {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }

    //Normalises a direction value to −1 0 or +1
    public static int sign(int value)
    {
        return Integer.compare(value, 0);
    }

    //Returns a display label for the given color
    public static String colorLabel(Color color)
    {
        return color == Color.WHITE ? "White" : "Black";
    }

    //Centres a string within a field of the width with spaces
    public static String centre(String text, int width)
    {
        if(text.length() >= width)
        {
            return text;
        }
        int totalPad = width - text.length();
        int left  = totalPad / 2;
        int right = totalPad - left;
        return " ".repeat(left) + text + " ".repeat(right);
    }
}
