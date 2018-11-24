package battleship.controllers;

import battleship.entities.BattleshipGame;
import battleship.entities.Player;
import com.corundumstudio.socketio.SocketIOClient;
import net.sf.json.JSONObject;


public interface GameController {
    public BattleshipGame load(int gameID);
    public BattleshipGame save(BattleshipGame battleshipGame);
    public BattleshipGame play(BattleshipGame battleshipGame);
//    public BattleshipGame playTurn(BattleshipGame battleshipGame);
    public BattleshipGame replay(BattleshipGame battleshipGame);
    public BattleshipGame start(String data);
    public BattleshipGame restart(BattleshipGame battleshipGame);
    public JSONObject createOnlineGame(SocketIOClient client, String req);
    public JSONObject joinOnlineGame(BattleshipGame battleshipGame, SocketIOClient client, String req);
    public JSONObject playTurn(Player p1, Player p2, String req);
}
