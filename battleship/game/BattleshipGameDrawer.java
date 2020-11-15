package battleship.game;

import battleship.game.field.BattleshipField;
import battleship.game.field.Cell;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

class BattleshipGameDrawer {
    private final BattleshipField BATTLESHIP_FIELD;
    private final String BATTLEFIELD_HEADER;
    private boolean boatsHidden;

    public BattleshipGameDrawer(BattleshipField battleShipField) {
        BATTLESHIP_FIELD = battleShipField;
        BATTLEFIELD_HEADER = IntStream.
                rangeClosed(1, BATTLESHIP_FIELD.getSize())
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(" "));
    }

    public String drawBattleshipGame() {
        var battleFieldStr = drawBattleshipField();
        if (boatsHidden) {
            battleFieldStr = battleFieldStr.replace('O', '~');
        }
        return String.format("  %s%n%s%n", BATTLEFIELD_HEADER, battleFieldStr);
    }

    private String drawBattleshipField() {
        return IntStream.range(0, BATTLESHIP_FIELD.getSize())
                .mapToObj(this::drawBattleshipFieldRow)
                .collect(Collectors.joining("\n"));
    }

    private String drawBattleshipFieldRow(int numRow) {
        String battleshipFieldRow = BATTLESHIP_FIELD.rowCells(numRow)
                .map(Cell::toString)
                .collect(Collectors.joining(" "));
        return String.format("%c %s", 'A' + numRow, battleshipFieldRow);
    }


    public void setBoatsHidden(boolean boatsHidden) {
        this.boatsHidden = boatsHidden;
    }
}
