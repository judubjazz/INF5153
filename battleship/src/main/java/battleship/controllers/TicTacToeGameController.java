package battleship.controllers;

import battleship.entities.Recorder;
import battleship.entities.games.TicTacToeGame;
import battleship.entities.games.Game;
import battleship.entities.games.TicTacToeGame;
import battleship.entities.players.TicTacToePlayer;
import battleship.entities.players.TicTacToePlayer;
import com.corundumstudio.socketio.SocketIOClient;
import net.sf.json.JSONObject;

public class TicTacToeGameController implements GameController<TicTacToeGame, TicTacToePlayer> {

    @Override
    public Game load(int gameID) {
        return null;
    }

    @Override
    public Game save(TicTacToeGame game) {
        return null;
    }

    @Override
    public boolean delete(int gameID) {
        return false;
    }

    @Override
    public Game play(TicTacToeGame game) {
        return null;
    }

    @Override
    public Game replay(TicTacToeGame game) {
        return null;
    }

    @Override
    public Game start(JSONObject data) {
        return null;
    }

    @Override
    public Game restart(TicTacToeGame game) {
        return null;
    }

    @Override
    public JSONObject createOnlineGame(SocketIOClient client, String req) {
        return null;
    }

    @Override
    public JSONObject joinOnlineGame(TicTacToeGame game, SocketIOClient client, String req) {
        return null;
    }

    @Override
    public JSONObject playTurnOnline(TicTacToePlayer p1, TicTacToePlayer p2, String req) {
        return null;
    }
}
