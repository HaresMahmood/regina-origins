package com.lbg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

import com.lbg.board.Board;
import com.lbg.board.BoardPosition;
import com.lbg.chars.IBoardPiece;
import com.lbg.chars.NPC;
import com.lbg.chars.NonEnemy;
import com.lbg.chars.Player;
import com.lbg.chars.Enemy;
import com.lbg.chars.Treasure;

public class Main {

    private Board board;
    private Player currentPlayer;
    private List<Player> players;
    private List<Treasure> treasures;
    private GameStatus gameStatus;

    /**
     * public Game(Board board) {
     * this.board = board;
     * }
     */

    private int parseIntegerInput(Scanner scanner, String message, int min, int max) {
        int size = 0;

        while (true) {
            System.out.print(message);
            String userInput = scanner.nextLine();

            try {
                size = Integer.parseInt(userInput);
                if (size >= min && size <= max) {
                    break;
                }
            } catch (NumberFormatException e) {
            }

            System.out.println("Please enter a valid number between " + min + "-" + max);
        }

        return size;
    }

    public void setup() {
        printASCIIArtFile("titleBanner.txt");
        System.out.println("");
        printTextBox("Welcome to the game!",
                "Select a size and number of treasures to begin... Here's a slightly longer message. And here's an even longer one");
        System.out.println("");

        Scanner scanner = new Scanner(System.in);

        int size = parseIntegerInput(scanner, "Grid size: ", 3, 10);
        int treasureCount = parseIntegerInput(scanner, "Number of treasures: ", 1, size);
        int playerCount = parseIntegerInput(scanner, "Number of players: ", 1, 4);

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
        ArrayList<String> reginaBanter = getBanterFromFile("reginaBanter.txt");

        for (int i = 0; i < totalMonsters; i++) {
            BoardPosition monsterStart;
            do {
                monsterStart = createRandomPieceStart();
            } while (!this.board.isCellEmpty(monsterStart));

            this.board.setCell(monsterStart, new Enemy(monsterStart, getRandomBanter(reginaBanter)));
        }

        int totalNPCs = (int) Math.floor(totalMonsters / 2);
        ArrayList<String> npcBanter = getBanterFromFile("npcJobsAndBanter.txt");

        for (int i = 0; i < totalNPCs; i++) {
            BoardPosition npcStart;
            do {
                npcStart = createRandomPieceStart();
            } while (!this.board.isCellEmpty(npcStart));

            String banterAndJob = getRandomBanter(npcBanter);
            this.board.setCell(npcStart, new NonEnemy(npcStart, banterAndJob.split(":")[0],
                    banterAndJob.split(":")[1]));
        }

        this.players = new ArrayList<>();

        for (int i = 0; i < playerCount; i++) {
            System.out.print("Player " + (i + 1) + " name: ");
            String name = scanner.nextLine();
            System.out.print("Player symbol: ");
            char symbol = scanner.nextLine().charAt(0);

            BoardPosition playerStart = createRandomPieceStart();

            Player newPlayer = new Player(playerStart, name, symbol);
            players.add(newPlayer);
            this.board.setCell(playerStart, newPlayer);
        }
        
        System.out.println("");

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
        try (InputStream inputStream = getClass().getResourceAsStream("/" + fileName)) {
            if (inputStream == null) {
                System.out.println("File not found: " + fileName);
                return banter;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
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
        BoardPosition currentPos = this.currentPlayer.getPosition();
        BoardPosition newPos = new BoardPosition(currentPos.getX() + deltaX, currentPos.getY() + deltaY);

        if (newPos.getX() < 0 || newPos.getX() >= this.board.getSize() || newPos.getY() < 0
                || newPos.getY() >= this.board.getSize()) {
            throw new Exception("Out of bounds, try again");
        }

        IBoardPiece currentOccupier = this.board.setCell(newPos, boardPiece);
        this.board.setCell(currentPos, null);
        this.currentPlayer.setPosition(newPos);

        if (currentOccupier instanceof Treasure) {
            treasures.remove(currentOccupier);
            this.currentPlayer.incrementDonuts();
        } else if (currentOccupier instanceof Enemy || currentOccupier instanceof NonEnemy) {
            printASCIIArtFile(((NPC) currentOccupier).getMugshotFileName());
            printTextBox(currentOccupier.getName(), ((NPC) currentOccupier).getMessage());
            System.out.println("");
        }

        if (currentOccupier instanceof Enemy) {
            this.currentPlayer.kill();
            this.board.setCell(this.currentPlayer.getPosition(), currentOccupier);
            for (Player player : players) {
                if (player.isAlive()) {
                    return;
                }
            }
            this.gameStatus = GameStatus.LOSE;
        }

        if (treasures.isEmpty()) {
            this.gameStatus = GameStatus.WIN;
        }
    }

    public void changeGameStatus(GameStatus newStatus) {
        this.gameStatus = newStatus;
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);
        // Game loop
        while (this.gameStatus == GameStatus.RUNNING) {
            for (Player player : players) {
                if (this.gameStatus != GameStatus.RUNNING) {
                    break;
                }
                if (!player.isAlive()) {
                    continue;
                }

                this.currentPlayer = player;
                boolean repeat = true;

                printTextBox("Player " + player.getName() + "'s turn", "You have " + player.getDonuts()
                        + " donuts in your stash!\nYou are at position " + player.getPosition());
                System.out.println("");

                while (repeat) {
                    repeat = false;
                    board.printBoard(this.currentPlayer);
                    System.out.print("Where would you like to move next? (help/? for help): ");

                    String userInput = scanner.nextLine().toLowerCase();
                    System.out.println("");

                    try {
                        switch (userInput) {
                            case "up":
                            case "h":
                                moveBoardPiece(this.currentPlayer, 0, -1);
                                break;
                            case "down":
                            case "l":
                                moveBoardPiece(this.currentPlayer, 0, 1);
                                break;
                            case "left":
                            case "j":
                                moveBoardPiece(this.currentPlayer, -1, 0);
                                break;
                            case "right":
                            case "k":
                                moveBoardPiece(this.currentPlayer, 1, 0);
                                break;
                            case "up-left":
                            case "y":
                                moveBoardPiece(this.currentPlayer, -1, -1);
                                break;
                            case "up-right":
                            case "u":
                                moveBoardPiece(this.currentPlayer, 1, -1);
                                break;
                            case "down-left":
                            case "i":
                                moveBoardPiece(this.currentPlayer, -1, 1);
                                break;
                            case "down-right":
                            case "o":
                                moveBoardPiece(this.currentPlayer, 1, 1);
                                break;
                            case "hint":
                            case "hi":
                                double minDist = Double.MAX_VALUE;
                                for (Treasure treasure : treasures) {
                                    double dist = BoardPosition.getDistance(this.currentPlayer.getPosition(),
                                            treasure.getPosition());
                                    if (dist < minDist) {
                                        minDist = dist; // store the new smallest distance
                                    }
                                }
                                printTextBox("Hint", "The clostest treasure is " + minDist
                                        + " distance away!\nThere are " + treasures.size() + " treasures left!");
                                System.out.println("");
                                repeat = true;
                                break;
                            case "help":
                            case "?":
                                printTextBox("Help", "Valid moves: up, down, left, right, up-left, up-right, down-left, down-right \nHelp: hint \nQuit: quit \n\nTip: you can use also vim keys (h, j, k, l)");
                                System.out.println("");
                                repeat = true;
                                break;
                            default:
                                System.out.println("That is not a valid instruction (help/? for help)");
                                repeat = true;
                                break;
                        }
                    } catch (Exception e) {
                        printTextBox("Invalid move", e.getMessage());
                        repeat = true;
                    }

                    if (userInput.equals("quit")) {
                        break;
                    }
                }
            }
        }

        switch (this.gameStatus) {
            case WIN:
                printASCIIArtFile("happyDonutMan.txt");
                printTextBox("You win!", "You collected all the treasures! Congratulations!");
                break;
            case LOSE:
                printTextBox("You lose!", "You were attacked by Regina! Better luck next time!");
                break;
            default:
                break;
        }

        scanner.close();
    }

    public static void main(String[] args) {
        Main game = new Main();
        game.setup();
        game.play();
    }

    private void printASCIIArtFile(String fileName) {
        try (InputStream inputStream = getClass().getResourceAsStream("/" + fileName)) {
            if (inputStream == null) {
                System.out.println("File not found: " + fileName);
                return;
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            System.out.println("Oh no!\n" + e);
        }
    }

    /**
     * Output:
     * +--- Title (can be any length) --------------------------------------------+
     * | This is a test message, which will eventually be replaced by a message |
     * | from either an enemy (Regina), or an NPC! |
     * +--------------------------------------------------------------------------+
     * The textbox should dynamically resize to fit the message, and the title
     */
    private void printTextBox(String title, String message) {
        int width = Math.max(title.length() + 4, 84);

        // Print the top border of the textbox, with the title embedded.
        System.out.print("+--- ");
        System.out.print(title + " ");
        System.out.print("-".repeat(width - title.length() - 8));
        System.out.println("---+");

        // Print the message of the textbox.
        String[] lines = message.split("\\r?\\n");
        for (String line : lines) {
            String[] sublines = line.split("(?<=\\G.{80})");
            for (String subline : sublines) {
                System.out.println("| " + subline + " ".repeat(width - subline.length() - 2) + " |");
            }
        }

        // Print the bottom border of the textbox.
        System.out.println("+" + "-".repeat(width) + "+");
    }

    public enum GameStatus {
        RUNNING,
        WIN,
        LOSE
    }
}
