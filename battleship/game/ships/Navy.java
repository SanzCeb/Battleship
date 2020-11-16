package battleship.game.ships;

import battleship.game.field.BattleshipField;
import battleship.game.field.Cell;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class Navy {
    private final BattleshipField BATTLESHIP_FIELD;
    private final Map<Ship, Cell> shipsStern;
    private final Map<Ship, Cell> shipsBow;

    public Navy(BattleshipField battleshipField){
        shipsBow = new HashMap<>();
        shipsStern = new HashMap<>();
        BATTLESHIP_FIELD = battleshipField;
    }

    public void addNewShip(Cell stern, Cell bow) {
        var shipClass = getShipClass(bow, stern);
        shipsStern.put(shipClass, stern);
        shipsBow.put(shipClass, bow);
    }

    private Ship getShipClass(Cell beginCell, Cell endCell) {
        Ship shipClass = null;
        for(var ship : Ship.values()) {
            try {
                var clazz = ship.getClass();
                var sizeField = clazz.getDeclaredField("size");
                sizeField.setAccessible(true);
                var sizeValue = (int) sizeField.get(ship);
                var shipLength = Math.abs(endCell.compareTo(beginCell)) + 1;

                var nameField = clazz.getDeclaredField("name");
                nameField.setAccessible(true);
                var nameValue = nameField.get(ship).toString();
                var shipNotExists = shipsBow.keySet().stream()
                        .noneMatch(s -> s.getShipName().equals(nameValue));
                if ( sizeValue == shipLength && shipNotExists) {
                    shipClass = ship;
                    break;
                }
            } catch (NoSuchFieldException | IllegalAccessException ignored) {

            }
        }
        return shipClass;
    }


    public Ship getTargetedShip(Cell target) {
        return shipsStern.keySet().stream()
                .filter(ship -> isCellOccupiedBy(ship, target))
                .findFirst()
                .orElse(null);
    }

    private boolean isCellOccupiedBy(Ship ship, Cell target) {
        var declaredMethods = Arrays.stream(BATTLESHIP_FIELD.getClass().getDeclaredMethods());
        var privateMethods = declaredMethods.filter(method -> Modifier.isPrivate(method.getModifiers()));
        var cellRangeMethod = privateMethods.filter(method -> method.getName().equals("cellRange")).findFirst();
        var cellRange = cellRangeMethod.map(method -> tryGetShipCellsFromMethod(method, ship));
        return cellRange.map(cells -> cells.anyMatch(cell -> cell.compareTo(target) == 0)).orElse(false);
    }

    public Cell getStern(Ship targetedShip) {
        return shipsStern.get(targetedShip);
    }

    public Cell getBow(Ship targetedShip) {
        return shipsBow.get(targetedShip);
    }

    private Stream<Cell> tryGetShipCellsFromMethod(Method method, Ship ship) {
        var stern = getStern(ship);
        var bow = getBow(ship);
        try {
            method.setAccessible(true);
            var cellsRange = (Stream<Cell>)method.invoke(BATTLESHIP_FIELD, stern, bow);
            method.setAccessible(false);
            return cellsRange;
        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }
        return null;
    }
}
