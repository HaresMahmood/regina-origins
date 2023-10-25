package chars;

import utils.BoardPosition;

public class Player extends Character {
    // Constructor
    public Player(BoardPosition position) {
        super(position);
        this.name = "Donald";
        this.symbol = 'P';
    }

    public void move(int x, int y) {
        position.setX(x);
        position.setY(y);
    }

    public void move(BoardPosition position) {
        this.position = position;
    }
}