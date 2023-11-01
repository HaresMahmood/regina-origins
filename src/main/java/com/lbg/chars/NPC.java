package com.lbg.chars;

import com.lbg.board.BoardPosition;

public abstract class NPC extends Character {
    protected String message;
    protected String mugshotFileName;

    public NPC(BoardPosition position, String message) {
        super(position);
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public String getMugshotFileName() {
        return this.mugshotFileName;
    }
}