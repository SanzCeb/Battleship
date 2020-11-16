package battleship.game;


import battleship.game.field.BattleshipField;
import battleship.game.field.Cell;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BattleshipGameDrawer {
    private final BattleshipGame BATTLESHIP_GAME;
    private final String BATTLEFIELD_HEADER;
    public BattleshipGameDrawer(BattleshipGame battleshipGame) {

        BATTLESHIP_GAME = battleshipGame;
        BATTLEFIELD_HEADER = IntStream.
                rangeClosed(1, 10)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining(" "));
    }


    public String drawFogOfWarScreen() {
        return drawBattleField(drawFogOfWarRows());
    }

    private String drawFogOfWarRows() {
        return IntStream.rangeClosed(0, 9)
                .mapToObj(this::drawFogOfWarRow)
                .collect(Collectors.joining("\n"));
    }

    private String drawFogOfWarRow(int numRow) {
        return drawRow(numRow, " ~".repeat(10));
    }

    private String drawRow(int numRow, String cellsRow) {
        return String.format("%c %s", 'A' + numRow, cellsRow);
    }

    private String drawBattleField(String battleFieldRows) {
        return String.format("  %s%n%s%n", BATTLEFIELD_HEADER, battleFieldRows);
    }

    public String drawGameSetupScreen() {
        var activeBattlefield = BATTLESHIP_GAME.getActiveBattlefield();
        return drawBattleField(drawBattleshipRows(activeBattlefield));
    }

    private String drawBattleshipRows(BattleshipField activeBattleField) {
        return IntStream.rangeClosed(0, 9)
                .mapToObj(numRow -> drawBattleshipRow(numRow, activeBattleField))
                .collect(Collectors.joining("\n"));
    }

    private String drawBattleshipRow(int numRow, BattleshipField activeBattleField) {
        String battleshipFieldRow = activeBattleField.rowCells(numRow)
                .map(Cell::toString)
                .collect(Collectors.joining(" "));
        return drawRow(numRow, battleshipFieldRow);
    }

    public String drawGameScreen() {
        return String.format("%s%s%n%s%n",drawFogOfWarScreen(), drawBorder(), drawGameSetupScreen());
    }

    private static String drawBorder() {
        return "----------------------";
    }
}
