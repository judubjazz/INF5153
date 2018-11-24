package battleship;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import battleship.controllers.BattleShipGameController;
import battleship.controllers.GameController;
import battleship.entities.BattleshipGame;
import battleship.entities.Player;
import battleship.entities.ships.Ship;
import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import db.Db;
import io.socket.client.Socket;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import battleship.entities.*;


public class SocketCS {
    static private Socket socket;
    private static final int PORT = 9291;
    private static SocketIOServer server = null;
    private static Thread thread = null;

    private SocketCS(){}

    private static SocketIOServer getServer(){
        if (server == null) {
            Configuration config = new Configuration();
            config.setHostname("127.0.0.1");
            config.setPort(PORT);
            SocketConfig sockConfig = new SocketConfig();
            sockConfig.setReuseAddress(true);
            config.setSocketConfig(sockConfig);
            server = new SocketIOServer(config);
        }
        return server;
    }

    public static void startServer() throws InterruptedException {
        if(thread == null){
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        server();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
    }

    private static void server() throws InterruptedException, UnsupportedEncodingException {
        server = getServer();
        GameController controller = new BattleShipGameController();

        server.addEventListener("playerWillCreateGame", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String req, AckRequest ackRequest) {
                JSONObject res = controller.createOnlineGame(client, req);
                client.sendEvent("playerDidCreateGame", res);
            }
        });

        server.addEventListener("playerWillJoinGame", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String req, AckRequest ackRequest) {

                JSONObject res = JSONObject.fromObject(req);
                int gameID = res.getInt("id");

                // TODO check for game ids has to be in gamelist
                BattleshipGame game = Application.gameList.get(0);
                res = controller.joinOnlineGame(game, client, req);

                client.sendEvent("playerDidJoinGame", res);
                game.p1Socket.sendEvent("playerDidJoinGame");
            }
        });

        // TODO load gameIDs with the the data sent
        // TODO possible to refactor next 2 functions in one
        server.addEventListener("playerOneWillPlayTurn", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String req, AckRequest ackRequest) {
                JSONObject res = JSONObject.fromObject(req);
                int gameID = res.getInt("id");
                BattleshipGame game = Application.gameList.get(0);
                res = controller.playTurn(game.playerOne, game.playerTwo, req);

                client.sendEvent("playerOneDidPlay", res);
                game.p2Socket.sendEvent("playerOneDidPlay", res);
            }
        });
        server.addEventListener("playerTwoWillPlayTurn", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String req, AckRequest ackRequest) {
                JSONObject res = JSONObject.fromObject(req);
                int gameID = res.getInt("id");
                BattleshipGame game = Application.gameList.get(0);
                res = controller.playTurn(game.playerTwo, game.playerOne, req);

                game.p1Socket.sendEvent("playerTwoDidPlay", res);
                client.sendEvent("playerTwoDidPlay", res);
            }
        });
        server.start();
//        Thread.sleep(10000);
//        server.stop();
    }
}
