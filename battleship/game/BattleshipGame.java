package battleship.game;

import battleship.game.field.BattleshipField;
import battleship.game.field.Cell;
import battleship.game.ships.Ship;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Optional;

public class BattleshipGame {
    private final static int BATTLEFIELD_ROWS = 10;
    private static final int MAX_NUM_OF_BOATS = 5;
    private final BattleshipField BATTLESHIP_FIELD;
    private final BattleshipField BATTLESHIP_FIELD_2;
    private GameState state = GameState.P1_SETUP;

    public BattleshipGame() {
        BATTLESHIP_FIELD = new BattleshipField(BATTLEFIELD_ROWS, MAX_NUM_OF_BOATS);
        BATTLESHIP_FIELD_2 = new BattleshipField(BATTLEFIELD_ROWS, MAX_NUM_OF_BOATS);
    }

    public void addShipToTheBattleField(String beginPosition, String endPosition, Ship ship) throws Exception {
        var activeBattleField = getActiveBattlefield();
        var shipCoordinates = parseShipCoordinates(beginPosition, endPosition);

        if (activeBattleField.isFull()) {
            throw new Exception("Error! You cannot put more boats!");
        }

        if (activeBattleField.shipDoesNotFit(shipCoordinates[0], shipCoordinates[1], ship)) {
            throw new Exception(String.format("Error! Wrong length of the %s!", ship.getShipName()));
        }

        if (activeBattleField.isRangeCloseToAnotherShip(shipCoordinates[0], shipCoordinates[1])){
            throw new Exception("Error! You placed it too close to another one.");
        }

        activeBattleField.takeCellRange(shipCoordinates[0], shipCoordinates[1]);
    }

    public ShellState shell(String position) throws Exception {
        var cellPosition = parseCell(position, getInactiveBattlefield());

        if (cellPosition.isEmpty()) {
            throw new  Exception("Error! You entered the wrong coordinates!");
        }

        return getShotOutcome(cellPosition.get());
    }

    public GameState getNewState() {
        switch (state){
            case P1_SETUP:
                if (BATTLESHIP_FIELD.isFull()) {
                    state = GameState.P2_SETUP;
                }
                break;
            case P2_SETUP:
                if (BATTLESHIP_FIELD_2.isFull()) {
                    state = GameState.P1_SHELLS;
                }
                break;
            case P1_SHELLS:
                state = (isFinished()) ? GameState.END : GameState.P2_SHELLS;
                break;
            case P2_SHELLS:
                state = (isFinished()) ? GameState.END : GameState.P1_SHELLS;
                break;
            case END:
            default:
                break;
        }
        return state;
    }


    public Ship[] getBattleShips() {
        return Ship.values();
    }

    public BattleshipField getActiveBattlefield() {
        return (state == GameState.P1_SETUP || state == GameState.P1_SHELLS) ? BATTLESHIP_FIELD : BATTLESHIP_FIELD_2;
    }

    public String getActivePlayer() {
        return (state == GameState.P1_SETUP || state == GameState.P1_SHELLS) ? "Player 1" : "Player 2";
    }

    private ShellState getShotOutcome(Cell target) {
        ShellState outcome;
        var shot = target.shot();
        if (shot && getInactiveBattlefield().shipSunk(target)) {
            outcome = ShellState.SUNK;
        } else if (shot) {
            outcome = ShellState.HIT;
        } else {
            outcome = ShellState.MISSED;
        }
        return  outcome;
    }


    private Cell[] parseShipCoordinates(String beginPosition, String endPosition) throws Exception {
        var beginCellOpt = parseCell(beginPosition, getActiveBattlefield());
        var endCellOpt = parseCell(endPosition, getActiveBattlefield());

        if (beginCellOpt.isEmpty() || endCellOpt.isEmpty()
                || getActiveBattlefield().cellRangeDoesNotExist(beginCellOpt.get(), endCellOpt.get())) {
            throw new Exception("Error! Wrong ship location!");
        }
        return new Cell[] {beginCellOpt.get(), endCellOpt.get()};
    }

    private Optional<Cell> parseCell(String position, BattleshipField battleshipField){
        int row;
        int column;
        Optional<Cell> result;
        try {
            row = Character.toUpperCase(position.charAt(0)) - 'A';
            column = Integer.parseInt(position.substring(1)) - 1;
            result = battleshipField.findCell(row, column);
        }catch (NumberFormatException | NullPointerException exception ) {
            result = Optional.empty();
        }
        return result;
    }

    private boolean isFinished() {
        return Arrays.stream(getInactiveBattlefield().getClass().getDeclaredFields())
                .filter(field -> field.getName().equals("numOfBoats"))
                .mapToInt(this::getNumOfBoats)
                .allMatch(numOfBoats -> numOfBoats == 0);
    }

    private int getNumOfBoats(Field field) {
        try {
            field.setAccessible(true);
            return field.getInt(getInactiveBattlefield());
        } catch (IllegalAccessException ignored) {
            return -1;
        }
    }

    private BattleshipField getInactiveBattlefield() {
        return (BATTLESHIP_FIELD == getActiveBattlefield()) ? BATTLESHIP_FIELD_2 : BATTLESHIP_FIELD;
    }
}
