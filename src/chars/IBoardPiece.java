package chars;

import utils.BoardPosition;

public interface IBoardPiece {
    BoardPosition getPosition();
    String getName();
    char getSymbol();
}
