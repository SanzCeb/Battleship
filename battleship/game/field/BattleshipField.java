package battleship.game.field;

import battleship.game.ships.Ship;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class BattleshipField {
    private final Cell [][] board;

    public BattleshipField(int battlefieldRows) {
        this.board = new Cell[battlefieldRows][battlefieldRows];
        for (int i = 0; i < battlefieldRows; i++) {
            for (int j = 0; j < battlefieldRows; j++) {
                this.board[i][j] = new Cell(i, j);
            }
        }
    }

    public int getSize() {
        return board.length;
    }

    public Optional<Cell> findCell(int row, int column) {
        try {
            return Optional.of(board[row][column]);
        }catch (IndexOutOfBoundsException ex) {
            return Optional.empty();
        }
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
        return Arrays.stream(board).flatMap(Arrays::stream)
                .filter(cell -> cellRange(beginCell, endCell).anyMatch(cell::isAdjacent));
    }

    private boolean isRangeOccupied(Cell beginCell, Cell endCell) {
        return cellRange(beginCell, endCell).anyMatch(Predicate.not(Cell::isFree));
    }


    public void takeCellRange(Cell beginCell, Cell endCell) {
        cellRange(beginCell, endCell).forEach(Cell::setOccupied);
    }

    private Stream<Cell> cellRange(Cell beginCell, Cell endCell) {
        var sortedCells = sortCellsIterator(beginCell, endCell);
        beginCell = sortedCells.next();
        endCell = sortedCells.next();
        return (beginCell.isAtSameColumn(endCell)) ? cellsColumnRange(beginCell, endCell) :
                (beginCell.isAtSameRow(endCell)) ? cellsRowRange(beginCell, endCell) : Stream.empty();
    }

    private Stream<Cell> cellsColumnRange(Cell beginCell, Cell endCell) {
        return Arrays.stream(board).map(row -> row[beginCell.getColumn()])
                .filter( cell -> cell.compareTo(beginCell) >= 0 && cell.compareTo(endCell) <= 0);
    }

    private Stream<Cell> cellsRowRange(Cell beginCell, Cell endCell) {
        return Arrays.stream(board[beginCell.getRow()])
                .filter( cell -> cell.compareTo(beginCell) >= 0 && cell.compareTo(endCell) <= 0);
    }

    private Iterator<Cell> sortCellsIterator(Cell beginCell, Cell endCell) {
        return Arrays.stream(new Cell[]{beginCell, endCell}).sorted().iterator();
    }

    public Stream<Cell> rowCells(int numRow) {
        return Arrays.stream(board[numRow]);
    }

}
