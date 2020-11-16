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
                case P1_SETUP:
                    runP1Setup();
                    newState = GAME.getNewState();
                    break;
                case P2_SETUP:
                    runEnterDialog();
                    runP2Setup();
                    newState = GAME.getNewState();
                    break;
                case P1_SHELLS:
                    runEnterDialog();
                    runP1Shells();
                    break;
                case P2_SHELLS:
                    runEnterDialog();
                    runP2Shells();
                    break;
                default:
                    break;
            }
        } while (newState != GameState.END);

    }

    private void runP2Shells() {
        System.out.println(GAME_DRAWER.drawGameScreen());
        System.out.println("Player 2, it's your turn:");
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

    private void runP1Shells() {
        System.out.println(GAME_DRAWER.drawGameScreen());
        System.out.println("Player 1, it's your turn:");
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

    private void runP2Setup() {
        System.out.println("Player 2, place your ships on the game field");
        System.out.println(GAME_DRAWER.drawFogOfWarScreen());
        for (var ship : GAME.getBattleShips()) {
            printShipMessage(ship);
            runAddShipToTheBattleField(ship);
            System.out.println(GAME_DRAWER.drawGameSetupScreen());
        }
    }

    private void runP1Setup() {
        System.out.println("Player 1, place your ships on the game field");
        System.out.println(GAME_DRAWER.drawFogOfWarScreen());
        for (var ship : GAME.getBattleShips()) {
            printShipMessage(ship);
            runAddShipToTheBattleField(ship);
            System.out.println(GAME_DRAWER.drawGameSetupScreen());
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

