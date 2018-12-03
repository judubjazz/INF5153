package battleship.entities.games;

import battleship.entities.Ais.TicTacToeAi;
import battleship.entities.Recorder;
import battleship.entities.players.TicTacToePlayer;

public class TicTacToeGame extends Game {
    public TicTacToeAi ai;
    public TicTacToePlayer playerOne;
    public TicTacToePlayer playerTwo;
    public TicTacToeGame memento;

    public TicTacToeGame(){
        super();
    }

    public TicTacToeGame(TicTacToePlayer playerOne, TicTacToePlayer playerTwo) {
        super(playerOne, playerTwo);
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public TicTacToeGame(int id, String name, TicTacToePlayer playerOne, TicTacToePlayer playerTwo, Recorder recorder, TicTacToeAi ai) {
        super(id, name, playerOne, playerTwo, recorder);
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.ai = ai;
        if (this.memento == null){
            this.memento = new TicTacToeGame(this);
        }
    }

    public TicTacToeGame(int id, String name, TicTacToePlayer playerOne, TicTacToePlayer playerTwo) {
        super(id, name, playerOne, playerTwo);
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    private TicTacToeGame(TicTacToeGame game){
        this.id = game.id;
        this.playerOne = game.playerOne;
        this.playerTwo = game.playerTwo;
        this.ai = game.ai;
    }

    @Override
    public TicTacToePlayer getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(TicTacToePlayer playerOne) {
        super.playerOne = playerOne;
        this.playerOne = playerOne;
    }

    @Override
    public TicTacToePlayer getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(TicTacToePlayer playerTwo) {
        super.playerTwo = playerTwo;
        this.playerTwo = playerTwo;
    }

    public TicTacToeAi getAi() {
        return ai;
    }

    public void setAi(TicTacToeAi ai) {
        this.ai = ai;
    }

    public TicTacToeGame getMemento() {
        return memento;
    }

    public void setMemento(TicTacToeGame memento) {
        this.memento = memento;
    }
}
