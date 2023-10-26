import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import board.Board;
import board.BoardPosition;
import chars.IBoardPiece;
import chars.NPC;
import chars.NonEnemy;
import chars.Player;
import chars.Enemy;
import chars.Treasure;

public class Game {

    private Board board;
    private Player player;
    private List<Treasure> treasures;
    private GameStatus gameStatus;

    /**
    public Game(Board board) {
        this.board = board;
    }
    */

    public void setup() {
        printTitleBanner();

        Scanner scanner = new Scanner(System.in);
        int size = 0;

        while (true) {
            System.out.println("What size of grid do you want?");
            String userInput = scanner.nextLine();

            try {
                size = Integer.parseInt(userInput);
                if (size > 2 && size < 11) {
                    break;
                }
            } catch (Exception e) {}

            System.out.println("Please enter a valid number 3-10");
        }

        int treasureCount = 0;
        while (true) {
            System.out.println("How many treasures should be on the map?");
            String userInput = scanner.nextLine();

            try {
                treasureCount = Integer.parseInt(userInput);
                if (treasureCount > 0 && treasureCount <= size) {
                    break;
                }
            } catch (Exception e) {}

            System.out.println("Please enter a valid number 1-" + size);
        }

        this.board = new Board(size);
        this.treasures = new ArrayList<>(treasureCount);

        for (int i = 0; i < treasureCount; i++) {
            BoardPosition treasureStart;
            do {
                treasureStart = createRandomPieceStart();
            } while (!this.board.isCellEmpty(treasureStart));

            Treasure treasure = new Treasure(treasureStart);
            this.treasures.add(treasure);
            this.board.setCell(treasureStart, treasure);
        }

        int totalMonsters = (int) Math.ceil((size * size) / 10);

        for (int i = 0; i < totalMonsters; i++) {
            BoardPosition monsterStart;
            do {
                monsterStart = createRandomPieceStart();
            } while (!this.board.isCellEmpty(monsterStart));

            this.board.setCell(monsterStart, new Enemy(monsterStart, "I'm going to eat your donut!"));
        }

        int totalNPCs = (int) Math.floor(totalMonsters / 2);

        for (int i = 0; i < totalNPCs; i++) {
            BoardPosition npcStart;
            do {
                npcStart = createRandomPieceStart();
            } while (!this.board.isCellEmpty(npcStart));

            this.board.setCell(npcStart, new NonEnemy(npcStart, "I'm not dangerous!"));
        }

        BoardPosition playerStart = createRandomPieceStart();

        this.player = new Player(playerStart);
        this.board.setCell(playerStart, this.player);

        this.gameStatus = GameStatus.RUNNING;
    }

    private BoardPosition createRandomPieceStart() {
        Random rand = new Random();
        int size = this.board.getSize();
        int x, y;
        BoardPosition charStart;

        do {
            x = rand.nextInt(size);
            y = rand.nextInt(size);
            charStart = new BoardPosition(x, y);
        } while (!this.board.isCellEmpty(charStart));

        return charStart;
    }

    public void moveBoardPiece(IBoardPiece boardPiece, int deltaX, int deltaY) throws Exception {
        BoardPosition currentPos = this.player.getPosition();
        BoardPosition newPos = new BoardPosition(currentPos.getX() + deltaX, currentPos.getY() + deltaY);

        if (newPos.getX() < 0 || newPos.getX() >= this.board.getSize() || newPos.getY() < 0 || newPos.getY() >= this.board.getSize()) {
            throw new Exception("Out of bounds!");
        }

        IBoardPiece currentOccupier = this.board.setCell(newPos, boardPiece);
        this.board.setCell(currentPos, null);
        this.player.setPosition(newPos);

        if (currentOccupier instanceof Treasure) {
            treasures.remove(currentOccupier);
        } else if (currentOccupier instanceof Enemy || currentOccupier instanceof NonEnemy) {
            System.out.println(((NPC)currentOccupier).getMessage());
        }
        
        if (currentOccupier instanceof Enemy) {
            this.changeGameStatus(GameStatus.LOSE);
        }

        if (treasures.isEmpty()) {
            this.gameStatus = GameStatus.WIN;
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
                        double minDist = Double.MAX_VALUE;
                        for (Treasure treasure : treasures) {
                            double dist = BoardPosition.getDistance(this.player.getPosition(), treasure.getPosition());
                            if(dist<minDist){
                                minDist = dist; // store the new smallest distance
                            }
                        }
                        System.out.println("The clostest treasure is " + minDist + " distance away!");
                        System.out.println("There are " + treasures.size() + " treasures left!");
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
        game.setup();
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
