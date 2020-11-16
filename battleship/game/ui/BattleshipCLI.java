package battleship.game.ui;

import battleship.game.*;
import battleship.game.ships.Ship;

import java.util.Scanner;


public class BattleshipCLI {
    private final BattleshipGame GAME = new BattleshipGame();
    private final BattleshipGameDrawer GAME_DRAWER = new BattleshipGameDrawer(GAME);
    private final Scanner SYSTEM_IN_SCANNER = new Scanner(System.in);

    public void run() {
        var newState = GAME.getNewState();

        do {
            switch (newState) {
                case P2_SETUP:
                    runEnterDialog();
                case P1_SETUP:
                    runPlayerSetup();
                    newState = GAME.getNewState();
                    break;
                case P1_SHELLS:
                case P2_SHELLS:
                    runEnterDialog();
                    runPlayerShell();
                    break;
                default:
                    break;
            }
        } while (newState != GameState.END);

    }

    private void runPlayerShell() {
        System.out.println(GAME_DRAWER.drawGameScreen());
        System.out.println(GAME.getActivePlayer() + ", it's your turn:");
        while (true) {
            var coordinates = SYSTEM_IN_SCANNER.nextLine();
            try {
                var shellOutcome = GAME.shell(coordinates).toString();
                if (GAME.getNewState() == GameState.END) {
                    System.out.println("You sank the last ship. You won. Congratulations!");
                } else {
                    System.out.println(shellOutcome);
                }
                break;
            } catch (Exception ex) {
                System.out.printf("%n%s Try again%n%n", ex.getMessage());
            }
        }
    }

    private void runEnterDialog() {
        System.out.println("Press Enter and pass the move to another player");
        SYSTEM_IN_SCANNER.nextLine();
    }

    private void runPlayerSetup() {
        System.out.println(GAME.getActivePlayer() + ", place your ships on the game field");
        System.out.println(GAME_DRAWER.drawFogOfWarScreen());
        for (var ship : GAME.getBattleShips()) {
            printShipMessage(ship);
            runAddShipToTheBattleField(ship);
            System.out.println(GAME_DRAWER.drawPlayerScreen());
        }
    }

    private void printShipMessage(Ship ship) {
        System.out.printf("Enter the coordinates of the %s:%n", ship);
    }

    private void runAddShipToTheBattleField(Ship ship) {
        while (true) {
            var coordinates = SYSTEM_IN_SCANNER.nextLine().split(" ");
            try {
                GAME.addShipToTheBattleField(coordinates[0], coordinates[1], ship);
                break;
            } catch (Exception ex) {
                System.out.printf("%n%s Try again%n%n", ex.getMessage());
            }
        }
    }
}

