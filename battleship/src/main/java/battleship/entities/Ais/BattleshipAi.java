package battleship.entities.Ais;

import battleship.entities.boards.BattleshipBoard;
import battleship.entities.games.BattleshipGame;
import battleship.entities.games.Game;
import battleship.entities.ships.*;
import battleship.middlewares.Validation;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BattleshipAi extends Ai {
    public enum State {UP, DOWN, LEFT, RIGHT, START}
    public boolean difficulty;
    public State state;
    public Map<String, Integer> startPosition;

    public BattleshipAi() {super();}

    public BattleshipAi(State state, boolean difficulty, Map<String, Integer> startPosition) {
        super();
        this.state = state;
        this.difficulty = difficulty;
        this.startPosition = startPosition;
    }


    // TODO refactor this in two or three different functions
    public static Map<String, Ship> generateFleet(BattleshipBoard board) {
        Map<String, Ship> fleet = new HashMap<>();
        Ship carrier = new Carrier();
        Ship battleship = new Battleship();
        Ship cruiser = new Cruiser();
        Ship destroyer = new Destroyer();
        Ship submarine = new Submarine();

        carrier = carrier.locateToRandomPosition(board);
        board.locateShip(carrier);
        battleship = battleship.locateToRandomPosition(board);
        board.locateShip(battleship);
        cruiser = cruiser.locateToRandomPosition(board);
        board.locateShip(cruiser);
        destroyer = destroyer.locateToRandomPosition(board);
        board.locateShip(destroyer);
        submarine = submarine.locateToRandomPosition(board);
        board.locateShip(submarine);
        fleet.put("carrier", carrier);
        fleet.put("battleship", battleship);
        fleet.put("cruiser", cruiser);
        fleet.put("destroyer", destroyer);
        fleet.put("submarine", submarine);
        return fleet;
    }


    public Map<String, Integer> targetRandomPosition(BattleshipGame battleshipGame) {
        Random r = new Random();
        Map<String, Integer> map = new HashMap<>();
        int x = r.nextInt(battleshipGame.playerOne.playerBoard.width);
        int y = r.nextInt(battleshipGame.playerOne.playerBoard.height);
        map.put("x", x);
        map.put("y", y);

        if (targetHasBeenUsed(map, battleshipGame)) targetRandomPosition(battleshipGame);
        return map;
    }

    public Map<String, Integer> targetMinMaxPosition(BattleshipGame battleshipGame) {
        if (battleshipGame.recorder.playerTwoMoves.size() < 1) return targetRandomPosition(battleshipGame);

        Map<String, Integer> nextTarget = new HashMap<>();
        Map<String, Integer> prevTarget = battleshipGame.recorder.playerTwoMoves.get(battleshipGame.recorder.playerTwoMoves.size() - 1);
        int prevTargetX = prevTarget.get("x");
        int prevTargetY = prevTarget.get("y");
        int prevTargetedArea = prevTarget.get("hit");

        switch (this.state) {
            case START:
                if (prevTargetedArea == 0) {
                    nextTarget = targetRandomPosition(battleshipGame);
                } else {
                    this.startPosition = new HashMap<>(prevTarget);
                    this.state = State.UP;
                    nextTarget = new HashMap<>(this.startPosition);
                    nextTarget.replace("y", ++prevTargetY);
                }
                break;
            case UP:
                if (prevTargetedArea == 0 || prevTargetedArea == -1) {
                    this.state = State.DOWN;
                    nextTarget = new HashMap<>(this.startPosition);
                    prevTargetY = nextTarget.get("y");
                    nextTarget.replace("y", --prevTargetY);
                } else {
                    nextTarget = new HashMap<>(prevTarget);
                    nextTarget.replace("y", ++prevTargetY);
                }
                break;
            case DOWN:
                if (prevTargetedArea == 0 || prevTargetedArea == -1) {
                    this.state = State.LEFT;
                    nextTarget = new HashMap<>(this.startPosition);
                    prevTargetX = nextTarget.get("x");
                    nextTarget.replace("x", --prevTargetX);
                } else {
                    nextTarget = new HashMap<>(prevTarget);
                    nextTarget.replace("y", --prevTargetY);
                }
                break;
            case LEFT:
                if (prevTargetedArea == 0 || prevTargetedArea == -1) {
                    this.state = State.RIGHT;
                    nextTarget = new HashMap<>(this.startPosition);
                    prevTargetX = nextTarget.get("x");
                    nextTarget.replace("x", ++prevTargetX);
                } else {
                    nextTarget = new HashMap<>(prevTarget);
                    nextTarget.replace("x", --prevTargetX);
                }
                break;
            case RIGHT:
                if (prevTargetedArea == 0 || prevTargetedArea == -1) {
                    this.state = State.START;
                    nextTarget = targetRandomPosition(battleshipGame);
                } else {
                    nextTarget = new HashMap<>(prevTarget);
                    nextTarget.replace("x", ++prevTargetX);
                }
                break;
        }
        if (Validation.targetIsOutOfBound(nextTarget, battleshipGame.playerOne.playerBoard) || targetHasBeenUsed(nextTarget, battleshipGame)) {
            switchToNextState();
            // TODO when state is forced to switch to next state because of validation, it keeps in mind the last prevTarget and jump a state case (does not enter case else); add a condition so it is forced to do at least one iteration into the else case
            return targetMinMaxPosition(battleshipGame);
        }
        return nextTarget;
    }


    private void switchToNextState(){
        if(this.state.equals(State.START)) this.state = State.UP;
        else if(this.state.equals(State.UP)) this.state = State.DOWN;
        else if(this.state.equals(State.DOWN)) this.state = State.LEFT;
        else if(this.state.equals(State.LEFT)) this.state = State.RIGHT;
        else if(this.state.equals(State.RIGHT)) this.state = State.START;
    }

    private boolean targetHasBeenUsed(Map<String, Integer> target, Game battleshipGame) {
        int targetX = target.get("x");
        int targetY = target.get("y");
        for (Map<String,Integer> t: battleshipGame.recorder.playerTwoMoves) {
            int x = t.get("x");
            int y = t.get("y");
            if(targetX == x && targetY == y) return true;
        }
        return false;
    }

    public State stringToState(String s){
        switch (s) {
            case "START":
                return State.START;
            case "UP":
                return State.UP;
            case "DOWN":
                return State.DOWN;
            case "LEFT":
                return State.LEFT;
            case "RIGHT":
                return State.RIGHT;
            default:
                return State.START;
        }
    }

    //GETTERS & SETTERS //


    @Override
    public boolean isDifficulty() {
        return difficulty;
    }

    @Override
    public void setDifficulty(boolean difficulty) {
        this.difficulty = difficulty;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public Map<String, Integer> getStartPosition() {
        return startPosition;
    }

    @Override
    public void setStartPosition(Map<String, Integer> startPosition) {
        this.startPosition = startPosition;
    }
}
