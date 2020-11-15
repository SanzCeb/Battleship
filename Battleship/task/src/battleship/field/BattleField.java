package battleship.field;

import battleship.ships.Ship;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class BattleField {
    private final static int BATTLEFIELD_ROWS = 10;
    private final static String BATTLEFIELD_HEADER;
    private final Cell [][] board = new Cell[BATTLEFIELD_ROWS][BATTLEFIELD_ROWS];

    static {
        BATTLEFIELD_HEADER = IntStream.
                rangeClosed(1, BATTLEFIELD_ROWS)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(" "));
    }

    public BattleField() {
        for (int i = 0; i < BATTLEFIELD_ROWS; i++) {
            for (int j = 0; j < 10; j++) {
                this.board[i][j] = new Cell(i, j);
            }
        }
    }

    public void takePosition(String beginPosition, String endPosition, Ship ship) throws Exception {
        Cell beginCell;
        Cell endCell;

        try {
            beginCell = parseCell(beginPosition);
            endCell = parseCell(endPosition);
        }catch (IndexOutOfBoundsException | NumberFormatException exception) {
            throw new Exception("Error! Wrong ship location!");
        }
        var sortedCells = new Cell[] {beginCell, endCell};
        var xDistance = endCell.horizontalDistance(beginCell);
        var yDistance = endCell.verticalDistance(beginCell);

        Arrays.sort(sortedCells);
        beginCell = sortedCells[0];
        endCell = sortedCells[1];

        if (xDistance == 0) {
            if (Math.abs(yDistance) != ship.getSize() - 1) {
                throw new Exception(String.format("Error! Wrong length of the %s!", ship.getShipName()));
            }

            if (notCloseToAnotherShip(cellsRowRange(beginCell, endCell))) {
                cellsRowRange(beginCell, endCell).forEach(Cell::setOccupied);
            } else {
                throw new Exception("Error! You placed it too close to another one.");
            }

        } else if (yDistance == 0){
            if (Math.abs(xDistance) != ship.getSize() - 1) {
                throw new Exception(String.format("Error! Wrong length of the %s!", ship.getShipName()));
            }

            if (notCloseToAnotherShip(cellsColumnRange(beginCell, endCell))) {
                cellsColumnRange(beginCell, endCell)
                        .forEach(Cell::setOccupied);
            } else {
                throw new Exception("Error! You placed it too close to another one.");
            }

        } else {
            throw new Exception("Error! Wrong ship location!");
        }

    }

    private boolean notCloseToAnotherShip(Stream<Cell> newShipCellsStream) {
        var newShipCells = newShipCellsStream.collect(Collectors.toSet());
        return isCellRangeFree(Arrays.stream(board).flatMap(Arrays::stream)
                .filter(cell -> newShipCells.stream().anyMatch(cell::isAdjacent)));

    }

    private boolean isCellRangeFree(Stream<Cell> cellsRowRange) {
        return cellsRowRange.allMatch(Cell::isFree);
    }

    private Stream<Cell> cellsRowRange(Cell from, Cell to) {
        return  Arrays.stream(board[from.getRow()])
                .filter( cell -> cell.compareTo(from) >= 0 && cell.compareTo(to) <= 0);
    }

    private Stream<Cell> cellsColumnRange(Cell from, Cell to) {
        return Arrays.stream(board).map(row -> row[from.getColumn()])
                .filter( cell -> cell.compareTo(from) >= 0 && cell.compareTo(to) <= 0);
    }

    private Cell parseCell(String position) {
        var row = Character.toUpperCase(position.charAt(0)) - 'A';
        var column = Integer.parseInt(position.substring(1)) - 1;
        return board[row][column];
    }

    @Override
    public String toString() {
        var battleField = buildBattleFieldRows();

        return String.format("  %s%n%s%n", BATTLEFIELD_HEADER, battleField);
    }

    private String buildBattleFieldRows() {
        return IntStream.range(0, BATTLEFIELD_ROWS)
                .mapToObj(this::buildBattleFieldRow)
                .collect(Collectors.joining("\n"));
    }

    private String buildBattleFieldRow(int numRow) {
        var rowCells = Arrays.stream(board[numRow])
                .map(Cell::toString)
                .collect(Collectors.joining(" "));
        return String.format("%c %s", 'A' + numRow, rowCells);
    }
}
