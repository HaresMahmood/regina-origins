package com.lbg.chars;

import com.lbg.board.BoardPosition;

/**
 * The Player class represents a player character in the game.
 * It extends the Character class and adds methods for moving the player.
 */
public class Player extends Character {
    private boolean isAlive = true;
    private int donuts;

    /**
     * Constructor for creating a new Player object.
     * @param position The initial position of the player on the game board.
     */
    public Player(BoardPosition position) {
        super(position);
        this.name = "Donald";
        this.symbol = 'P';
        this.donuts = 0;
    }

    /**
     * Constructor for creating a new Player object.
     * @param position The initial position of the player on the game board.
     * @param name The name of the player
     * @param symbol The symbol to show the player on the board
     */
    public Player(BoardPosition position, String name, char symbol) {
        super(position);
        this.name = name;
        this.symbol = symbol;
        this.donuts = 0;
    }
    

    /**
     * Moves the player to the specified position on the game board.
     * @param position The new position of the player.
     */
    public void move(BoardPosition position) {
        this.position = position;
    }

    public void incrementDonuts(){
        this.donuts++;
    }

    public int getDonuts(){
        return this.donuts;
    }

    public boolean isAlive(){
        return this.isAlive;
    }

    public void kill(){
        this.isAlive = false;
    }
}