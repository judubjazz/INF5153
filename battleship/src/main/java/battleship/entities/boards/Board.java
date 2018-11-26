package battleship.entities.boards;

import battleship.entities.ships.Ship;
import java.util.Map;

public class Board {
    public int height;
    public int width;
    public int[][] map;

    public Board() {}

    public Board(int height, int width, int[][] map){
        this.height = height;
        this.width = width;
        this.map = map;
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
