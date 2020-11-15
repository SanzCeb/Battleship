package battleship.game;

import battleship.game.field.BattleshipField;
import battleship.game.field.Cell;
import battleship.game.ships.Ship;

import java.io.IOException;
import java.util.Optional;

public class BattleshipGame {
    private final static int BATTLEFIELD_ROWS = 10;
    private final BattleshipField BATTLESHIP_FIELD = new BattleshipField(BATTLEFIELD_ROWS);
    private final BattleshipGameDrawer BATTLESHIP_FIELD_DRAWER = new BattleshipGameDrawer(BATTLESHIP_FIELD);


    public BattleshipGame() {

    }

    public void takePosition(String beginPosition, String endPosition, Ship ship) throws Exception {
        var shipCoordinates = parseShipCoordinates(beginPosition, endPosition);

        if (BATTLESHIP_FIELD.shipDoesNotFit(shipCoordinates[0], shipCoordinates[1], ship)) {
            throw new Exception(String.format("Error! Wrong length of the %s!", ship.getShipName()));
        }

        if (BATTLESHIP_FIELD.isRangeCloseToAnotherShip(shipCoordinates[0], shipCoordinates[1])){
            throw new Exception("Error! You placed it too close to another one.");
        }

         BATTLESHIP_FIELD.takeCellRange(shipCoordinates[0], shipCoordinates[1]);

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

    @Override
    public String toString() {
        return BATTLESHIP_FIELD_DRAWER.drawBattleshipGame();
    }


    public String shot(String position) throws Exception {
        var cellPosition = parseCell(position);
        if (cellPosition.isEmpty()) {
            throw new  Exception("Error! You entered the wrong coordinates!");
        }
        return (cellPosition.get().shot()) ? "You hit a ship\n" : "You missed!\n";

    }

    public void hideBoats() {
        BATTLESHIP_FIELD_DRAWER.setBoatsHidden(true);
    }

    public void unhideBoats() {
        BATTLESHIP_FIELD_DRAWER.setBoatsHidden(false);
    }
}
