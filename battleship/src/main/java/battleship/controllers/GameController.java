package battleship.controllers;

import battleship.entities.games.Game;
import battleship.entities.players.Player;
import com.corundumstudio.socketio.SocketIOClient;
import net.sf.json.JSONObject;



public interface GameController<G extends Game, P extends Player> {

    /** get a controller according to the name of the game*/
    static GameController getController(String key){
        if (key.equals("battleship")) return new BattleShipGameController();
        else if(key.equals("tictactoe")) return new TicTacToeGameController();
        return null;
    }

    /** load a game from the database*/
    Game load(int gameID);

    /** save a game to the database*/
    Game save(G game);

    /** delete a game from the database*/
    boolean delete(int gameID);

    /** play a turn*/
    Game play(G game);

    /** replay the game*/
    Game replay(G game);

    /** start a game with json data*/
    Game start(JSONObject data);

    /** restart a game to his initiate value*/
    Game restart(G game);

    /** create an online game opening socket*/
    JSONObject createOnlineGame(SocketIOClient client, String req);

    /** join an active online game*/
    JSONObject joinOnlineGame(G game, SocketIOClient client, String req);

    /** analyse an online player turn*/
    JSONObject playTurnOnline(P p1, P p2, String req);
}
