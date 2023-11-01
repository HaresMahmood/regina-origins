package com.lbg.chars;

import com.lbg.board.BoardPosition;

/**
 * Treasure class represents a character in the game that holds a donut stash.
 * It extends the Character class and sets the name to "Donut Stash" and the symbol to 'T'.
 * @param position The position of the Treasure on the game board.
 */
public class Treasure extends Character {

    /**
     * Constructor for the Treasure class.
     * @param position The position of the Treasure on the game board.
     */
    public Treasure(BoardPosition position) {
        super(position);
        this.name = "Donut Stash";
        this.symbol = '?';
    }
}
