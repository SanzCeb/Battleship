package battleship.game.field;

public class Cell implements Comparable <Cell>{

    private CellState state;
    private final int row;
    private final int column;

    Cell(int row, int column) {
        this.state = CellState.FOG_OF_WAR;
        this.row = row;
        this.column = column;
    }

    @Override
    public String toString() {
        return state.toString();
    }


    @Override
    public int compareTo(Cell cell) {
        var distance = verticalDistance(cell);
        if (distance == 0) {
            distance += horizontalDistance(cell);
        }
        return distance;
    }

    public int horizontalDistance(Cell beginCell) {
        return (this.row - beginCell.row);
    }

    public int verticalDistance(Cell beginCell) {
        return (this.column - beginCell.column);
    }

    public boolean isFree() {
        return this.state == CellState.FOG_OF_WAR;
    }

    public void setOccupied() {
        this.state = CellState.OCCUPIED;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public boolean isAdjacent(Cell cell) {
        return isAdjacentSameRow(cell) || isAdjacentSameColumn(cell);
    }

    private boolean isAdjacentSameColumn(Cell cell) {
        return this.column == cell.column && Math.abs(horizontalDistance(cell)) == 1;
    }

    private boolean isAdjacentSameRow(Cell cell) {
        return this.row == cell.row && Math.abs(verticalDistance(cell)) == 1;
    }

    public boolean isAtSameRow(Cell than) {
        return horizontalDistance(than) == 0;
    }

    public boolean isAtSameColumn(Cell than) {
        return verticalDistance(than) == 0;
    }
}

