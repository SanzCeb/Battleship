package battleship.game.ships;

public enum Ship {
    AIRCRAFT_CARRIER("Aircraft Carrier", 5),
    BATTLESHIP("Battleship", 4),
    CRUISER("Cruiser", 3),
    SUBMARINE("Submarine", 3),
    DESTROYER("Destroyer", 2);

    private final String name;
    private final int size;

    Ship(String name, int size) {
        this.name = name;
        this.size = size;
    }

    @Override
    public String toString() {
        return String.format("%s (%d cells)", name, size);
    }

    public String getShipName() {
        return name;
    }

    public int getSize() {
        return size;
    }
}
