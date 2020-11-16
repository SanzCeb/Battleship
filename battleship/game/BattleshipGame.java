package battleship.game;

import battleship.game.field.BattleshipField;
import battleship.game.field.Cell;
import battleship.game.ships.Ship;

import java.util.Arrays;
import java.util.Optional;

public class BattleshipGame {
    private final static int BATTLEFIELD_ROWS = 10;
    private static final int MAX_NUM_OF_BOATS = 5;
    private final BattleshipField BATTLESHIP_FIELD;
    private final BattleshipGameDrawer BATTLESHIP_FIELD_DRAWER;
    private GameState state = GameState.ADDING_SHIPS;

    public BattleshipGame() {
        BATTLESHIP_FIELD = new BattleshipField(BATTLEFIELD_ROWS, MAX_NUM_OF_BOATS);
        BATTLESHIP_FIELD_DRAWER = new BattleshipGameDrawer(BATTLESHIP_FIELD, this);
    }

    public void takePosition(String beginPosition, String endPosition, Ship ship) throws Exception {
        var shipCoordinates = parseShipCoordinates(beginPosition, endPosition);

        if (BATTLESHIP_FIELD.isFull()) {
            throw new Exception("Error! You cannot put more boats!");
        }

        if (BATTLESHIP_FIELD.shipDoesNotFit(shipCoordinates[0], shipCoordinates[1], ship)) {
            throw new Exception(String.format("Error! Wrong length of the %s!", ship.getShipName()));
        }

        if (BATTLESHIP_FIELD.isRangeCloseToAnotherShip(shipCoordinates[0], shipCoordinates[1])){
            throw new Exception("Error! You placed it too close to another one.");
        }

        BATTLESHIP_FIELD.takeCellRange(shipCoordinates[0], shipCoordinates[1]);
    }

    public String shot(String position) throws Exception {
        var cellPosition = parseCell(position);

        if (cellPosition.isEmpty()) {
            throw new  Exception("Error! You entered the wrong coordinates!");
        }

        return getShotOutcome(cellPosition.get());
    }

    private String getShotOutcome(Cell target) {
        String message;
        var shot = target.shot();
        if (shot && BATTLESHIP_FIELD.shipSunk(target)) {
            message = (isFinished()) ? "You sank the last ship. You won. Congratulations!" :
                    "You sank a ship! Specify a new target:";
        } else if (shot) {
            message = "You hit a ship! Try again:\n";
        } else {
            message = "You missed. Try again:\n";
        }
        return  message;
    }


    @Override
    public String toString() {
        return BATTLESHIP_FIELD_DRAWER.drawBattleshipGame();
    }

    boolean isThereFogOfWar() {
        return state == GameState.FOG_OF_WAR;
    }

    boolean isReadyForStarting() {
        return state == GameState.READY_FOR_STARTING;
    }

    void refreshState() {
        switch (state) {
            case FOG_OF_WAR:
            case SHIPS_VISIBLE:
            case READY_FOR_STARTING:
                state = GameState.FOG_OF_WAR;
                break;
            case ADDING_SHIPS:
                startGameIfReady();
                break;
        }
    }

    private Cell[] parseShipCoordinates(String beginPosition, String endPosition) throws Exception {
        var beginCellOpt = parseCell(beginPosition);
        var endCellOpt = parseCell(endPosition);

        if (beginCellOpt.isEmpty() || endCellOpt.isEmpty()
                || BATTLESHIP_FIELD.cellRangeDoesNotExist(beginCellOpt.get(), endCellOpt.get())) {
            throw new Exception("Error! Wrong ship location!");
        }
        return new Cell[] {beginCellOpt.get(), endCellOpt.get()};
    }

    private Optional<Cell> parseCell(String position){
        int row;
        int column;
        Optional<Cell> result;
        try {
            row = Character.toUpperCase(position.charAt(0)) - 'A';
            column = Integer.parseInt(position.substring(1)) - 1;
            result = BATTLESHIP_FIELD.findCell(row, column);
        }catch (NumberFormatException | NullPointerException exception ) {
            result = Optional.empty();
        }
        return result;
    }


    private void startGameIfReady() {
        if (BATTLESHIP_FIELD.isFull()) {
            state = GameState.READY_FOR_STARTING;
        }
    }

    public boolean isFinished() {
        return Arrays.stream(BATTLESHIP_FIELD.getClass().getDeclaredFields())
                .filter(field -> field.getName().equals("numOfBoats"))
                .mapToInt(field -> {field.setAccessible(true);
                    try {
                        return field.getInt(BATTLESHIP_FIELD);
                    } catch (IllegalAccessException ignored) {
                        return -1;
                    }
                })
                .allMatch(numOfBoats -> numOfBoats == 0);
    }
}
