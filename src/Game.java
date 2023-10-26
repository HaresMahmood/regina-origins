import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

import board.Board;
import board.BoardPosition;
import chars.IBoardPiece;
import chars.Player;
import chars.Treasure;

public class Game {

    private Board board;
    private Player player;
    private Treasure treasure;
    private GameStatus gameStatus;

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

        Random rand = new Random();

        int x = rand.nextInt(0, size);
        int y = rand.nextInt(0, size);

        BoardPosition playerStart = new BoardPosition(x, y);
        this.player = new Player(playerStart);
        this.board.setCell(this.player.getPosition(), this.player);

        x = rand.nextInt(0, size);
        y = rand.nextInt(0, size);

        BoardPosition treasureStart = new BoardPosition(x, y);
        this.treasure = new Treasure(treasureStart);
        this.board.setCell(this.treasure.getPosition(), this.treasure);

        this.gameStatus = GameStatus.RUNNING;
    }

    public void moveBoardPiece(IBoardPiece boardPiece, int deltaX, int deltaY) throws Exception {
        BoardPosition currentPos = this.player.getPosition();
        BoardPosition newPos = new BoardPosition(currentPos.getX() + deltaX, currentPos.getY() + deltaY);

        if (newPos.getX() >= 0 && newPos.getX() < this.board.getSize() &&
            newPos.getY() >= 0 && newPos.getY() < this.board.getSize()
        ) {
            IBoardPiece currentOccupier = this.board.setCell(newPos, boardPiece);
            this.board.setCell(currentPos, null);
            this.player.setPosition(newPos);

            if (currentOccupier instanceof Treasure) {
                this.changeGameStatus(GameStatus.WIN);
            }
        } else {
            throw new Exception("Out of bounds!");
        }
    }

    public void changeGameStatus(GameStatus newStatus) {
        this.gameStatus = newStatus;
    }

    public void play(){
        Scanner scanner = new Scanner(System.in);
        // Game loop
        while (this.gameStatus == GameStatus.RUNNING) {
            board.printBoard();
            System.out.println("Where would you like to move next? (Enter quit to exit): ");

            String userInput = scanner.nextLine().toLowerCase();

            try {
                switch (userInput) {
                    case "up":
                        moveBoardPiece(this.player, 0, -1);
                        break;
                    case "down":
                        moveBoardPiece(this.player, 0, 1);
                        break;
                    case "left":
                        moveBoardPiece(this.player, -1, 0);
                        break;
                    case "right":
                        moveBoardPiece(this.player, 1, 0);
                        break;
                    case "hint":
                        System.out.println("The treasure is " +
                                BoardPosition.getDistance(this.player.getPosition(), this.treasure.getPosition()) +
                                " distance away!");
                }
            } catch (Exception e) {
                System.out.println(e);
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

    public enum GameStatus {
        RUNNING,
        WIN,
        LOSE
    }
}
