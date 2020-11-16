package battleship.game;

public enum ShellState {
    MISSED("You missed."),
    HIT("You hit a ship!"),
    SUNK("You sank a ship!");

    private final String outcome;

    ShellState(String outcome) {
        this.outcome = outcome;
    }


    @Override
    public String toString() {
        return outcome;
    }
}
