package board;

import chars.IBoardPiece;

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

        if (currentOccupier == null) {
            this.grid[boardPosition.getY()][boardPosition.getX()] = boardPiece;
        }
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
     * Prints the current state of the board to the console.
     * Empty spaces are represented by a period (".") character.
     * Non-empty spaces are represented by the symbol of the IBoardPiece occupying the space.
     */
    public void printBoard() {
        // Prints the top border row
        System.out.print("╔");
        for (int x = 0; x < grid.length; x++) {
            System.out.print("═");
            if (x == grid.length - 1) {
                System.out.print("╗\n");
            }
        }
        // Start printing the rows between top and bottom border
        for (int i = 0; i < grid.length; i++) {
            // Print the left border
            System.out.print("║");
            // Print the grid row
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] != null) {
                    System.out.print(grid[i][j].getSymbol());
                } else {
                    System.out.print("+");
                }
            }
            // Print the right border
            System.out.print("║\n");
        }
        // Prints the bottom border row
        System.out.print("╚");
        for (int x = 0; x < grid.length; x++) {
            System.out.print("═");
            if (x == grid.length - 1) {
                System.out.print("╝\n");
            }
        }
    }
}
