package battleship;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Map;

import battleship.entities.BattleshipGame;
import battleship.entities.Player;
import battleship.entities.ships.Ship;
import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import db.Db;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import battleship.entities.*;


public class SocketCS {
    static private Socket socket;
    static final int PORT = 9291;
    public static SocketIOServer server;

    public static void startServer() throws InterruptedException {
        Thread ts = new Thread(new Runnable() {
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
        ts.start();
    }

    public static void server() throws InterruptedException, UnsupportedEncodingException {
        // TODO do a singleton for the server
        Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(PORT);
        SocketConfig sockConfig = new SocketConfig();
        sockConfig.setReuseAddress(true);
        config.setSocketConfig(sockConfig);
        server = new SocketIOServer(config);

        server.addEventListener("createGame", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
                int id = Db.getDb().getMaxID();
                JSONObject json = JSONObject.fromObject(data);
                Player player = new Player("player1");
                BattleshipGame game = new BattleshipGame(id, player, null, null);
                game.p1Socket = client;
                Application.gameList.add(game);
                json.put("id", game.id);
                System.out.println(json.toString());
                client.sendEvent("toClient", json.toString());
            }
        });
//        server.addEventListener("join-game", String.class, new DataListener<String>() {
//            @Override
//            public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
//                JSONObject json = JSONObject.fromObject(data);
//                BattleshipGame game = Application.gameList.get(0);
//                game.p2Socket = client;
//                client.sendEvent("toClient", "message from server " + data);
//                client.sendEvent("toPlayer1", "message from server " + data);
//                client.sendEvent("toPlayer2", "message from server " + data);
//            }
//        });

        // TODO make the events Listen to gameIDs
        server.addEventListener("player1-turn", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
                client.sendEvent("toPlayer1", data);
                client.sendEvent("toPlayer2", data);
            }
        });
        server.addEventListener("player2-turn", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
                BattleshipGame game = Application.gameList.get(0);
                game.p1Socket.sendEvent("toPlayer1", data);
                client.sendEvent("toPlayer2", data);
            }
        });
        server.start();
//        Thread.sleep(10000);
//        server.stop();
    }
    public static void client() throws URISyntaxException, InterruptedException {
        socket = IO.socket("http://localhost:" + PORT);
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                socket.emit("toServer", "connected");
                socket.send("test");
            }
        });
        socket.on("toClient", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Client recievd : " + args[0]);

            }
        });
        socket.connect();
        while (!socket.connected())
            Thread.sleep(50);
        socket.send("another test");
        Thread.sleep(10000);
        socket.disconnect();
    }
}
