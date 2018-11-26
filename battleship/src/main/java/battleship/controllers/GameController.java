package battleship.controllers;

import battleship.entities.games.Game;
import battleship.entities.players.Player;
import com.corundumstudio.socketio.SocketIOClient;
import net.sf.json.JSONObject;


public interface GameController<P extends Player, G extends Game> {
    Game load(int gameID);

    Game save(G game);

    boolean delete(int gameID);

    Game play(G game);

    Game replay(G game);

    Game start(String data);

    Game restart(G game);

    JSONObject createOnlineGame(SocketIOClient client, String req);

    JSONObject joinOnlineGame(G game, SocketIOClient client, String req);

    JSONObject playTurnOnline(P p1, P p2, String req);
}
