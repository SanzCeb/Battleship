package battleship.game.field;

import battleship.game.ships.Navy;
import battleship.game.ships.Ship;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class BattleshipField {
    private final Cell [][] BOARD;
    private int numOfBoats = 0;
    private final int MAX_NUM_OF_BOATS;
    private final Navy navy;

    public BattleshipField(int battlefieldRows, int maxNumOfBoats) {
        this.BOARD = new Cell[battlefieldRows][battlefieldRows];
        for (int i = 0; i < battlefieldRows; i++) {
            for (int j = 0; j < battlefieldRows; j++) {
                this.BOARD[i][j] = new Cell(i, j);
            }
        }
        MAX_NUM_OF_BOATS = maxNumOfBoats;
        navy = new Navy(this);
    }

    public int getSize() {
        return BOARD.length;
    }

    public Optional<Cell> findCell(int row, int column) {
        try {
            return Optional.of(BOARD[row][column]);
        }catch (IndexOutOfBoundsException ex) {
            return Optional.empty();
        }
    }

    public void takeCellRange(Cell beginCell, Cell endCell) {
        cellRange(beginCell, endCell).forEach(Cell::setOccupied);
        navy.addNewShip(beginCell, endCell);
        numOfBoats++;
    }

    public Stream<Cell> rowCells(int numRow) {
        return Arrays.stream(BOARD[numRow]);
    }

    public boolean isFull() {
        return this.numOfBoats == MAX_NUM_OF_BOATS;
    }

    public boolean cellRangeDoesNotExist(Cell beginCell, Cell endCell) {
        return cellRange(beginCell, endCell).findAny().isEmpty();
    }

    public boolean shipDoesNotFit(Cell beginCell, Cell endCell, Ship ship) {
        return cellRange(beginCell, endCell).count() != ship.getSize();
    }

    public boolean isRangeCloseToAnotherShip(Cell beginCell, Cell endCell) {
        return isRangeOccupied(beginCell, endCell) || anyAdjacentCellOccupied(beginCell, endCell);
    }

    private boolean anyAdjacentCellOccupied(Cell beginCell, Cell endCell) {
        return adjacentCellsToRange(beginCell, endCell).anyMatch(Predicate.not(Cell::isFree));
    }

    private Stream<Cell> adjacentCellsToRange(Cell beginCell, Cell endCell) {
        return Arrays.stream(BOARD).flatMap(Arrays::stream)
                .filter(cell -> cellRange(beginCell, endCell).anyMatch(cell::isAdjacent));
    }

    private boolean isRangeOccupied(Cell beginCell, Cell endCell) {
        return cellRange(beginCell, endCell).anyMatch(Predicate.not(Cell::isFree));
    }


    private Stream<Cell> cellRange(Cell beginCell, Cell endCell) {
        var sortedCells = sortCellsIterator(beginCell, endCell);
        beginCell = sortedCells.next();
        endCell = sortedCells.next();
        return (beginCell.isAtSameColumn(endCell)) ? cellsColumnRange(beginCell, endCell) :
                (beginCell.isAtSameRow(endCell)) ? cellsRowRange(beginCell, endCell) : Stream.empty();
    }

    private Stream<Cell> cellsColumnRange(Cell beginCell, Cell endCell) {
        return Arrays.stream(BOARD).map(row -> row[beginCell.getColumn()])
                .filter( cell -> cell.compareTo(beginCell) >= 0 && cell.compareTo(endCell) <= 0);
    }

    private Stream<Cell> cellsRowRange(Cell beginCell, Cell endCell) {
        return Arrays.stream(BOARD[beginCell.getRow()])
                .filter( cell -> cell.compareTo(beginCell) >= 0 && cell.compareTo(endCell) <= 0);
    }

    private Iterator<Cell> sortCellsIterator(Cell beginCell, Cell endCell) {
        return Arrays.stream(new Cell[]{beginCell, endCell}).sorted().iterator();
    }

    public boolean shipSunk(Cell target) {
        Ship targetedShip = navy.getTargetedShip(target);
        var shipSunk = targetedShip == null;
        if (!shipSunk) {
            Cell stern = navy.getStern(targetedShip);
            Cell bow = navy.getBow(targetedShip);
            shipSunk = cellRange(stern, bow).allMatch(Cell::isHit);
            if (shipSunk) {
                removeShip(targetedShip);
            }

        }
        return shipSunk;
    }

    private void removeShip(Ship targetedShip) {
        var clazz = navy.getClass();
        for (var field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.getName().contains("ships")){
                try {
                    field.setAccessible(true);
                    var fieldClass = (Map<Ship, Cell>) field.get(navy);
                    fieldClass.remove(targetedShip);
                    numOfBoats = fieldClass.size();
                } catch (IllegalAccessException ignored) {

                }
            }
        }
    }
}
