package battleship.controllers;

import battleship.entities.Board;
import battleship.entities.ships.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Cpu {
    // TODO refactor this in two or three different functions
    public static Map<String, Ship> generateFleet(Board board) {
        Map<String, Ship> fleet = new HashMap<>();
        Ship carrier = new Carrier();
        Ship battleship = new Battleship();
        Ship cruiser = new Cruiser();
        Ship destroyer = new Destroyer();
        Ship submarine = new Submarine();

        carrier = carrier.generateRandomPosition(board);
        board.locateShip(carrier);
        battleship = battleship.generateRandomPosition(board);
        board.locateShip(battleship);
        cruiser = cruiser.generateRandomPosition(board);
        board.locateShip(cruiser);
        destroyer = destroyer.generateRandomPosition(board);
        board.locateShip(destroyer);
        submarine = submarine.generateRandomPosition(board);
        board.locateShip(submarine);
        fleet.put("carrier", carrier);
        fleet.put("battleship", battleship);
        fleet.put("cruiser", cruiser);
        fleet.put("destroyer", destroyer);
        fleet.put("submarine", submarine);
        return fleet;
    }

    public static Map<String, Integer> targetRandomPosition(Board board){
        Random r = new Random();
        Map<String, Integer> map = new HashMap<>();
        int x = r.nextInt(board.width - 1);
        int y = r.nextInt(board.height - 1);
        map.put("x", x );
        map.put("y", y);
        return map;
    }

    public static Map<String, Integer> targetRandomPosition(){
        Random r = new Random();
        Map<String, Integer> map = new HashMap<>();
        int x = r.nextInt(10);
        int y = r.nextInt(10);
        map.put("x", x );
        map.put("y", y);
        return map;
    }
}
