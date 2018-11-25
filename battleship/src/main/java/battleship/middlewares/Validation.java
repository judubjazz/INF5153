package battleship.middlewares;

import battleship.Application;
import battleship.entities.BattleshipGame;
import battleship.entities.Board;
import java.util.Map;

public class Validation {
    public static boolean targetIsOutOfBound(Map<String, Integer> target, Board board){
        int x = target.get("x");
        int y = target.get("y");

        return x == board.width || y == board.height || x < 0 || y < 0;
    }

    public static boolean gameIDisInTheList(int id){
        for (BattleshipGame game: Application.gameList){
            System.out.println(game.id);
            if (game.id == id) return true;
        }
        return false;
    }
}
