package battleship.controllers;

import battleship.entities.*;
import battleship.entities.ships.Ship;
import db.Db;
import net.sf.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BattleShipGameController implements GameController {
    private void playTurn(Player actor, Player ennemy, Map<String,Integer> target, Recorder recorder){
        int targetX = target.get("x");
        int targetY = target.get("y");

        int targetedArea = actor.ennemyBoard.map[targetX][targetY];
        if(targetedArea > 0){ennemy.shipsRemaining--;}
        target.put("hit", targetedArea);

        if(actor.name.equals("player1")){
            recorder.playerOneMoves.add(target);
        } else {
            recorder.playerTwoMoves.add(target);
        }

        actor.ennemyBoard.map[targetX][targetY] = -1;
        ennemy.playerBoard.map[targetX][targetY] = -1;

        if(ennemy.shipsRemaining == 0){ actor.winner = true; }
    }


    @Override
    public BattleshipGame replay(BattleshipGame battleshipGame){
        Player p1 = battleshipGame.playerOne;
        Player p2 = battleshipGame.playerTwo;
        ArrayList<Map<String,Integer>> p1Moves = battleshipGame.recorder.playerOneMoves;
        ArrayList<Map<String,Integer>> p2Moves = battleshipGame.recorder.playerTwoMoves;

        // playerOne turn
        Map<String,Integer> target = p1Moves.get(battleshipGame.recorder.index);
        playTurn(p1,p2,target,battleshipGame.recorder);

        // playerTwo turn
        target = p2Moves.get(battleshipGame.recorder.index);
        playTurn(p2,p1,target,battleshipGame.recorder);

        battleshipGame.recorder.index++;
        return battleshipGame;
    }
    @Override
    public BattleshipGame save(BattleshipGame battleshipGame){
        Db.getInstance().save(battleshipGame);
        return null;
    }
    @Override
    public BattleshipGame load(){
        Recorder r = new Recorder();
        Player p1 = new Player("player1");
        Player p2 = new Player("player2");
        BattleshipGame battleshipGame = new BattleshipGame(1,p1,p2,r,null);
        Db.getInstance().load(battleshipGame);
        return battleshipGame;
    }

    @Override
    public BattleshipGame start(String gameSettings) {
        // TODO validation of the player fleet, some validation methods are available in Board class, maybe add a Validation class
        JSONObject p1Settings = JSONObject.fromObject(gameSettings);
        JSONObject p1FleetJSON = p1Settings.getJSONObject("fleet");
        Map<String, Ship> playerOneFleet = Ship.buildFleet(p1FleetJSON);
        Board p1Board = new Board();
        p1Board.locateFleet(playerOneFleet);

        // fleet has to be validated with the grid, therefore the grid is initialise inside generateFleet maybe change the function name
        Board cpuBoard = new Board();
        Map<String, Ship> cpuFleet = Ai.generateFleet(cpuBoard);

        Player cpu = new Player(cpuFleet, cpuBoard, p1Board, "player2");
        Player player = new Player(playerOneFleet, p1Board, cpuBoard, "player1");
        Recorder recorder = new Recorder(new ArrayList<>(), new ArrayList<>());
        boolean difficulty = p1Settings.getBoolean("difficulty");
        Ai ai = new Ai(Ai.State.START,difficulty, null);

        // TODO get maxid from db and add it to the game
        BattleshipGame battleshipGame = new BattleshipGame(1, player, cpu, recorder, ai);
        return  battleshipGame;
    }

    @Override
    public BattleshipGame play(BattleshipGame battleshipGame) {
        Player p1 = battleshipGame.playerOne;
        Player p2 = battleshipGame.playerTwo;
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
    public BattleshipGame restart(BattleshipGame battleshipGame){
        Player p1 = battleshipGame.playerOne;
        Player p2 = battleshipGame.playerTwo;

        Map<String, Ship> p1Fleet = Ship.buildFleetFromShips(p1.carrier, p1.battleship, p1.cruiser, p1.destroyer, p1.submarine);
        Map<String, Ship> p2Fleet = Ship.buildFleetFromShips(p2.carrier, p2.battleship, p2.cruiser, p2.destroyer, p2.submarine);
        Board p1Board = new Board();
        Board p2Board = new Board();
        p1.playerBoard = p2.ennemyBoard = p1Board;
        p2.playerBoard = p1.ennemyBoard = p2Board;

        p1.playerBoard.locateFleet(p1Fleet);
        p1.ennemyBoard.locateFleet(p2Fleet);
        p2.playerBoard.locateFleet(p2Fleet);
        p2.ennemyBoard.locateFleet(p1Fleet);

        p1.shipsRemaining = p2.shipsRemaining = 17;
        return battleshipGame;
    }
}
