package battleship.entities;

import com.corundumstudio.socketio.SocketIOClient;

public class BattleshipGame {
    public int id;
    public Player playerOne;
    public Player playerTwo;
    public Recorder recorder;
    public Ai ai;

    // TODO make a battleshipGame extends Game and a online game extends Game
    public SocketIOClient p1Socket;
    public SocketIOClient p2Socket;

    public BattleshipGame(){

    }

    public BattleshipGame(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public BattleshipGame(int id, Player playerOne, Player playerTwo, Recorder recorder, Ai ai) {
        this.id = id;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.recorder = recorder;
        this.ai = ai;
    }

    public BattleshipGame(int id, Player playerOne, Player playerTwo, Ai ai) {
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
}
