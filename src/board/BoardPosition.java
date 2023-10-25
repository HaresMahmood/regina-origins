package board;

/**
 * Represents a position on a game board with an x and y coordinate.
 */
public class BoardPosition {

    private int x;
    private int y;

    /**
     * Constructs a new BoardPosition object with the given x and y coordinates.
     * @param x the x coordinate of the position
     * @param y the y coordinate of the position
     */
    public BoardPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the x coordinate of the position.
     * @return the x coordinate of the position
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x coordinate of the position.
     * @param x the new x coordinate of the position
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Returns the y coordinate of the position.
     * @return the y coordinate of the position
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y coordinate of the position.
     * @param y the new y coordinate of the position
     */
    public void setY(int y) {
        this.y = y;
    }
}