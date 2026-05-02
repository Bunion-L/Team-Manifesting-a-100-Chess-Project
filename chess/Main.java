package chess;

import chess.game.Game;
import java.util.Scanner;

//Run this file to start the game
public class Main
{
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        boolean playAgain = true;
        while(playAgain)
        {
            Game game = new Game(scanner);
            game.start();
            game.play();

            System.out.print("  Play again? (yes/no): ");
            String answer = scanner.nextLine().trim().toLowerCase();
            playAgain = answer.equals("yes") || answer.equals("y");
        }

        System.out.println("  Thanks for playing!");
        scanner.close();
    }
}
