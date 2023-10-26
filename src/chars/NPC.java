package chars;

import board.BoardPosition;

public abstract class NPC extends Character {
    protected String message;

    public NPC(BoardPosition position, String message) {
        super(position);
        this.message = message;
    }
}