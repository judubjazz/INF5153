package battleship.factories;

import battleship.entities.Recorder;
import battleship.entities.games.Game;
import net.sf.json.JSONObject;

public interface GameFactory {
    static GameFactory getFactory(String key){
        if (key.equals("battleship")) return new BattleshipGameCreator();
        else if(key.equals("tictactoe")) return new TicTacToeGameCreator();
        return null;
    }
    Game createGame(JSONObject data);

    Recorder buildRecorderFromJSONObject(JSONObject data);
}
