package battleship;

import java.io.UnsupportedEncodingException;
import battleship.controllers.BattleShipGameController;
import battleship.controllers.GameController;
import battleship.entities.BattleshipGame;
import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import io.socket.client.Socket;
import net.sf.json.JSONObject;


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

    public static void startServer(){
        if(thread == null){
            thread = new Thread(SocketCS::server);
            thread.start();
        }
    }

    private static void server(){
        server = getServer();
        GameController controller = new BattleShipGameController();

        server.addEventListener("playerWillCreateGame", String.class, (client, req, ackRequest) -> {
            JSONObject res = controller.createOnlineGame(client, req);
            client.sendEvent("playerDidCreateGame", res);
        });

        server.addEventListener("playerWillJoinGame", String.class, (client, req, ackRequest) -> {
            JSONObject res = JSONObject.fromObject(req);
            int gameID = res.getInt("id");

            BattleshipGame game = Application.gameList.get(gameID - 1);
            res = controller.joinOnlineGame(game, client, req);

            client.sendEvent("playerDidJoinGame", res);
            game.p1Socket.sendEvent("playerDidJoinGame");
        });

        server.addEventListener("playerOneWillPlayTurn", String.class, (client, req, ackRequest) -> {
            JSONObject res = JSONObject.fromObject(req);
            int gameID = res.getInt("id");
            BattleshipGame game = Application.gameList.get(gameID - 1);
            res = controller.playTurnOnline(game.playerOne, game.playerTwo, req);

            client.sendEvent("playerOneDidPlay", res);
            game.p2Socket.sendEvent("playerOneDidPlay", res);
        });

        server.addEventListener("playerTwoWillPlayTurn", String.class, (client, req, ackRequest) -> {
            JSONObject res = JSONObject.fromObject(req);
            int gameID = res.getInt("id");
            BattleshipGame game = Application.gameList.get(gameID - 1);
            res = controller.playTurnOnline(game.playerTwo, game.playerOne, req);

            game.p1Socket.sendEvent("playerTwoDidPlay", res);
            client.sendEvent("playerTwoDidPlay", res);
        });
        server.start();
//        Thread.sleep(10000);
//        server.stop();
    }
}
