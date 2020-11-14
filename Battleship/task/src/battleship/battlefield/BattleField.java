package battleship.battlefield;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
                this.board[i][j] = Cell.FOG_OF_WAR;
            }
        }
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
