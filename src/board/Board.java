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
    
    private IBoardPiece[][] grid;

    /**
     * Constructs a new Board object with the specified size.
     *
     * @param size the size of the board
     */
    public Board(int size) {
        this.grid = new IBoardPiece[size][size];
    }

    /**
     * Prints the current state of the board to the console.
     * Empty spaces are represented by a period (".") character.
     * Non-empty spaces are represented by the symbol of the IBoardPiece occupying the space.
     */
    public void printBoard() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if (grid[i][j] != null) {
                    System.out.print(grid[i][j].getSymbol());
                } else {
                    System.out.print(".");
                }
            }
            System.out.print("\n");
        }
    }
}
