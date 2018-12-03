package battleship.controllers;

import battleship.entities.games.Game;
import battleship.entities.players.Player;
import com.corundumstudio.socketio.SocketIOClient;
import net.sf.json.JSONObject;


public interface GameController<G extends Game, P extends Player> {

    static GameController getController(String key){
        if (key.equals("battleship")) return new BattleShipGameController();
        else if(key.equals("tictactoe")) return new TicTacToeGameController();
        return null;
    }

    Game load(int gameID);

    Game save(G game);

    boolean delete(int gameID);

    Game play(G game);

    Game replay(G game);

    Game start(JSONObject data);

    Game restart(G game);

    JSONObject createOnlineGame(SocketIOClient client, String req);

    JSONObject joinOnlineGame(G game, SocketIOClient client, String req);

    JSONObject playTurnOnline(P p1, P p2, String req);
}
