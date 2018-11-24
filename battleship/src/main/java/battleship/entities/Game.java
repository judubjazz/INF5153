package battleship.entities;

import com.corundumstudio.socketio.SocketIOClient;

public class Game {
    public int id;
    public Recorder recorder;
    public Ai ai;
    public Player playerOne;
    public Player playerTwo;
    public SocketIOClient p1Socket;
    public SocketIOClient p2Socket;

    public Game(){}


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    public Recorder getRecorder() {
        return recorder;
    }

    public void setRecorder(Recorder recorder) {
        this.recorder = recorder;
    }

    public Ai getAi() {
        return ai;
    }

    public void setAi(Ai ai) {
        this.ai = ai;
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
