package battleship.entities.games;

import battleship.entities.Ais.Ai;
import battleship.entities.players.BattleshipPlayer;
import battleship.entities.players.Player;
import battleship.entities.Recorder;
import com.corundumstudio.socketio.SocketIOClient;

public class Game<P extends Player> {
    public int id;
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

    public Game(int id, P playerOne, P playerTwo, Recorder recorder) {
        this.id = id;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.recorder = recorder;
    }

    public Game(int id, P playerOne, P playerTwo) {
        this.id = id;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public Game(Game game){
        this.id = game.id;
        this.playerOne = (P)game.playerOne;
        this.playerTwo = (P)game.playerTwo;
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
}
