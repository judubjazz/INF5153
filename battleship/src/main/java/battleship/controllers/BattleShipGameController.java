package battleship.controllers;

import battleship.Application;
import battleship.entities.*;
import battleship.entities.Ais.Ai;
import battleship.entities.Ais.BattleshipAi;
import battleship.entities.boards.BattleshipBoard;
import battleship.entities.boards.Board;
import battleship.entities.games.BattleshipGame;
import battleship.entities.games.Game;
import battleship.entities.players.BattleshipPlayer;
import battleship.entities.players.Player;
import battleship.entities.ships.Ship;
import com.corundumstudio.socketio.SocketIOClient;
import db.Db;
import net.sf.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BattleShipGameController implements GameController<BattleshipPlayer, BattleshipGame> {


    @Override
    public JSONObject playTurnOnline(BattleshipPlayer actor, BattleshipPlayer ennemy, String req){
        JSONObject res = JSONObject.fromObject(req);

        int targetX = res.getInt("x");
        int targetY = res.getInt("y");

        int targetedArea = actor.ennemyBoard.map[targetX][targetY];
        if(targetedArea > 0){ennemy.shipsRemaining--;}
        res.put("hit", targetedArea);

        actor.ennemyBoard.map[targetX][targetY] = -1;
        ennemy.playerBoard.map[targetX][targetY] = -1;

        if(ennemy.shipsRemaining == 0){
            actor.winner = true;
            res.put("winner", true);
        }

        return res;
    }

    private void playTurn(BattleshipPlayer actor, BattleshipPlayer ennemy, Map<String,Integer> target, Recorder recorder){
        int targetX = target.get("x");
        int targetY = target.get("y");

        int targetedArea = actor.ennemyBoard.map[targetX][targetY];

        if(targetedArea > 0){ennemy.shipsRemaining--;}

        target.put("hit", targetedArea);

        if(recorder != null){
            if(actor.name.equals("player1")){
                recorder.playerOneMoves.add(target);
            } else {
                recorder.playerTwoMoves.add(target);
            }
        }

        actor.ennemyBoard.map[targetX][targetY] = -1;
        ennemy.playerBoard.map[targetX][targetY] = -1;

        if(ennemy.shipsRemaining == 0){ actor.winner = true; }
    }

    @Override
    public Game replay(BattleshipGame battleshipGame){

    	BattleshipPlayer p1 = (BattleshipPlayer) battleshipGame.playerOne;
        BattleshipPlayer p2 = (BattleshipPlayer) battleshipGame.playerTwo;
        ArrayList<Map<String,Integer>> p1Moves = battleshipGame.recorder.playerOneMoves;
        ArrayList<Map<String,Integer>> p2Moves = battleshipGame.recorder.playerTwoMoves;

        // playerOne turn
        Map<String,Integer> target1 = p1Moves.get(battleshipGame.recorder.index);
        playTurn(p1,p2,target1,battleshipGame.recorder);

        // playerTwo turn
        Map<String,Integer> target2 = p2Moves.get(battleshipGame.recorder.index);
        playTurn(p2,p1,target2,battleshipGame.recorder);

        battleshipGame.recorder.index++;
        return battleshipGame;
    }

    @Override
    public Game save(BattleshipGame battleshipGame){
        Db.getDb().save(battleshipGame);
        return null;
    }

    @Override
    public Game load(int gameID){
        Recorder r = new Recorder();
        BattleshipPlayer p1 = new BattleshipPlayer("player1");
        BattleshipPlayer p2 = new BattleshipPlayer("player2");
        BattleshipAi ai = new BattleshipAi();
        BattleshipGame battleshipGame = new BattleshipGame(gameID,p1,p2,r,ai);
        Db.getDb().load(battleshipGame);
        return battleshipGame;
    }


    @Override
    public boolean delete(int gameID){
        String id = String.valueOf(gameID);
        try {
            Db.getDb().deleteNode(id);
        } catch (Exception e){
            return false;
        }
        return true;
    }


    @Override
    public Game start(String gameSettings) {
        // TODO validation of the player fleet, some validation methods are available in Board class, maybe add a Validation class
        JSONObject p1Settings = JSONObject.fromObject(gameSettings);
        JSONObject p1FleetJSON = p1Settings.getJSONObject("fleet");
        Map<String, Ship> playerOneFleet = Ship.buildFleet(p1FleetJSON);
        BattleshipBoard p1Board = new BattleshipBoard();
        p1Board.locateFleet(playerOneFleet);

        // fleet has to be validated with the grid, therefore the grid is initialise inside generateFleet maybe change the function name
        BattleshipBoard cpuBoard = new BattleshipBoard();
        Map<String, Ship> cpuFleet = BattleshipAi.generateFleet(cpuBoard);

        BattleshipPlayer cpu = new BattleshipPlayer(cpuFleet, cpuBoard, p1Board, "player2");
        BattleshipPlayer player = new BattleshipPlayer(playerOneFleet, p1Board, cpuBoard, "player1");
        Recorder recorder = new Recorder(new ArrayList<>(), new ArrayList<>());
        boolean difficulty = p1Settings.getBoolean("difficulty");
        BattleshipAi ai = new BattleshipAi(BattleshipAi.State.START,difficulty, null);

        int id = Db.getDb().getMaxID();
        Game battleshipGame = new BattleshipGame(id, player, cpu, recorder, ai);
        return  battleshipGame;
    }

    @Override
    public Game play(BattleshipGame battleshipGame) {
        BattleshipPlayer p1 = battleshipGame.playerOne;
        BattleshipPlayer p2 = battleshipGame.playerTwo;
        // player one attacks
        Map<String, Integer> target = new HashMap<>();
        target.put("x", p1.targetX);
        target.put("y", p1.targetY);

        playTurn(p1,p2,target,battleshipGame.recorder);

        // cpu counter attacks
        if(battleshipGame.ai.difficulty){
            target = battleshipGame.ai.targetMinMaxPosition(battleshipGame);
        } else {
            target = battleshipGame.ai.targetRandomPosition(battleshipGame);
        }

        playTurn(p2,p1,target,battleshipGame.recorder);

        return battleshipGame;
    }

    @Override
    public Game restart(BattleshipGame battleshipGame){
        // TODO add a memento;
        BattleshipPlayer p1 = (BattleshipPlayer)  battleshipGame.playerOne;
        BattleshipPlayer p2 = (BattleshipPlayer)  battleshipGame.playerTwo;

        Map<String, Ship> p1Fleet = Ship.buildFleetFromShips(p1.carrier, p1.battleship, p1.cruiser, p1.destroyer, p1.submarine);
        Map<String, Ship> p2Fleet = Ship.buildFleetFromShips(p2.carrier, p2.battleship, p2.cruiser, p2.destroyer, p2.submarine);
        BattleshipBoard p1Board = new BattleshipBoard();
        BattleshipBoard p2Board = new BattleshipBoard();

        p1Board.locateFleet(p1Fleet);
        p2Board.locateFleet(p2Fleet);

        p1.playerBoard = p2.ennemyBoard = p1Board;
        p2.playerBoard = p1.ennemyBoard = p2Board;
        p1.shipsRemaining = p2.shipsRemaining = 17;
        return battleshipGame;
    }

    @Override
    public JSONObject createOnlineGame(SocketIOClient client, String req){
        int id = Application.gameList.size() + 1;
        JSONObject res = JSONObject.fromObject(req);
        JSONObject p1FleetJSON = res.getJSONObject("fleet");
        BattleshipPlayer player = new BattleshipPlayer("player1");

        Map<String, Ship> playerOneFleet = Ship.buildFleet(p1FleetJSON);
        BattleshipBoard p1Board = new BattleshipBoard();
        p1Board.locateFleet(playerOneFleet);
        player.playerBoard = p1Board;

        Game game = new BattleshipGame(id, player, null, null);
        res.put("id", game.id);
        res.put("map", p1Board.map);
        game.p1Socket = client;

        Application.gameList.add(game);
        return res;
    }

    @Override
    public JSONObject joinOnlineGame(BattleshipGame game, SocketIOClient client, String req){
        JSONObject res = JSONObject.fromObject(req);
        BattleshipPlayer player = new BattleshipPlayer("player2");
        JSONObject p2FleetJSON = res.getJSONObject("fleet");
        Map<String, Ship> p2Fleet = Ship.buildFleet(p2FleetJSON);
        BattleshipBoard p2Board = new BattleshipBoard();
        p2Board.locateFleet(p2Fleet);

        res.put("map", p2Board.map);
        player.ennemyBoard = game.playerOne.playerBoard;
        player.playerBoard = game.playerOne.ennemyBoard = p2Board;
        game.playerTwo = player;
        game.setPlayerTwo(player);
        game.p2Socket = client;
        return res;
    }


}
