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
    private enum PlayerID {P1, P2}

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

        server.addEventListener("creatingGame", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
                int id = Db.getDb().getMaxID();
                JSONObject json = JSONObject.fromObject(data);
                JSONObject p1FleetJSON = json.getJSONObject("fleet");
                Player player = new Player("player1");


                Map<String, Ship> playerOneFleet = Ship.buildFleet(p1FleetJSON);
                Board p1Board = new Board();
                p1Board.locateFleet(playerOneFleet);
                player.playerBoard = p1Board;

                BattleshipGame game = new BattleshipGame(id, player, null, null);
                json.put("id", game.id);
                json.put("map", p1Board.map);
                game.p1Socket = client;

                Application.gameList.add(game);
                System.out.println(json.toString());

                client.sendEvent("gameCreated", json.toString());
            }
        });

        server.addEventListener("joiningGame", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
                // TODO refactor this in one function and add validation
                BattleshipGame game = Application.gameList.get(0);
                JSONObject json = JSONObject.fromObject(data);
                Player player = new Player("player2");
                JSONObject p2FleetJSON = json.getJSONObject("fleet");
                Map<String, Ship> p2Fleet = Ship.buildFleet(p2FleetJSON);
                Board p2Board = new Board();
                p2Board.locateFleet(p2Fleet);

                json.put("map", p2Board.map);
                player.playerBoard = p2Board;
                game.playerTwo = player;
                game.p2Socket = client;

                game.p1Socket.sendEvent("hasJoinedGame", json.toString());
            }
        });

        // TODO make the events Listen to gameIDs
        server.addEventListener("playerOneIsPlaying", String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client, String data, AckRequest ackRequest) {
                BattleshipGame game = Application.gameList.get(0);
                client.sendEvent("toPlayer1", data);
                game.p2Socket.sendEvent("toPlayer2", data);
            }
        });
        server.addEventListener("playerTwoIsPlaying", String.class, new DataListener<String>() {
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

    private static SocketIOClient getPlayerSocket(int gameID, PlayerID playerID){
        BattleshipGame game = Application.gameList.get(0);
        if(playerID.equals(PlayerID.P1)) return  game.p1Socket;
        return  game.p2Socket;
    };

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
