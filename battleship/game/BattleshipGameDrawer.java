package battleship.game;

import battleship.game.field.BattleshipField;
import battleship.game.field.Cell;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

class BattleshipGameDrawer {
    private final BattleshipField BATTLESHIP_FIELD;
    private final BattleshipGame BATTLESHIP_GAME;
    private final String BATTLEFIELD_HEADER;

    public BattleshipGameDrawer(BattleshipField battleShipField, BattleshipGame battleshipGame) {
        BATTLESHIP_FIELD = battleShipField;
        BATTLESHIP_GAME = battleshipGame;
        BATTLEFIELD_HEADER = IntStream.
                rangeClosed(1, BATTLESHIP_FIELD.getSize())
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(" "));
    }

    public String drawBattleshipGame() {
        var battlefieldStr = drawBattleshipField();
        BATTLESHIP_GAME.refreshState();
        return String.format("  %s%n%s%n", BATTLEFIELD_HEADER, battlefieldStr);
    }

    private String drawBattleshipField() {
        var battleshipFieldStreamStr = IntStream.range(0, BATTLESHIP_FIELD.getSize())
                .mapToObj(this::drawBattleshipFieldRow);

        if (BATTLESHIP_GAME.isThereFogOfWar() || BATTLESHIP_GAME.isReadyForStarting()) {
            battleshipFieldStreamStr = battleshipFieldStreamStr
                    .map(BattleshipGameDrawer::setFogOfWar);
        }

        return  battleshipFieldStreamStr.collect(Collectors.joining("\n"));
    }

    private static String setFogOfWar(String battleFieldRowStr) {
        return battleFieldRowStr.replace('O', '~');
    }

    private String drawBattleshipFieldRow(int numRow) {
        String battleshipFieldRow = BATTLESHIP_FIELD.rowCells(numRow)
                .map(Cell::toString)
                .collect(Collectors.joining(" "));
        return String.format("%c %s", 'A' + numRow, battleshipFieldRow);
    }

}
