package chars;

import board.BoardPosition;

/**
 * The Player class represents a player character in the game.
 * It extends the Character class and adds methods for moving the player.
 */
public class Player extends Character {
    /**
     * Constructor for creating a new Player object.
     * @param position The initial position of the player on the game board.
     */
    public Player(BoardPosition position) {
        super(position);
        this.name = "Donald";
        this.symbol = 'P';
    }

    /**
     * Moves the player to the specified position on the game board.
     * @param x The x-coordinate of the new position.
     * @param y The y-coordinate of the new position.
     */
    public void setPosition(BoardPosition position) {
        this.position = position;
    }

    /**
     * Moves the player to the specified position on the game board.
     * @param position The new position of the player.
     */
    public void move(BoardPosition position) {
        this.position = position;
    }
}