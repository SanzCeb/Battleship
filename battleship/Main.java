package battleship;

import battleship.game.BattleshipGame;
import battleship.game.ships.Ship;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    private final static Scanner SCANNER = new Scanner(System.in);
    private final static BattleshipGame BATTLESHIP_GAME = new BattleshipGame();

    public static void main(String[] args) {

        System.out.println(BATTLESHIP_GAME);
        placeShipOnTheBattleField(Ship.AIRCRAFT_CARRIER);
        System.out.println(BATTLESHIP_GAME);
        placeShipOnTheBattleField(Ship.BATTLESHIP);
        System.out.println(BATTLESHIP_GAME);
        placeShipOnTheBattleField(Ship.SUBMARINE);
        System.out.println(BATTLESHIP_GAME);
        placeShipOnTheBattleField(Ship.CRUISER);
        System.out.println(BATTLESHIP_GAME);
        placeShipOnTheBattleField(Ship.DESTROYER);
        System.out.println(BATTLESHIP_GAME);
        takeAShot();
    }

    private static void takeAShot() {
        System.out.println("The game starts!");;
        System.out.println(BATTLESHIP_GAME);
        System.out.println("Take a shot!");
        while (true) {
            try {
                var coordinate = SCANNER.nextLine();
                var message = BATTLESHIP_GAME.shot(coordinate);
                System.out.println(BATTLESHIP_GAME);
                System.out.println(message);
                break;
            } catch (Exception ex) {
                System.out.printf("%n%s Try again%n%n", ex.getMessage());
            }
        }
    }

    private static void placeShipOnTheBattleField(Ship ship) {
        System.out.println("Enter the coordinates of the " + ship);
        while (true) {
            var coordinates = SCANNER.nextLine().split(" ");
            try {
                BATTLESHIP_GAME.takePosition(coordinates[0], coordinates[1], ship);
                break;
            } catch (Exception ex) {
                System.out.printf("%n%s Try again%n%n", ex.getMessage());
            }
        }
    }

}
