package battleship.entities.boards;

import battleship.entities.ships.*;

import java.util.HashMap;
import java.util.Map;

public class BattleshipBoard extends Board {
    public boolean hidden;

    public BattleshipBoard() {
        super(10, 10, new int[][]{
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        });
        this.hidden = false;
    }

    public BattleshipBoard(int height, int width, int[][] map) {
        super(height, width, map);
        this.hidden = false;
    }

    public void locateFleet(Map<String, Ship> fleet) {
        for (Ship ship : fleet.values()) {
            locateShip(ship);
        }
    }

    public void locateShip(Ship ship) {
        int stemX = ship.stemX;
        int stemY = ship.stemY;
        int bowX = ship.bowX;
        int shipID = ship.id;
        int shipSize = ship.size;

        for(int i = 0; i < shipSize; ++i) {
            if(stemX != bowX) {
                // horizontal
                this.map[stemX][stemY] = shipID;
                ++stemX;
            } else {
                // vertical
                this.map[stemX][stemY] = shipID;
                ++stemY;
            }
        }
    }

    public boolean validateRow(Ship ship){
        int stemX = ship.getStemX();
        int stemY = ship.getStemY();

        for(int i = 0; i < ship.getSize(); ++i) {
            if(this.map[stemX][stemY] != 0) return false;
            ++stemX;
            if(stemX > this.width - 1) return false;
        }
        return true;
    }

    public boolean validateCol(Ship ship){
        int stemX = ship.getStemX();
        int stemY = ship.getStemY();

        for(int i = 0; i < ship.getSize(); ++i) {
            if(this.map[stemX][stemY] != 0) return false;
            ++stemY;
            if(stemY > this.height - 1) return false;
        }
        return true;
    }


    // TODO refactor this in two or three different functions

    /**
     * Initiate all 5 differents ship, then localise them on the board
     * If a ships overlaps another one or is out of bounds, it will relocalise it
     * @param board
     * @return a map with the ship's name as keys
     */
    public static Map<String, Ship> generateFleet(BattleshipBoard board) {
        Map<String, Ship> fleet = new HashMap<>();
        Ship carrier = new Carrier();
        Ship battleship = new Battleship();
        Ship cruiser = new Cruiser();
        Ship destroyer = new Destroyer();
        Ship submarine = new Submarine();

        carrier = carrier.locateToRandomPosition(board);
        board.locateShip(carrier);
        battleship = battleship.locateToRandomPosition(board);
        board.locateShip(battleship);
        cruiser = cruiser.locateToRandomPosition(board);
        board.locateShip(cruiser);
        destroyer = destroyer.locateToRandomPosition(board);
        board.locateShip(destroyer);
        submarine = submarine.locateToRandomPosition(board);
        board.locateShip(submarine);
        fleet.put("carrier", carrier);
        fleet.put("battleship", battleship);
        fleet.put("cruiser", cruiser);
        fleet.put("destroyer", destroyer);
        fleet.put("submarine", submarine);
        return fleet;
    }


    // Getters & Setters

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    @Override
    public String toString() {
        String s="";
        for(int i = 0; i< 9; ++i){
            for(int j = 0; j<9; ++j){
                s = s.concat(String.valueOf(this.map[i][j]));
            }
        }
        return s;
    }
}
