import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.BoardPosition;
import chars.IBoardPiece;
import chars.Player;
import chars.Treasure;

public class Game {

    private Board board;
    private Player player;
    private List<Treasure> treasures;
    private Treasure treasure;
    private GameStatus gameStatus;

    /**
    public Game(Board board) {
        this.board = board;
    }
    */

    public void setup(int treasureCount) {
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

        // Random rand = new Random();

        // int x = rand.nextInt(0, size);
        // int y = rand.nextInt(0, size);

        // BoardPosition playerStart = new BoardPosition(x, y);
        // this.player = new Player(playerStart);
        // this.board.setCell(this.player.getPosition(), this.player);

        // x = rand.nextInt(0, size);
        // y = rand.nextInt(0, size);

        // BoardPosition treasureStart = new BoardPosition(x, y);
        // this.treasure = new Treasure(treasureStart);
        // this.board.setCell(this.treasure.getPosition(), this.treasure);

        BoardPosition playerStart = createRandomPieceStart();
        this.player = new Player(playerStart);
        this.board.setCell(playerStart, this.player);

        this.treasures = new ArrayList<>(treasureCount);
        for (int i = 0; i < treasureCount; i++) {
            System.out.println(i);
            BoardPosition treasureStart = createRandomPieceStart();
            Treasure treasure = new Treasure(treasureStart);
            this.treasures.add(treasure);
            this.board.setCell(treasureStart, treasure);
        }

        this.gameStatus = GameStatus.RUNNING;
    }

    private BoardPosition createRandomPieceStart() {
        Random rand = new Random();

        int x = 0;
        int y = 0;
        BoardPosition charStart = null;

        do {
            x = rand.nextInt(0, this.board.getSize());
            y = rand.nextInt(0, this.board.getSize());
            charStart = new BoardPosition(x, y);
        } while (!this.board.isCellEmpty(charStart));

        return charStart;
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
                treasures.remove(currentOccupier);
            }
            if(treasures.isEmpty()){
                    this.gameStatus = GameStatus.WIN;
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
                    case "h":
                        moveBoardPiece(this.player, 0, -1);
                        break;
                    case "down":
                    case "l":
                        moveBoardPiece(this.player, 0, 1);
                        break;
                    case "left":
                    case "j":
                        moveBoardPiece(this.player, -1, 0);
                        break;
                    case "right":
                    case "k":
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
            
            if (userInput.equals("quit")){
                break;
            }
        }

        switch (this.gameStatus) {
            case WIN:
                printWinImage();
                break;
            case LOSE:
                System.out.println("Sucks to be you");
                break;
        }

        scanner.close();
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.setup(2);
        game.play();
    }

    public void printTitleBanner() {
        try {
            System.out.println(new String(Files.readAllBytes(Paths.get("src\\titleBanner.txt"))));
        } catch(Exception e) {
            System.out.println("Oh no!\n" + e);
        }
    }

    public void printWinImage() {
        try {
            System.out.println(new String(Files.readAllBytes(Paths.get("src\\happyDonutMan.txt"))));
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
