package battleship.field;

public enum CellState {
    FOG_OF_WAR('~'),
    OCCUPIED('O'),
    HIT('X');

    private final char symbol;

    CellState(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return Character.toString(symbol);
    }
}
