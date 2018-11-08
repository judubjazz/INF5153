package battleship.entities;

public class BattleshipGame {
    public int id;
    public boolean difficulty;
    public Player playerOne;
    public Player playerTwo;
    public Recorder recorder;

    public BattleshipGame(){

    }

    public BattleshipGame(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public BattleshipGame(int id, Player playerOne, Player playerTwo, Recorder recorder, boolean difficulty) {
        this.id = id;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.recorder = recorder;
        this.difficulty = difficulty;
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
}
