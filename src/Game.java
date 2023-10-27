import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;
import java.util.StringJoiner;
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
import util.Table;

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
        ArrayList<String> reginaBanter = getBanterFromFile("src\\reginaBanter.txt");

        for (int i = 0; i < totalMonsters; i++) {
            BoardPosition monsterStart;
            do {
                monsterStart = createRandomPieceStart();
            } while (!this.board.isCellEmpty(monsterStart));

            this.board.setCell(monsterStart, new Enemy(monsterStart, getRandomBanter(reginaBanter)));
        }

        int totalNPCs = (int) Math.floor(totalMonsters / 2);
        ArrayList<String> npcBanter = getBanterFromFile("src\\npcJobsAndBanter.txt");

        for (int i = 0; i < totalNPCs; i++) {
            BoardPosition npcStart;
            do {
                npcStart = createRandomPieceStart();
            } while (!this.board.isCellEmpty(npcStart));

            String banterAndJob = getRandomBanter(npcBanter);
            this.board.setCell(npcStart, new NonEnemy(npcStart, banterAndJob.split(":")[0],
                    banterAndJob.split(":")[1]));
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

    private ArrayList<String> getBanterFromFile(String fileName) {
        ArrayList<String> banter = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                banter.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return banter;
    }

    private String getRandomBanter(ArrayList<String> banter) {
        Random rand = new Random();
        int randomIndex = rand.nextInt(banter.size());
        return banter.get(randomIndex);
    }

    public void moveBoardPiece(IBoardPiece boardPiece, int deltaX, int deltaY) throws Exception {
        BoardPosition currentPos = this.player.getPosition();
        BoardPosition newPos = new BoardPosition(currentPos.getX() + deltaX, currentPos.getY() + deltaY);

        if (newPos.getX() < 0 || newPos.getX() >= this.board.getSize() || newPos.getY() < 0 || newPos.getY() >= this.board.getSize()) {
            throw new Exception("Out of bounds, try again");
        }

        IBoardPiece currentOccupier = this.board.setCell(newPos, boardPiece);
        this.board.setCell(currentPos, null);
        this.player.setPosition(newPos);

        if (currentOccupier instanceof Treasure) {
            treasures.remove(currentOccupier);
        } else if (currentOccupier instanceof Enemy || currentOccupier instanceof NonEnemy) {
            // System.out.println(((NPC)currentOccupier).getMessage());

            printTextBox(currentOccupier.getName(), ((NPC)currentOccupier).getMessage());
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
            printTextBox("Enter your next move", "Valid moves: up, down, left, right, up-left, up-right, down-left, down-right\nHelp: hint\nQuit: quit\nTip: you can use also vim keys (h, j, k, l)");

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
                    case "up-left":
                    case "y":
                        moveBoardPiece(this.player, -1, -1);
                        break;
                    case "up-right":
                    case "u":
                        moveBoardPiece(this.player, 1, -1);
                        break;
                    case "down-left":
                    case "i":
                        moveBoardPiece(this.player, -1, 1);
                        break;
                    case "down-right":
                    case "o":
                        moveBoardPiece(this.player, 1, 1);
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
                printTextBox("Invalid move", e.getMessage());
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

     /**
     * +--- Title (can be any length) --------------------------------------------+
       | This is a test message, which will eventually be replaced by a message   | 
       | from either an enemy (Regina), or an NPC!                                |  
       +--------------------------------------------------------------------------+
     */
    public void printTitleBanner() {
        try {
            System.out.println(new String(Files.readAllBytes(Paths.get("src\\titleBanner.txt"))));
            printTextBox("Welcome to the game", "Select a size and number of treasures to begin");
        } catch(Exception e) {
            System.out.println("Oh no!\n" + e);
        }
    }

    private void printTextBox(String title, String message) {
        List<String[]> rows = new ArrayList<>();
            rows.add(new String[] { (title + ":") });
            rows.add(new String[] { 
                message
            });

            Table.table(rows, true, 1, true);
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
