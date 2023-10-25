import java.util.Scanner;

import board.Board;
import board.BoardPosition;
import chars.Player;

public class Game {

    private Board board;

    public Game(Board board) {
        this.board = board;
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
        Game game = new Game(new Board(5));
        game.play();
    }
}
