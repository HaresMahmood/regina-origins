package board;

import chars.IBoardPiece;

public class Board {
    
    private IBoardPiece[][] grid;

    public Board(int size) {
        this.grid = new IBoardPiece[size][size];
    }

    public void setCell(BoardPosition boardPosition, IBoardPiece boardPiece) {
        this.grid[boardPosition.getY()][boardPosition.getX()] = boardPiece;
    }

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
