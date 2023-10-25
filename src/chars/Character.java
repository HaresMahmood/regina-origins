package chars;

import board.BoardPosition;

/**
 * An abstract class representing a character on the game board.
 * Implements the IBoardPiece interface.
 */
public abstract class Character implements IBoardPiece {
    protected BoardPosition position;
    protected String name;  
    protected char symbol;

    /**
     * Constructor for the Character class.
     * @param position The initial position of the character on the game board.
     */
    public Character(BoardPosition position) {
        this.position = position;
    }

    /**
     * Getter for the position of the character on the game board.
     * @return The position of the character on the game board.
     */
    @Override
    public BoardPosition getPosition() {
        return this.position;
    }

    /**
     * Getter for the name of the character.
     * @return The name of the character.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Getter for the symbol representing the character on the game board.
     * @return The symbol representing the character on the game board.
     */
    @Override
    public char getSymbol() {
        return this.symbol;
    }
}