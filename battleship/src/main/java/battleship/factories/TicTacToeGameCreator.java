package battleship.factories;

import battleship.entities.Recorder;
import battleship.entities.games.Game;
import net.sf.json.JSONObject;

public class TicTacToeGameCreator implements GameFactory {
    @Override
    public Game createGame(JSONObject data) {
        return null;
    }

    @Override
    public Recorder buildRecorderFromJSONObject(JSONObject data) {
        return null;
    }
}
