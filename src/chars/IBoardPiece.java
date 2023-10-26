package chars;

import board.BoardPosition;

/**
 * The IBoardPiece interface represents a game piece on a board.
 * It provides methods to get the position, name, and symbol of the piece.
 */
public interface IBoardPiece {
    
    /**
     * Returns the position of the board piece.
     * @return the position of the board piece
     */
    BoardPosition getPosition();

    void setPosition(BoardPosition position);
    
    /**
     * Returns the name of the board piece.
     * @return the name of the board piece
     */
    String getName();
    
    /**
     * Returns the symbol of the board piece.
     * @return the symbol of the board piece
     */
    char getSymbol();
}
