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

    private boolean winnerVerticaly(TicTacToePlayer actor, int x, int y) {
        if(y == 0) {
            if(actor.playerBoard.map[x][y+1] == actor.sign && actor.playerBoard.map[x][y+2] == actor.sign ) {
                return true;
            }
        } else if (y ==1){
            if(actor.playerBoard.map[x][y-1] == actor.sign && actor.playerBoard.map[x][y+1] == actor.sign ) {
                return true;
            }
        } else if (y ==2){
            if(actor.playerBoard.map[x][y-1] == actor.sign && actor.playerBoard.map[x][y - 2 ] == actor.sign ) {
                return true;
            }
        }
        return false;
    }

    private boolean winnerHorizontaly(TicTacToePlayer actor, int x, int y) {
        if(x == 0) {
            if(actor.playerBoard.map[x + 1][y] == actor.sign && actor.playerBoard.map[x + 2][y] == actor.sign ) {
                return true;
            }
        } else if (x ==1){
            if(actor.playerBoard.map[x - 1][y] == actor.sign && actor.playerBoard.map[x+1][y] == actor.sign ) {
                return true;
            }
        } else if (x ==2){
            if(actor.playerBoard.map[x-1 ][y] == actor.sign && actor.playerBoard.map[x - 2 ][y] == actor.sign ) {
                return true;
            }
        }
        return false;
    }

    private boolean winnerDiagonaly(TicTacToePlayer actor, int x, int y) {
        if(x == 0 && y == 0) {
            if(actor.playerBoard.map[x + 1][y+1] == actor.sign && actor.playerBoard.map[x + 2][y + 2] == actor.sign ) {
                return true;
            }
        } else if (x == 2 && y == 0){
            if(actor.playerBoard.map[x - 1][y+1] == actor.sign && actor.playerBoard.map[x-2][y+2] == actor.sign ) {
                return true;
            }
        } else if (x == 0 && y == 2){
            if(actor.playerBoard.map[x + 1 ][y -1 ] == actor.sign && actor.playerBoard.map[x + 2 ][y - 2] == actor.sign ) {
                return true;
            }
        } else if (x==2 && y == 2){
            if(actor.playerBoard.map[x -1  ][y -1 ] == actor.sign && actor.playerBoard.map[x -2 ][y - 2] == actor.sign ) {
                return true;
            }
        }
        return false;
    }

    private boolean weHaveAWinner(TicTacToePlayer actor, Map<String,Integer> target){
        int targetX = target.get("x");
        int targetY = target.get("y");

        try{
            if(winnerVerticaly(actor, targetX, targetY) || winnerHorizontaly(actor, targetX, targetY) || winnerDiagonaly(actor, targetX, targetY)){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
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
        ennemy.playerBoard.map = actor.playerBoard.map;
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

        if(!p1.winner && !game.isDraw()){
            if(game.ai.difficulty){
                target = game.ai.targetMinMaxPosition(game);
            } else {
                target = game.ai.targetRandomPosition(game);
            }
            // cpu counter attacks
            playTurn(p2,p1,target,game.recorder);
        } else {
            game.draw = true;
        }
        return game;
    }

    @Override
    public Game replay(TicTacToeGame game) {
        return null;
    }

    @Override
    public Game start(JSONObject p1Settings) {
        // TODO this could be done by the game factory
        TicTacToeBoard board = new TicTacToeBoard();
        Map<String,Integer> playerTargets = new HashMap<>();
        Map<String,Integer> cpuTargets = new HashMap<>();

        TicTacToePlayer cpu = new TicTacToePlayer(cpuTargets, board, null, "playerTwo", -1);
        TicTacToePlayer player = new TicTacToePlayer(playerTargets, board, null, "playerOne", 1);
        Recorder recorder = new Recorder(new ArrayList<>(), new ArrayList<>());
        boolean difficulty = p1Settings.getBoolean("difficulty");
        TicTacToeAi ai = new TicTacToeAi(TicTacToeAi.State.FIRST,difficulty, null);

//        int id = Db.getDb().getMaxID();
        // TODO add a id
        TicTacToeGame ticTacToeGame = new TicTacToeGame(1, "ticTacToe", player, cpu, recorder, ai);
        return  ticTacToeGame;
    }

    @Override
    public Game restart(TicTacToeGame game) {
        game.memento.memento = game.memento;
        game.memento.recorder = game.recorder;
        game = game.memento;
        return game;
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
