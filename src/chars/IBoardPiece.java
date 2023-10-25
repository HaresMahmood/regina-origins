package chars;

import board.BoardPosition;

public interface IBoardPiece {
    BoardPosition getPosition();
    String getName();
    char getSymbol();
}
