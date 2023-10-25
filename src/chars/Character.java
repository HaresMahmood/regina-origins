package src.chars;

import src.utils.Position;

// Teams is fd for me, you can continue without me

public abstract class Character {
    protected Position position;
    protected String name;  
    protected char symbol; 

    // Constructor
    public Character(Position position) {
        this.position = position;
    }

    // Getters & setters
    public Position getPosition() {
        return this.position;
    }

    public void setPosition(int x, int y) {
        position.setX(x);
        position.setY(y);
    }

    // TODO: Explain
    public void setPosition(Position position) {
        this.position = position;
    }

    public String getName() {
        return this.name;
    }

    public char getSymbol() {
        return this.symbol;
    }
}