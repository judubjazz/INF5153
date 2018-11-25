package battleship.entities;


import battleship.entities.ships.Ship;

import java.util.Map;

public class Board {
    public int height = 10;
    public int width = 10;
    public boolean hidden;
    public int[][] map;

    public Board() {
        this.map = new int[][]{
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
        };
        this.hidden = false;
    }
    public Board(boolean hidden) {
        this.map = new int[][]{
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
        };
        this.hidden = hidden;
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

    // Getters & Setters
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    public boolean isHidden() {
        return hidden;
    }
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
    public int[][] getMap() {
        return map;
    }
    public void setMap(int[][] map) {
        this.map = map;
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
