package battleship.middlewares;

import battleship.entities.Board;

import java.util.Map;

public class Validation {
    public static boolean targetIsOutOfBound(Map<String, Integer> target, Board board){
        int x = target.get("x");
        int y = target.get("y");

        if(x == board.width || y == board.height || x < 0 || y < 0 ) return true;
        return false;
    }
}
