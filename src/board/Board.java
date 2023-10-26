package board;

import java.util.StringJoiner;

import chars.IBoardPiece;
import chars.Player;

 /**  For Lorna:
    *     0  1  2  3  4
    * [
    *    [0, 0, 0, 0, 0], // 0
    *    [0, 0, 0, 0, 0], // 1
    *    [0, 0, 0, 0, 0], // 2
    *    [0, 0, 0, 0, 0], // 3
    *    [0, 0, 0, 0, 0]  // 4
    * ]
*/
/**
 * The Board class represents the game board for Regina Origins.
 * It contains a 2D array of IBoardPiece objects and provides methods for printing the board.
 *
 * Example usage:
 * <pre>
 * {@code
 * Board board = new Board(8);
 * board.printBoard();
 * }
 * </pre>
 */
public class Board {

    private int size;
    private IBoardPiece[][] grid;

    /**
     * Constructs a new Board object with the specified size.
     *
     * @param size the size of the board
     */
    public Board(int size) {
        this.size = size;
        this.grid = new IBoardPiece[this.size][this.size];
    }

    public IBoardPiece setCell(BoardPosition boardPosition, IBoardPiece boardPiece) {
        IBoardPiece currentOccupier = this.grid[boardPosition.getY()][boardPosition.getX()];

        this.grid[boardPosition.getY()][boardPosition.getX()] = boardPiece;

        return currentOccupier;
        // do a switch later in Game
    }

    public boolean isCellEmpty (BoardPosition boardPosition) {
        return this.grid[boardPosition.getY()][boardPosition.getX()] == null;
    }

    public int getSize() {
        return this.size;
    }

    
    /**
     * Prints the current state of the game board to the console.
     * Each cell of the board is represented by a symbol, and the board is surrounded by a border.
     * Empty cells are represented by two spaces.
     */
    public void printBoard() {
        StringBuilder sb = new StringBuilder();
        String lineSplit = "";
        StringJoiner splitJoiner = new StringJoiner("+", "|", "|");

        for (int index = 0; index < grid.length; index++) {
            splitJoiner.add(String.format("%4s", "").replace(" ", "-"));
        }

        lineSplit = splitJoiner.toString();
        sb.append(lineSplit).append("\n");

        for (IBoardPiece[] row : grid) {
            StringJoiner sj = new StringJoiner(" | ", "| ", " |");
            for (IBoardPiece piece : row) {
                if (piece != null) {
                    String format = (piece instanceof Player) ? "\u001B[47m\u001B[30m%2s\u001B[0m" : "%2s";

                    sj.add(String.format(format, piece.getSymbol()));
                } else {
                    sj.add("  ");
                }
            }
            
            sb.append(sj.toString()).append("\n");
            sb.append(lineSplit).append("\n");
        }

        System.out.println(sb.toString());
    }
}
