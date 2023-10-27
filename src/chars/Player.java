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
     * Constructor for creating a new Player object.
     * @param position The initial position of the player on the game board.
     * @param name The name of the player
     * @param symbol The symbol to show the player on the board
     */
    public Player(BoardPosition position, String name, char symbol) {
        super(position);
        this.name = name;
        this.symbol = symbol;
    }
    

    /**
     * Moves the player to the specified position on the game board.
     * @param position The new position of the player.
     */
    public void move(BoardPosition position) {
        this.position = position;
    }
}