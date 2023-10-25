package chars;

import board.BoardPosition;

// Teams is fd for me, you can continue without me

public abstract class Character implements IBoardPiece {
    protected BoardPosition position;
    protected String name;  
    protected char symbol;

    // Constructor
    public Character(BoardPosition position) {
        this.position = position;
    }

    // Getters & setters
    @Override
    public BoardPosition getPosition() {
        return this.position;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public char getSymbol() {
        return this.symbol;
    }
}