package battleship.controllers;

import battleship.entities.Grid;
import battleship.entities.ships.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Cpu {
    // TODO refactor this in two or three different functions
    public static Map<String, Ship> generateFleet(Grid grid) {
        Map<String, Ship> fleet = new HashMap<>();
        Ship carrier = new Carrier();
        Ship battleship = new Battleship();
        Ship cruiser = new Cruiser();
        Ship destroyer = new Destroyer();
        Ship submarine = new Submarine();

        carrier = carrier.generateRandomPosition(grid);
        grid.locateShip(carrier);
        battleship = battleship.generateRandomPosition(grid);
        grid.locateShip(battleship);
        cruiser = cruiser.generateRandomPosition(grid);
        grid.locateShip(cruiser);
        destroyer = destroyer.generateRandomPosition(grid);
        grid.locateShip(destroyer);
        submarine = submarine.generateRandomPosition(grid);
        grid.locateShip(submarine);
        fleet.put("carrier", carrier);
        fleet.put("battleship", battleship);
        fleet.put("cruiser", cruiser);
        fleet.put("destroyer", destroyer);
        fleet.put("submarine", submarine);
        return fleet;
    }

    public static Map<String, Integer> targetRandomPosition(Grid grid){
        Random r = new Random();
        Map<String, Integer> map = new HashMap<>();
        int x = r.nextInt(grid.width - 1);
        int y = r.nextInt(grid.height - 1);
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
