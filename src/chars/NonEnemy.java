package chars;

import board.BoardPosition;

public class NonEnemy extends NPC {
    /**
     * Constructor for creating a new Player object.
     * @param position The initial position of the player on the game board.
     */
    public NonEnemy(BoardPosition position, String name, String message) {
        super(position, message);
        this.name = name;
        this.mugshotFileName = "npcMugshot.txt";
    }
}
