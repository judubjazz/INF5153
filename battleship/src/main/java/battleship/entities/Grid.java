package battleship.entities;


import battleship.entities.ships.Ship;
import java.util.Map;

public class Grid {
    public int height = 10;
    public int width = 10;
    public boolean hidden;
    public int[][] map;
//    public ArrayList [][] map;

    public Grid() {
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
//        this.map = new ArrayList[10][10];
//        for(int i = 0; i< 9; ++i){
//            this.map[i][0] = new ArrayList();
//        }
        this.hidden = false;
    }
    public Grid(boolean hidden) {
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
//        this.map = new ArrayList[10][10];
    }

//    public Grid(int [][] map) {
//        this.map = map;
//        this.hidden = false;
//    }
//    public Grid(ArrayList [][] map) {
//        this.map = map;
//        this.hidden = false;
//    }

    public Grid generateRandomGrid() {
        return this;
    }

    public void locateFleet(Map<String, Ship> fleet) {
        for (Ship ship : fleet.values()) {
           locateShip(ship);
        }
    }

    public void locateShip(Ship ship) {
        int stemX = ship.getStemX();
        int stemY = ship.getStemY();
        int bowX = ship.getBowX();
        int bowY = ship.getBowY();
        int shipID = ship.getId();
        int shipSize = ship.getSize();

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

    public static int [][] Stringto2DArray(String source) {
        String[] data = source.split(",");
        int[][] map = new int[10][10];
        for(int i=0; i < 10; ++i){
            for(int j = 0; j < 10; ++j){
                map[i][j] = Integer.parseInt(data[(j) + (i*10)]);
            }
        }
        return map;
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
