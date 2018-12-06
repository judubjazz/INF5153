package battleship.entities.games;

import battleship.entities.players.Player;
import battleship.entities.Recorder;
import com.corundumstudio.socketio.SocketIOClient;

public class Game<P extends Player> {
    public int id;
    public String name;
    public Recorder recorder;
    public P playerOne;
    public P playerTwo;
    public SocketIOClient p1Socket;
    public SocketIOClient p2Socket;


    public Game(){}

    public Game(P playerOne, P playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public Game(int id, String name, P playerOne, P playerTwo, Recorder recorder) {
        this.id = id;
        this.name = name;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.recorder = recorder;
    }

    public Game(int id, String name, P playerOne, P playerTwo) {
        this.id = id;
        this.name = name;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public Game(Game<P> game){
        this.id = game.id;
        this.name = game.name;
        this.playerOne = game.playerOne;
        this.playerTwo = game.playerTwo;
        this.recorder = null;
        this.p1Socket = null;
        this.p2Socket = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Recorder getRecorder() {
        return recorder;
    }

    public void setRecorder(Recorder recorder) {
        this.recorder = recorder;
    }

    public P getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(P playerOne) {
        this.playerOne = playerOne;
    }

    public P getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(P playerTwo) {
        this.playerTwo = playerTwo;
    }

    public SocketIOClient getP1Socket() {
        return p1Socket;
    }

    public void setP1Socket(SocketIOClient p1Socket) {
        this.p1Socket = p1Socket;
    }

    public SocketIOClient getP2Socket() {
        return p2Socket;
    }

    public void setP2Socket(SocketIOClient p2Socket) {
        this.p2Socket = p2Socket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
