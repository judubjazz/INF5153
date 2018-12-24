package battleship.entities.Ais;

import battleship.entities.Ais.Strategy.*;
import battleship.entities.Recorder;
import battleship.entities.boards.Board;
import battleship.entities.games.TicTacToeGame;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TicTacToeAi extends Ai {
    public enum State {FIRST, SECOND, THIRD, FOURTH}
    public enum Strategy {DEFEND_CORNERS, ATTACK, ATTACK_CORNERS, DEFEND_MIDDLE, DEFEND_DEFAULT}

    public boolean difficulty;
    public TicTacToeAi.State state;
    public Strategy strategy;

    public TicTacToeAi() {
        super();
    }

    public TicTacToeAi(TicTacToeAi.State state, boolean difficulty, Map<String, Integer> startPosition) {
        super();
        this.state = state;
        this.difficulty = difficulty;
    }


    public static boolean targetHasBeenUsed(Map<String, Integer> target, Board b) {
        int targetX = target.get("x");
        int targetY = target.get("y");
        return b.map[targetX][targetY] != 0;
    }

    public Map<String, Integer> targetRandomPosition(TicTacToeGame ticTacToeGame) {
        Random r = new Random();
        Map<String, Integer> map = new HashMap<>();
        int x = r.nextInt(ticTacToeGame.playerOne.playerBoard.width);
        int y = r.nextInt(ticTacToeGame.playerOne.playerBoard.height);
        map.put("x", x);
        map.put("y", y);
        if (targetHasBeenUsed(map, ticTacToeGame.playerOne.playerBoard)) return targetRandomPosition(ticTacToeGame);
        return map;
    }

    public static boolean opponentPlayedInCorners(Recorder r, int index) {
        Map<String, Integer> target = r.playerOneMoves.get(index);
        int x = target.get("x");
        int y = target.get("y");
        return x != 1 && y != 1;
    }

    private boolean opponentPlayedInMiddle(Recorder r, int index) {
        Map<String, Integer> target = r.playerOneMoves.get(index);
        int x = target.get("x");
        int y = target.get("y");
        return x == 1 && y == 1;
    }

    private Map<String, Integer> resolveFirstCase(TicTacToeGame ticTacToeGame) {
        Map<String, Integer> nextTarget;
        StrategyController c = new FirstStrategy();

        if (opponentPlayedInCorners(ticTacToeGame.recorder, 0)) {
            nextTarget = c.defendCorners(null, null);
            this.strategy = Strategy.DEFEND_CORNERS;
        } else if (opponentPlayedInMiddle(ticTacToeGame.recorder, 0)) {
            nextTarget = c.defendMiddle(null, null);
            this.strategy = Strategy.DEFEND_MIDDLE;
        } else {
            nextTarget = c.attack(null, null);
            this.strategy = Strategy.ATTACK;
        }
        return nextTarget;
    }

    private Map<String, Integer> resolveSecondCase(TicTacToeGame ticTacToeGame) {
        Map<String, Integer> nextTarget = new HashMap<>();
        StrategyController c = new SecondStrategy();

        switch (this.strategy) {
            case DEFEND_MIDDLE:
                if (c.hasToDefend(ticTacToeGame.playerOne.playerBoard)) {
                    nextTarget = c.defendMiddle(ticTacToeGame.recorder, ticTacToeGame.playerOne.playerBoard);
                } else {
                    nextTarget.put("x", 2);
                    nextTarget.put("y", 0);
                }
                // opponent played middle on first move thus cpu will automaticly be attacking after second turn
                this.strategy = Strategy.ATTACK;
                break;
            case DEFEND_CORNERS:
                boolean opponentTargetedASafePosition = ((SecondStrategy) c).opponentTargetedASafePosition(ticTacToeGame.recorder);
                // bottoms right positions are backed up by the cpu middle marks on first turn
                if (opponentTargetedASafePosition) {
                    nextTarget = ((SecondStrategy) c).defendCross(ticTacToeGame.recorder, ticTacToeGame.playerOne.playerBoard);
                    this.strategy = Strategy.ATTACK_CORNERS;
                } else {
                    nextTarget = c.defendCorners(ticTacToeGame.recorder, ticTacToeGame.playerOne.playerBoard);
                    this.strategy = Strategy.DEFEND_DEFAULT;
                }
                break;
            case ATTACK:
                if (opponentPlayedInMiddle(ticTacToeGame.recorder,1)) {
                    nextTarget = c.defend(ticTacToeGame.recorder, ticTacToeGame.playerOne.playerBoard);
                    this.strategy = Strategy.DEFEND_DEFAULT;
                } else {
                    nextTarget = c.attack(ticTacToeGame.recorder, ticTacToeGame.playerOne.playerBoard);
                    this.strategy = Strategy.ATTACK;
                }
                break;
        }
        return nextTarget;
    }


    private Map<String, Integer> resolveThirdCase(TicTacToeGame ticTacToeGame) {
        StrategyController c = new ThirdStrategy();
        Map<String, Integer> nextTarget = new HashMap<>();

        switch(this.strategy){
            case ATTACK:
                nextTarget = c.attack(ticTacToeGame.recorder, ticTacToeGame.playerOne.playerBoard);
                //cpu will win on this turn or will defend on next one
                this.strategy = Strategy.DEFEND_DEFAULT;
                // player one did defend thus player two needs to also defends after third turn
                if (nextTarget.size() == 0) {
                    nextTarget = c.defend(ticTacToeGame.recorder, ticTacToeGame.playerOne.playerBoard);
                    this.strategy = Strategy.DEFEND_DEFAULT;
                }
                break;
            case ATTACK_CORNERS:
                nextTarget = c.attack(ticTacToeGame.recorder,ticTacToeGame.playerOne.playerBoard);
                if(nextTarget.size() == 0){
                    nextTarget = c.attackCorners(ticTacToeGame.recorder, ticTacToeGame.playerOne.playerBoard);
                }
                this.strategy = Strategy.ATTACK_CORNERS;
                break;
            case DEFEND_DEFAULT:
                nextTarget = c.attack(ticTacToeGame.recorder, ticTacToeGame.playerOne.playerBoard);
                //cpu will win on this turn or will defend on next one
                this.strategy = Strategy.DEFEND_DEFAULT;
                // player one did defend thus cpu can reattack
                if (nextTarget.size() == 0) {
                    nextTarget = ((ThirdStrategy) c).getCrossTarget(ticTacToeGame.recorder, ticTacToeGame.playerOne.playerBoard);
                    this.strategy = Strategy.ATTACK;
                }
                break;
        }
        return nextTarget;
    }

    private Map<String, Integer> resolveFourthCase(TicTacToeGame ticTacToeGame) {
        Map<String, Integer> nextTarget = new HashMap<>();
        StrategyController c = new FourthStrategy();

        switch(this.strategy){
            case DEFEND_DEFAULT:
                nextTarget = c.defend(ticTacToeGame.recorder, ticTacToeGame.playerOne.playerBoard);
                if (nextTarget.size() == 0){
                    nextTarget = c.attack(ticTacToeGame.recorder, ticTacToeGame.playerOne.playerBoard);
                }
                break;
            case ATTACK:
                nextTarget = c.attack(ticTacToeGame.recorder, ticTacToeGame.playerOne.playerBoard);
                break;
            case ATTACK_CORNERS:
                nextTarget = c.attackCorners(ticTacToeGame.recorder, ticTacToeGame.playerOne.playerBoard);
                if (nextTarget.size() == 0){
                    nextTarget = c.defendCorners(ticTacToeGame.recorder, ticTacToeGame.playerOne.playerBoard);
                }
                break;
        }
        return nextTarget;
    }


    public Map<String, Integer> targetMinMaxPosition(TicTacToeGame ticTacToeGame) {
        Map<String, Integer> nextTarget = new HashMap<>();
        switch (this.state) {
            case FIRST:
                nextTarget = resolveFirstCase(ticTacToeGame);
                this.state = State.SECOND;
                break;
            case SECOND:
                nextTarget = resolveSecondCase(ticTacToeGame);
                this.state = State.THIRD;
                break;
            case THIRD:
                nextTarget = resolveThirdCase(ticTacToeGame);
                this.state = State.FOURTH;
                break;
            case FOURTH:
                nextTarget = resolveFourthCase(ticTacToeGame);
                break;
        }
        return nextTarget;
    }


    public TicTacToeAi.State stringToState(String s) {
        switch (s) {
            case "FIRST":
                return State.FIRST;
            case "SECOND":
                return State.SECOND;
            case "THIRD":
                return State.THIRD;
            case "FOURTH":
                return State.FOURTH;
            default:
                return State.FIRST;
        }
    }

    public TicTacToeAi.Strategy stringToStrategy(String s) {
        switch (s) {
            case "DEFEND_DEFAULT":
                return Strategy.DEFEND_DEFAULT;
            case "ATTACK":
                return Strategy.ATTACK;
            case "ATTACK_CORNERS":
                return Strategy.ATTACK_CORNERS;
            case "DEFEND_CORNERS":
                return Strategy.DEFEND_CORNERS;
            case "DEFEND_MIDDLE":
                return Strategy.DEFEND_MIDDLE;
            default:
                return Strategy.DEFEND_DEFAULT;
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

    public Strategy getStrategy() {
        return strategy;
    }

    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }
}
