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
    private boolean hasJustSwitchState = false;

    public BattleshipAi() {
        super();
    }

    public BattleshipAi(State state, boolean difficulty, Map<String, Integer> startPosition) {
        super();
        this.state = state;
        this.difficulty = difficulty;
        this.startPosition = startPosition;
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

    /**
     * Ai switches state from Start, Up, Down, Right, Left
     * if he had previously hit a ship, it will iterate all differents state
     * else it will hit a random target
     * @param battleshipGame
     * @return a target with keys x, y
     */
    public Map<String, Integer> targetMinMaxPosition(BattleshipGame battleshipGame) {
        // first turn
        if (battleshipGame.recorder.playerTwoMoves.size() < 1) return targetRandomPosition(battleshipGame);

        Map<String, Integer> nextTarget = new HashMap<>();
        Map<String, Integer> prevTarget = battleshipGame.recorder.playerTwoMoves.get(battleshipGame.recorder.playerTwoMoves.size() - 1);
        int prevTargetX = prevTarget.get("x");
        int prevTargetY = prevTarget.get("y");
        int prevTargetedArea = prevTarget.get("hit");

        // backup in case of theorical edge case that could cause infinite loop
        try {
            switch (this.state) {
                case START:
                    if (prevTargetedArea == 0 || hasJustSwitchState) {
                        nextTarget = targetRandomPosition(battleshipGame);
                    } else {
                        this.startPosition = new HashMap<>(prevTarget);
                        this.state = State.UP;
                        hasJustSwitchState = true;
                        nextTarget = new HashMap<>(this.startPosition);
                        nextTarget.replace("y", ++prevTargetY);
                    }
                    break;
                case UP:
                    if (prevTargetedArea < 1 && !hasJustSwitchState) {
                        this.state = State.DOWN;
                        hasJustSwitchState = true;
                        nextTarget = new HashMap<>(this.startPosition);
                        prevTargetY = nextTarget.get("y");
                        nextTarget.replace("y", --prevTargetY);
                    } else {
                        hasJustSwitchState = false;
                        nextTarget = new HashMap<>(prevTarget);
                        nextTarget.replace("y", ++prevTargetY);
                    }
                    break;
                case DOWN:
                    if (prevTargetedArea < 1 && !hasJustSwitchState) {
                        this.state = State.LEFT;
                        hasJustSwitchState = true;
                        nextTarget = new HashMap<>(this.startPosition);
                        prevTargetX = nextTarget.get("x");
                        nextTarget.replace("x", --prevTargetX);
                    } else {
                        hasJustSwitchState = false;
                        nextTarget = new HashMap<>(prevTarget);
                        nextTarget.replace("y", --prevTargetY);
                    }
                    break;
                case LEFT:
                    if (prevTargetedArea < 1 && !hasJustSwitchState) {
                        this.state = State.RIGHT;
                        hasJustSwitchState = true;
                        nextTarget = new HashMap<>(this.startPosition);
                        prevTargetX = nextTarget.get("x");
                        nextTarget.replace("x", ++prevTargetX);
                    } else {
                        hasJustSwitchState = false;
                        nextTarget = new HashMap<>(prevTarget);
                        nextTarget.replace("x", --prevTargetX);
                    }
                    break;
                case RIGHT:
                    if (prevTargetedArea < 1 && !hasJustSwitchState) {
                        this.state = State.START;
                        hasJustSwitchState = true;
                        nextTarget = targetRandomPosition(battleshipGame);
                    } else {
                        hasJustSwitchState = false;
                        nextTarget = new HashMap<>(prevTarget);
                        nextTarget.replace("x", ++prevTargetX);
                    }
                    break;
            }
            if (Validation.targetIsOutOfBound(nextTarget, battleshipGame.playerOne.playerBoard) || targetHasBeenUsed(nextTarget, battleshipGame)) {
                switchToNextState();
                this.hasJustSwitchState = true;
                return targetMinMaxPosition(battleshipGame);
            }
        } catch (StackOverflowError e){
            e.printStackTrace();
            return targetRandomPosition(battleshipGame);
        }
        return nextTarget;
    }


    private void switchToNextState() {
        if (this.state.equals(State.START)) this.state = State.UP;
        else if (this.state.equals(State.UP)) this.state = State.DOWN;
        else if (this.state.equals(State.DOWN)) this.state = State.LEFT;
        else if (this.state.equals(State.LEFT)) this.state = State.RIGHT;
        else if (this.state.equals(State.RIGHT)) this.state = State.START;
    }

    private boolean targetHasBeenUsed(Map<String, Integer> target, Game battleshipGame) {
        int targetX = target.get("x");
        int targetY = target.get("y");
        for (Map<String, Integer> t : battleshipGame.recorder.playerTwoMoves) {
            int x = t.get("x");
            int y = t.get("y");
            if (targetX == x && targetY == y) return true;
        }
        return false;
    }

    public State stringToState(String s) {
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
