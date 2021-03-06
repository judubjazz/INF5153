package battleship.middlewares;

import battleship.Application;
import battleship.entities.games.Game;
import battleship.entities.boards.Board;
import java.util.Map;

public class Validation {
    public static boolean targetIsOutOfBound(Map<String, Integer> target, Board board){
        int x = target.get("x");
        int y = target.get("y");

        return x == board.width || y == board.height || x < 0 || y < 0;
    }

    public static boolean gameIDisInTheList(int id){
        for (Game game: Application.gameListVsHuman){
            if (game.id == id) return true;
        }
        return false;
    }
}
