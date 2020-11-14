package battleship;

import battleship.field.BattleField;
import battleship.ships.Ship;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        var battleField = new BattleField();
        var scanner = new Scanner(System.in);

        System.out.println(battleField);
        System.out.println("Enter the coordinates of the " + Ship.AIRCRAFT_CARRIER);

    }
}
