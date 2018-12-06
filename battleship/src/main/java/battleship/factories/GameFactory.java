package battleship.factories;

import battleship.entities.Recorder;
import battleship.entities.games.Game;
import net.sf.json.JSONObject;

public interface GameFactory {
    /**return a factory according to the game name*/
    static GameFactory getFactory(String key){
        if (key.equals("battleship")) return new BattleshipGameCreator();
        else if(key.equals("tictactoe")) return new TicTacToeGameCreator();
        return null;
    }

    /** initialise a game with json data */
    Game createGame(JSONObject data);

    /** builder for the game Recorder */
    Recorder buildRecorderFromJSONObject(JSONObject data);
}
