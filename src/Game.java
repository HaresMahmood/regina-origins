import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import board.Board;
import board.BoardPosition;
import chars.Player;

public class Game {

    private Board board;

    /**
    public Game(Board board) {
        this.board = board;
    }
     */

    public void setup() {
        printTitleBanner();

        System.out.println("What size of grid do you want?");
        Scanner scanner = new Scanner(System.in);

        int size = 0;
        while(true) {
            String userInput = scanner.nextLine();
            try {
                size = Integer.parseInt(userInput);
                if (size > 2 && size < 11) {
                    break;
                } else {
                    System.out.println("Please enter a valid number 3-10");
                }
            } catch (Exception e) {
                System.out.println("Please enter a valid number!");
            }
        }

        this.board = new Board(size);
    }

    public void play(){

        Scanner scanner = new Scanner(System.in);
        while(true){
            board.printBoard();
            System.out.println("Where would you like to move next? (Enter quit to exit): ");

            String userInput = scanner.nextLine().toLowerCase();
            
            switch (userInput) {
                case "up":

                    break;
                case "down":

                    break;
                case "left":

                    break;
                case "right":

                    break;
            }
            
            if(userInput.equals("quit")){
                break;
            }
        }

        scanner.close();
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.setup();
        game.play();
    }

    public static void printTitleBanner() {
        try{
            System.out.println(new String(Files.readAllBytes(Paths.get("src\\titleBanner.txt"))));
        } catch(Exception e) {
            System.out.println("Oh no!\n" + e);
        }
    }
}
