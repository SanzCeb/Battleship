package battleship.game.field;

enum CellState {
    FOG_OF_WAR('~'),
    OCCUPIED('O'),
    HIT('X'),
    MISS('M');

    private final char symbol;

    CellState(char symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return Character.toString(symbol);
    }
}
