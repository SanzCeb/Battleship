package battleship;

import battleship.field.BattleField;
import battleship.ships.Ship;

import java.util.Scanner;

public class Main {
    private final static Scanner SCANNER = new Scanner(System.in);
    private final static BattleField BATTLE_FIELD = new BattleField();

    public static void main(String[] args) {

        System.out.println(BATTLE_FIELD);
        placeShipOnTheBattleField(Ship.AIRCRAFT_CARRIER);
        System.out.println(BATTLE_FIELD);
        placeShipOnTheBattleField(Ship.BATTLESHIP);
        System.out.println(BATTLE_FIELD);
        placeShipOnTheBattleField(Ship.SUBMARINE);
        System.out.println(BATTLE_FIELD);
        placeShipOnTheBattleField(Ship.CRUISER);
        System.out.println(BATTLE_FIELD);
        placeShipOnTheBattleField(Ship.DESTROYER);
        System.out.println(BATTLE_FIELD);

    }

    private static void placeShipOnTheBattleField(Ship ship) {
        System.out.println("Enter the coordinates of the " + ship);
        while (true) {
            var coordinates = SCANNER.nextLine().split(" ");
            try {
                BATTLE_FIELD.takePosition(coordinates[0], coordinates[1], ship);
                break;
            } catch (Exception ex) {
                System.out.printf("%n%s Try again%n%n", ex.getMessage());
            }
        }
    }

}
