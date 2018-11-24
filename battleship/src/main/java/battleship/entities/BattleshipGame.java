package battleship.entities;

import com.corundumstudio.socketio.SocketIOClient;

public class BattleshipGame extends Game {
    public int id;
    public Player playerOne;
    public Player playerTwo;
    public Recorder recorder;
    public Ai ai;

    // TODO make a battleshipGame extends Game
    public SocketIOClient p1Socket;
    public SocketIOClient p2Socket;

    public BattleshipGame(){
        super();
    }

    public BattleshipGame(Player playerOne, Player playerTwo) {
        super();
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public BattleshipGame(int id, Player playerOne, Player playerTwo, Recorder recorder, Ai ai) {
        super();
        this.id = id;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.recorder = recorder;
        this.ai = ai;
    }

    public BattleshipGame(int id, Player playerOne, Player playerTwo, Ai ai) {
        super();
        this.id = id;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.ai = ai;
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
    public Ai getAi() {return ai; }
    public void setAi(Ai ai) {this.ai = ai; }

    @Override
    public SocketIOClient getP1Socket() {
        return p1Socket;
    }

    @Override
    public void setP1Socket(SocketIOClient p1Socket) {
        this.p1Socket = p1Socket;
    }

    @Override
    public SocketIOClient getP2Socket() {
        return p2Socket;
    }

    @Override
    public void setP2Socket(SocketIOClient p2Socket) {
        this.p2Socket = p2Socket;
    }
}
