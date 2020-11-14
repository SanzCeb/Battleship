package battleship.ships;

public enum Ship {
    AIRCRAFT_CARRIER("Aircraft Carrier", 5);

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


}
