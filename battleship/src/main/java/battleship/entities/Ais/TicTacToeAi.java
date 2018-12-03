package battleship.entities.Ais;

import battleship.entities.boards.TicTacToeBoard;
import battleship.entities.games.Game;
import battleship.entities.games.TicTacToeGame;
import battleship.entities.ships.*;
import battleship.middlewares.Validation;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TicTacToeAi extends Ai {
    public enum State {UP, DOWN, LEFT, RIGHT, START, UPLEFT, UPRIGHT, DOWNLEFT, DOWNRIGHT}
    public boolean difficulty;
    public TicTacToeAi.State state;
    public Map<String, Integer> startPosition;

    public TicTacToeAi() {super();}

    public TicTacToeAi(TicTacToeAi.State state, boolean difficulty, Map<String, Integer> startPosition) {
        super();
        this.state = state;
        this.difficulty = difficulty;
        this.startPosition = startPosition;
    }


    // TODO refactor this in two or three different functions
    public static Map<String, Ship> generateFleet(TicTacToeBoard board) {
        Map<String, Ship> fleet = new HashMap<>();
        return fleet;
    }


    public Map<String, Integer> targetRandomPosition(TicTacToeGame ticTacToeGame) {
        Random r = new Random();
        Map<String, Integer> map = new HashMap<>();
        int x = r.nextInt(ticTacToeGame.playerOne.playerBoard.width);
        int y = r.nextInt(ticTacToeGame.playerOne.playerBoard.height);
        map.put("x", x);
        map.put("y", y);
        if (targetHasBeenUsed(map, ticTacToeGame)) targetRandomPosition(ticTacToeGame);
        return map;
    }

    public Map<String, Integer> targetMinMaxPosition(TicTacToeGame ticTacToeGame) {
        if (ticTacToeGame.recorder.playerTwoMoves.size() < 1) return targetRandomPosition(ticTacToeGame);

        Map<String, Integer> nextTarget = new HashMap<>();
        Map<String, Integer> prevTarget = ticTacToeGame.recorder.playerTwoMoves.get(ticTacToeGame.recorder.playerTwoMoves.size() - 1);
        int prevTargetX = prevTarget.get("x");
        int prevTargetY = prevTarget.get("y");
        int prevTargetedArea = prevTarget.get("hit");

        switch (this.state) {
            case START:
                if (prevTargetedArea == 0) {
                    nextTarget = targetRandomPosition(ticTacToeGame);
                } else {
                    this.startPosition = new HashMap<>(prevTarget);
                    this.state = TicTacToeAi.State.UP;
                    nextTarget = new HashMap<>(this.startPosition);
                    nextTarget.replace("y", ++prevTargetY);
                }
                break;
            case UP:
                if (prevTargetedArea == 0 || prevTargetedArea == -1) {
                    this.state = TicTacToeAi.State.DOWN;
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
                    this.state = TicTacToeAi.State.LEFT;
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
                    this.state = TicTacToeAi.State.RIGHT;
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
                    this.state = TicTacToeAi.State.START;
                    nextTarget = targetRandomPosition(ticTacToeGame);
                } else {
                    nextTarget = new HashMap<>(prevTarget);
                    nextTarget.replace("x", ++prevTargetX);
                }
                break;
        }
        if (Validation.targetIsOutOfBound(nextTarget, ticTacToeGame.playerOne.playerBoard) || targetHasBeenUsed(nextTarget, ticTacToeGame)) {
            switchToNextState();
            // TODO when state is forced to switch to next state because of validation, it keeps in mind the last prevTarget and jump a state case (does not enter case else); add a condition so it is forced to do at least one iteration into the else case
            return targetMinMaxPosition(ticTacToeGame);
        }
        return nextTarget;
    }


    private void switchToNextState(){
        if(this.state.equals(TicTacToeAi.State.START)) this.state = TicTacToeAi.State.UP;
        else if(this.state.equals(TicTacToeAi.State.UP)) this.state = TicTacToeAi.State.DOWN;
        else if(this.state.equals(TicTacToeAi.State.DOWN)) this.state = TicTacToeAi.State.LEFT;
        else if(this.state.equals(TicTacToeAi.State.LEFT)) this.state = TicTacToeAi.State.RIGHT;
        else if(this.state.equals(TicTacToeAi.State.RIGHT)) this.state = TicTacToeAi.State.START;
    }

    private boolean targetHasBeenUsed(Map<String, Integer> target, Game ticTacToeGame) {
        int targetX = target.get("x");
        int targetY = target.get("y");
        for (Map<String,Integer> t: ticTacToeGame.recorder.playerTwoMoves) {
            int x = t.get("x");
            int y = t.get("y");
            if(targetX == x && targetY == y) return true;
        }
        return false;
    }

    public TicTacToeAi.State stringToState(String s){
        switch (s) {
            case "START":
                return TicTacToeAi.State.START;
            case "UP":
                return TicTacToeAi.State.UP;
            case "DOWN":
                return TicTacToeAi.State.DOWN;
            case "LEFT":
                return TicTacToeAi.State.LEFT;
            case "RIGHT":
                return TicTacToeAi.State.RIGHT;
            default:
                return TicTacToeAi.State.START;
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

    public TicTacToeAi.State getState() {
        return state;
    }

    public void setState(TicTacToeAi.State state) {
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
