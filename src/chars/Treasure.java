package chars;

import board.BoardPosition;

public class Treasure extends Character {

    public Treasure(BoardPosition position) {
        super(position);
        this.name = "Donut Stash";
        this.symbol = 'T';
    }
}
