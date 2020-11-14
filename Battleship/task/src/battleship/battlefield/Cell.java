package battleship.battlefield;

public enum Cell {
    FOG_OF_WAR('~');

    private char symbol;

    Cell(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return Character.toString(symbol);
    }
}

