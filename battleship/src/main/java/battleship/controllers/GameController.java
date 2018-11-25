package battleship.controllers;

import battleship.entities.BattleshipGame;
import battleship.entities.Player;
import com.corundumstudio.socketio.SocketIOClient;
import net.sf.json.JSONObject;


public interface GameController {
    BattleshipGame load(int gameID);

    BattleshipGame save(BattleshipGame battleshipGame);

    boolean delete(int gameID);

    BattleshipGame play(BattleshipGame battleshipGame);

    BattleshipGame replay(BattleshipGame battleshipGame);

    BattleshipGame start(String data);

    BattleshipGame restart(BattleshipGame battleshipGame);

    JSONObject createOnlineGame(SocketIOClient client, String req);

    JSONObject joinOnlineGame(BattleshipGame battleshipGame, SocketIOClient client, String req);

    JSONObject playTurnOnline(Player p1, Player p2, String req);
}
