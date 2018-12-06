package battleship.controllers;

import battleship.Application;
import battleship.entities.*;
import battleship.entities.Ais.BattleshipAi;
import battleship.entities.boards.BattleshipBoard;
import battleship.entities.games.BattleshipGame;
import battleship.entities.games.Game;
import battleship.entities.players.BattleshipPlayer;
import battleship.entities.ships.Ship;
import com.corundumstudio.socketio.SocketIOClient;
import db.Db;
import db.XMLDb;
import net.sf.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BattleShipGameController implements GameController<BattleshipGame, BattleshipPlayer> {

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

        if(targetedArea > 0){
            actor.ennemyBoard.map[targetX][targetY] = -1;
            ennemy.playerBoard.map[targetX][targetY] = -1;
            ennemy.shipsRemaining--;
        } else if (targetedArea == 0) {
            actor.ennemyBoard.map[targetX][targetY] = -2;
            ennemy.playerBoard.map[targetX][targetY] = -2;
        }

        if(ennemy.shipsRemaining == 0){ actor.winner = true; }

        target.put("hit", targetedArea);
        if(recorder != null){
            if(actor.name.equals("playerOne")){
                recorder.playerOneMoves.add(target);
            } else {
                recorder.playerTwoMoves.add(target);
            }
        }
    }

    @Override
    public Game<BattleshipPlayer> replay(BattleshipGame battleshipGame){

    	BattleshipPlayer p1 = battleshipGame.playerOne;
        BattleshipPlayer p2 = battleshipGame.playerTwo;
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
    public Game<BattleshipPlayer> save(BattleshipGame battleshipGame){
        Db.getDb("battleship").save(battleshipGame);
        return battleshipGame;
    }

    @Override
    public Game<BattleshipPlayer> load(int gameID){
        Recorder r = new Recorder();
        BattleshipPlayer p1 = new BattleshipPlayer("playerOne");
        BattleshipPlayer p2 = new BattleshipPlayer("playerTwo");
        BattleshipAi ai = new BattleshipAi();
        BattleshipGame battleshipGame = new BattleshipGame(gameID,"battleship", p1,p2,r,ai);
        Db.getDb("battleship").load(battleshipGame);
        return battleshipGame;
    }


    @Override
    public boolean delete(int gameID){
        String id = String.valueOf(gameID);
        try {
            XMLDb.getXMLDb().deleteNode(id);
        } catch (Exception e){
            return false;
        }
        return true;
    }


    @Override
    public Game<BattleshipPlayer> start(JSONObject p1Settings) {
        // TODO validation of the player fleet
        // TODO the game could be instantiate before in the routes with the game factory
        JSONObject p1FleetJSON = p1Settings.getJSONObject("fleet");
        Map<String, Ship> playerOneFleet = Ship.buildFleet(p1FleetJSON);
        BattleshipBoard p1Board = new BattleshipBoard();
        p1Board.locateFleet(playerOneFleet);

        // fleet has to be validated with the grid, therefore the grid is initialise inside generateFleet maybe change the function name
        BattleshipBoard cpuBoard = new BattleshipBoard();
        Map<String, Ship> cpuFleet = BattleshipBoard.generateFleet(cpuBoard);

        BattleshipPlayer cpu = new BattleshipPlayer(cpuFleet, cpuBoard, p1Board, "playerTwo");
        BattleshipPlayer player = new BattleshipPlayer(playerOneFleet, p1Board, cpuBoard, "playerOne");
        Recorder recorder = new Recorder(new ArrayList<>(), new ArrayList<>());
        boolean difficulty = p1Settings.getBoolean("difficulty");
        BattleshipAi ai = new BattleshipAi(BattleshipAi.State.START,difficulty, null);

        int id = XMLDb.getXMLDb().getMaxID();
        BattleshipGame battleshipGame = new BattleshipGame(id, "battleship", player, cpu, recorder, ai);
        return  battleshipGame;
    }

    @Override
    public Game<BattleshipPlayer> play(BattleshipGame battleshipGame) {
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
    public Game<BattleshipPlayer> restart(BattleshipGame battleshipGame){
        battleshipGame.memento.memento = battleshipGame.memento;
        battleshipGame.memento.recorder = battleshipGame.recorder;
        battleshipGame = battleshipGame.memento;
        return battleshipGame;
    }

    @Override
    public JSONObject createOnlineGame(SocketIOClient client, String req){
        int id = Application.gameListVsHuman.size() + 1;
        JSONObject res = JSONObject.fromObject(req);
        JSONObject p1FleetJSON = res.getJSONObject("fleet");
        BattleshipPlayer player = new BattleshipPlayer("playerOne");

        Map<String, Ship> playerOneFleet = Ship.buildFleet(p1FleetJSON);
        BattleshipBoard p1Board = new BattleshipBoard();
        p1Board.locateFleet(playerOneFleet);
        player.playerBoard = p1Board;

        Game<BattleshipPlayer> game = new BattleshipGame(id, "battleship", player, null);
        res.put("id", game.id);
        res.put("map", p1Board.map);
        game.p1Socket = client;

        Application.gameListVsHuman.add(game);
        return res;
    }

    @Override
    public JSONObject joinOnlineGame(BattleshipGame game, SocketIOClient client, String req){
        JSONObject res = JSONObject.fromObject(req);
        BattleshipPlayer player = new BattleshipPlayer("playerTwo");
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
