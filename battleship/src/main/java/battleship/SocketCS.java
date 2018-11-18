package battleship;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.Map;

import battleship.entities.BattleshipGame;
import battleship.entities.Player;
import battleship.entities.ships.Ship;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.DataListener;
import db.Db;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import net.sf.json.JSON;
import net.sf.json.JSONObject;


public class SocketCS {
    static private Socket socket;
    static final int PORT = 9291;
    static SocketIOServer server;

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
//        try {
//            client();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
    }
//    public static void main(String[] args) throws InterruptedException {
//        Thread ts = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    server();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//        ts.start();
//        try {
//            client();
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }
    public static void server() throws InterruptedException, UnsupportedEncodingException {
        Configuration config = new Configuration();
        config.setHostname("127.0.0.1");
        config.setPort(PORT);
        server = new SocketIOServer(config);
        server.addEventListener("createGame", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
                int id = Db.getDb().getMaxID();
                Player player = new Player("player1");
                JSONObject json = JSONObject.fromObject(data);
                Map<String, Ship> fleet = Ship.buildFleet(json);
                BattleshipGame game = new BattleshipGame(id, player, null, null);
                Db.getDb().save(game);
                Application.gameList.add(game);
                json.accumulate("game", game.id);
                client.sendEvent("toClient", json);
            }
        });
        server.addEventListener("message", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
                client.sendEvent("toClient", "message from server " + data);
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
