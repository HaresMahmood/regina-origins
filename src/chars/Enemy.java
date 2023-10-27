package chars;

import board.BoardPosition;

public class Enemy extends NPC {

    public Enemy(BoardPosition position, String message) {
        super(position, message);
        this.name = "Regina";
        this.mugshotFileName = "reginaMugshot.txt";
    }
}
