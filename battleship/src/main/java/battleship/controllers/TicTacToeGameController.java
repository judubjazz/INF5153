package battleship.controllers;

import battleship.entities.Ais.TicTacToeAi;
import battleship.entities.Recorder;
import battleship.entities.boards.TicTacToeBoard;
import battleship.entities.games.TicTacToeGame;
import battleship.entities.games.Game;
import battleship.entities.games.TicTacToeGame;
import battleship.entities.players.TicTacToePlayer;
import battleship.entities.players.TicTacToePlayer;
import com.corundumstudio.socketio.SocketIOClient;
import db.Db;
import db.MongoDb;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TicTacToeGameController implements GameController<TicTacToeGame, TicTacToePlayer> {

    @Override
    public Game load(int gameID) {
        return null;
    }

    @Override
    public Game save(TicTacToeGame game) {
        return null;
    }

    @Override
    public boolean delete(int gameID) {
        return false;
    }

    private boolean weHaveAWinner(TicTacToePlayer actor, Map<String,Integer> target){
        // TODO only checks horizontals lines for testing
        int targetX = target.get("x");
        int targetY = target.get("y");

        int targetX2 = targetX + 1;
        int targetX3 = targetX + 2;


        if(actor.playerBoard.map[targetX2][targetY] == actor.sign && actor.playerBoard.map[targetX3][targetY] == actor.sign ) {
            return true;
        }
        return false;
    }

    private void playTurn(TicTacToePlayer actor, TicTacToePlayer ennemy, Map<String,Integer> target, Recorder recorder){
        int targetX = target.get("x");
        int targetY = target.get("y");

        if(recorder != null){
            if(actor.name.equals("playerOne")){
                recorder.playerOneMoves.add(target);
            } else {
                recorder.playerTwoMoves.add(target);
            }
        }

        actor.playerBoard.map[targetX][targetY] = actor.sign;

        actor.playerBoard.map = ennemy.playerBoard.map;

        if(weHaveAWinner(actor,target)) actor.winner = true;

    }

    @Override
    public Game play(TicTacToeGame game) {
        TicTacToePlayer p1 = game.playerOne;
        TicTacToePlayer p2 = game.playerTwo;
        // player one attacks
        Map<String, Integer> target = new HashMap<>();
        target.put("x", p1.targetX);
        target.put("y", p1.targetY);

        playTurn(p1,p2,target,game.recorder);

        // cpu counter attacks
        if(game.ai.difficulty){
            target = game.ai.targetMinMaxPosition(game);
        } else {
            target = game.ai.targetRandomPosition(game);
        }

        playTurn(p2,p1,target,game.recorder);

        return game;
    }

    @Override
    public Game replay(TicTacToeGame game) {
        return null;
    }

    @Override
    public Game start(JSONObject p1Settings) {
        // TODO this could be done by the game factory
        JSONObject p1FleetJSON = p1Settings.getJSONObject("marks");
        TicTacToeBoard board = new TicTacToeBoard();
        Map<String,Integer> playerTargets = new HashMap<>();
        Map<String,Integer> cpuTargets = new HashMap<>();

        TicTacToePlayer cpu = new TicTacToePlayer(cpuTargets, board, null, "playerTwo", 'O');
        TicTacToePlayer player = new TicTacToePlayer(playerTargets, board, null, "playerOne", 'X');
        Recorder recorder = new Recorder(new ArrayList<>(), new ArrayList<>());
        boolean difficulty = p1Settings.getBoolean("difficulty");
        TicTacToeAi ai = new TicTacToeAi(TicTacToeAi.State.START,difficulty, null);

//        int id = Db.getDb().getMaxID();
        // TODO add a id
        TicTacToeGame ticTacToeGame = new TicTacToeGame(1, "ticTacToe", player, cpu, recorder, ai);
        return  ticTacToeGame;
    }

    @Override
    public Game restart(TicTacToeGame game) {
        return null;
    }

    @Override
    public JSONObject createOnlineGame(SocketIOClient client, String req) {
        return null;
    }

    @Override
    public JSONObject joinOnlineGame(TicTacToeGame game, SocketIOClient client, String req) {
        return null;
    }

    @Override
    public JSONObject playTurnOnline(TicTacToePlayer p1, TicTacToePlayer p2, String req) {
        return null;
    }
}
