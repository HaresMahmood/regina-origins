package src.chars;

public abstract class Character {
    protected int position;
    protected String name;  
    protected char symbol; 

    // Getters & setters
    public String getName() {
        return this.name;
    }

    public char getSymbol() {
        return this.symbol;
    }
}