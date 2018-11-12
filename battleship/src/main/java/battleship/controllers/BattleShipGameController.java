package battleship.controllers;

import battleship.entities.*;
import battleship.entities.ships.Ship;
import db.Db;
import net.sf.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BattleShipGameController implements GameController {
    @Override
    public BattleshipGame replay(BattleshipGame battleshipGame){
        Player p1 = battleshipGame.playerOne;
        Player p2 = battleshipGame.playerTwo;

        ArrayList<Map<String,Integer>> p1Moves = battleshipGame.recorder.playerOneMoves;
        ArrayList<Map<String,Integer>> p2Moves = battleshipGame.recorder.playerTwoMoves;

        // TODO Refactor this in one function , maybe play() in BattleshipGame class
        // playerOne turn
        Map<String,Integer> target = p1Moves.get(battleshipGame.recorder.index);
        int targetX = target.get("x");
        int targetY = target.get("y");
        int targetedArea = p1.ennemyBoard.map[targetX][targetY];
        if(targetedArea > 0){p2.shipsRemaining--;}
        p1.ennemyBoard.map[targetX][targetY] = -1;
        p2.playerBoard.map[targetX][targetY] = -1;
        if(p2.shipsRemaining == 0){p1.winner = true; }

        // playerTwo turn
        target = p2Moves.get(battleshipGame.recorder.index);
        targetX = target.get("x");
        targetY = target.get("y");
        targetedArea = p2.ennemyBoard.map[targetX][targetY];

        if(targetedArea > 0){p1.shipsRemaining--;}
        p2.ennemyBoard.map[targetX][targetY] = -1;
        p1.playerBoard.map[targetX][targetY] = -1;
        if(p1.shipsRemaining == 0){p2.winner = true;}

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
        JSONObject playerOneSettings = JSONObject.fromObject(gameSettings);
        JSONObject playerOneFleetJSON = playerOneSettings.getJSONObject("fleet");
        Map<String, Ship> playerOneFleet = Ship.buildFleet(playerOneFleetJSON);
        Board playerOneBoard = new Board();
        playerOneBoard.locateFleet(playerOneFleet);

        // fleet has to be validated with the grid, therefore the grid is initialise inside generateFleet maybe change the function name
        Board cpuBoard = new Board();
        Map<String, Ship> cpuFleet = Ai.generateFleet(cpuBoard);

        Player cpu = new Player(cpuFleet, cpuBoard, playerOneBoard, "player2");
        Player player = new Player(playerOneFleet, playerOneBoard, cpuBoard, "player1");
        Recorder recorder = new Recorder(new ArrayList<>(), new ArrayList<>());
        boolean difficulty = playerOneSettings.getBoolean("difficulty");
        Ai ai = new Ai(Ai.State.START,difficulty, null);


        // TODO add an id to the battleshipGame
        BattleshipGame battleshipGame = new BattleshipGame(1, player, cpu, recorder, ai);
        return  battleshipGame;
    }

    @Override
    public BattleshipGame play(BattleshipGame battleshipGame) {
        Player p1 = battleshipGame.playerOne;
        Player p2 = battleshipGame.playerTwo;
        // player one attacks
        Map<String, Integer> target = new HashMap<>();
        int targetX = p1.targetX;
        int targetY = p1.targetY;
        target.put("x", targetX);
        target.put("y", targetY);

        // TODO refactor the code below because it repeats itself
        int targetedArea = p1.ennemyBoard.map[targetX][targetY];
        if(targetedArea > 0){p2.shipsRemaining--;}
        target.put("hit", targetedArea);

        battleshipGame.recorder.playerOneMoves.add(target);
        p1.ennemyBoard.map[targetX][targetY] = -1;
        p2.playerBoard.map[targetX][targetY] = -1;

        if(p2.shipsRemaining == 0){ p1.winner = true; }

        // cpu counter attacks
        if(battleshipGame.ai.difficulty){
            target = battleshipGame.ai.targetMinMaxPosition(battleshipGame);
        } else {
            target = battleshipGame.ai.targetRandomPosition(battleshipGame);
        }

        int cpuTargetX = target.get("x");
        int cpuTargetY = target.get("y");

        targetedArea = p1.playerBoard.map[cpuTargetX][cpuTargetY];
        if(targetedArea > 0){p1.shipsRemaining--;}
        target.put("hit", targetedArea);

        battleshipGame.recorder.playerTwoMoves.add(target);
        p2.ennemyBoard.map[cpuTargetX][cpuTargetY] = -1;
        p1.playerBoard.map[cpuTargetX][cpuTargetY] = -1;

        if(p1.shipsRemaining == 0){p2.winner = true; }
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
