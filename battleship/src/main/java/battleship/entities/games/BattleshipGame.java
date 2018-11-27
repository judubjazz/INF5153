package battleship.entities.games;

import battleship.entities.Ais.Ai;
import battleship.entities.Ais.BattleshipAi;
import battleship.entities.players.BattleshipPlayer;
import battleship.entities.players.Player;
import battleship.entities.Recorder;
import com.corundumstudio.socketio.SocketIOClient;

public class BattleshipGame extends Game {
    public BattleshipAi ai;
    public BattleshipPlayer playerOne;
    public BattleshipPlayer playerTwo;

    public BattleshipGame(){
        super();
    }

    public BattleshipGame(BattleshipPlayer playerOne, BattleshipPlayer playerTwo) {
        super(playerOne, playerTwo);
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public BattleshipGame(int id, BattleshipPlayer playerOne, BattleshipPlayer playerTwo, Recorder recorder, BattleshipAi ai) {
        super(id, playerOne, playerTwo, recorder);
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.ai = ai;
    }

    public BattleshipGame(int id, BattleshipPlayer playerOne, BattleshipPlayer playerTwo, BattleshipAi ai) {
        super(id, playerOne, playerTwo);
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.ai = ai;
    }


    @Override
    public BattleshipPlayer getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(BattleshipPlayer playerOne) {
        super.playerOne = playerOne;
        this.playerOne = playerOne;
    }

    @Override
    public BattleshipPlayer getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(BattleshipPlayer playerTwo) {
        super.playerTwo = playerTwo;
        this.playerTwo = playerTwo;
    }

    public BattleshipAi getAi() {
        return ai;
    }

    public void setAi(BattleshipAi ai) {
        this.ai = ai;
    }
}
