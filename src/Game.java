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
    private Player currentPlayer;
    private List<Player> players;
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

        int playerCount = 0;
        while (true) {
            System.out.println("How many players are there?");
            String userInput = scanner.nextLine();

            try {
                playerCount = Integer.parseInt(userInput);
                if (playerCount >= 1 && playerCount <= 4) {
                    break;
                }
            } catch (Exception e) {}

            System.out.println("Please enter a valid number 1-4");
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

        this.players = new ArrayList<>();


        for (int i = 0; i < playerCount; i++) {
            System.out.println("What is the name of Player " + (i+1));
            String name = scanner.nextLine();
            System.out.println("What symbol can be used for this player?");
            char symbol = scanner.nextLine().charAt(0);

            BoardPosition playerStart = createRandomPieceStart();

            Player newPlayer = new Player(playerStart, name, symbol);
            players.add(newPlayer);
            this.board.setCell(playerStart, newPlayer);
        }
        
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
        BoardPosition currentPos = this.currentPlayer.getPosition();
        BoardPosition newPos = new BoardPosition(currentPos.getX() + deltaX, currentPos.getY() + deltaY);

        if (newPos.getX() < 0 || newPos.getX() >= this.board.getSize() || newPos.getY() < 0 || newPos.getY() >= this.board.getSize()) {
            throw new Exception("Out of bounds!");
        }

        IBoardPiece currentOccupier = this.board.setCell(newPos, boardPiece);
        this.board.setCell(currentPos, null);
        this.currentPlayer.setPosition(newPos);

        if (currentOccupier instanceof Treasure) {
            treasures.remove(currentOccupier);
        } else if (currentOccupier instanceof Enemy || currentOccupier instanceof NonEnemy) {
            // System.out.println(((NPC)currentOccupier).getMessage());

            printTextBox(currentOccupier);
        }
        
        if (currentOccupier instanceof Enemy) {
            this.players.remove(this.currentPlayer);
            this.board.setCell(this.currentPlayer.getPosition(), currentOccupier);
            if(this.players.size()==0){
                this.changeGameStatus(GameStatus.LOSE);
            }
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
            for (Player player : players) {
                if (this.gameStatus != GameStatus.RUNNING) {
                    break;
                }
                this.currentPlayer = player;
                boolean repeat = true;

                System.out.println("------------" + player.getName() + "'s turn!" + "------------");

                while(repeat){
                    repeat=false;
                    board.printBoard();
                    System.out.println("Where would you like to move next? (Enter quit to exit): ");
                    String userInput = scanner.nextLine().toLowerCase();

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
                                double minDist = Double.MAX_VALUE;
                                for (Treasure treasure : treasures) {
                                    double dist = BoardPosition.getDistance(this.currentPlayer.getPosition(), treasure.getPosition());
                                    if(dist<minDist){
                                        minDist = dist; // store the new smallest distance
                                    }
                                }
                                System.out.println("The clostest treasure is " + minDist + " distance away!");
                                System.out.println("There are " + treasures.size() + " treasures left!");
                                break;
                            default:
                                System.out.println("That is not a valid instruction!");
                                repeat=true;
                                break;
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        repeat = true;
                    }
                    
                    if (userInput.equals("quit")){
                        break;
                    }
                }
                
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

    // private void printTextBox(IBoardPiece boardPiece) {
    //     StringBuilder sb = new StringBuilder();
    
    //     String characterName = boardPiece.getName();
    //     String message = "This is a very long message, which spans multiple lines. It will be displayed in the message box, even if it is too long to fit on one line.";
    
    //     // Calculate the width of the longest line in the message or the character name, whichever is longer.
    //     int maxLineWidth = Math.max(characterName.length() + 4, message.length() + 2);
    
    //     // Append the top border of the message box.
    //     sb.append("+--- " + characterName + " ");
    //     for (int i = 0; i < maxLineWidth - characterName.length() - 4; i++) {
    //         sb.append("-");
    //     }
    //     sb.append("-+\n");
    
    //     // Split the message into multiple lines, if necessary.
    //     String[] messageLines = message.split("\n");
    
    //     // Append each line of the message to the StringBuilder object, with the appropriate padding.
    //     for (String messageLine : messageLines) {
    //         sb.append("| " + messageLine);
    //         for (int i = 0; i < maxLineWidth - messageLine.length() - 2; i++) {
    //             sb.append(" ");
    //         }
    //         sb.append("|\n");
    //     }
    
    //     // Append the bottom border of the message box.
    //     for (int i = 0; i < maxLineWidth; i++) {
    //         sb.append("-");
    //     }
    //     sb.append("+\n");
    
    //     // Print the output message to the console.
    //     System.out.println(sb.toString());
    // }

    private void printTextBox(IBoardPiece boardPiece) {
        List<String[]> rows = new ArrayList<>();
            rows.add(new String[] { (boardPiece.getName() + ":") });
            rows.add(new String[] { 
                // "/\\ /\n((ovo))\n():::()\n  VVV",
                "This is a test message, which will eventually be replaced by a message from either an enemy (Regina), or an NPC!" 
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
