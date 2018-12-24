package battleship.entities.Ais.Strategy;

import battleship.entities.Ais.TicTacToeAi;
import battleship.entities.Recorder;
import battleship.entities.boards.Board;

import java.util.HashMap;
import java.util.Map;

public class SecondStrategy extends Strategy implements StrategyController {

    @Override
    public boolean hasToDefend(Board board) {
        return board.map[2][2] != 1;
    }

    @Override
    public Map<String, Integer> defendCorners(Recorder r, Board b) {
        Map<String, Integer> nextTarget = new HashMap<>();
        Map<String, Integer> firstOppTarget = r.playerOneMoves.get(0);
        int firstOppTargetX = firstOppTarget.get("x");
        int firstOppTargetY = firstOppTarget.get("y");

        Map<String, Integer> secondOppTarget = r.playerOneMoves.get(1);
        int secondOppTargetX = secondOppTarget.get("x");
        int secondOppTargetY = secondOppTarget.get("y");

        Corner corner = getCorner(firstOppTargetX, firstOppTargetY);

        switch (corner){
            case DOWN_LEFT:
                if (secondOppTargetX == 0 && secondOppTargetY == 0) {
                    nextTarget.put("x", 0);
                    nextTarget.put("y", 1);
                } else if (secondOppTargetX == 0 && secondOppTargetY == 1) {
                    nextTarget.put("x", 0);
                    nextTarget.put("y", 0);
                } else if (secondOppTargetX == 2 && secondOppTargetY == 2) {
                    nextTarget.put("x", 1);
                    nextTarget.put("y", 2);
                } else if (secondOppTargetX == 1 && secondOppTargetY == 2) {
                    nextTarget.put("x", 2);
                    nextTarget.put("y", 2);
                }
                break;
            case UP_LEFT:
                if (secondOppTargetX == 0 && secondOppTargetY == 2) {
                    nextTarget.put("x", 0);
                    nextTarget.put("y", 1);
                } else if (secondOppTargetX == 0 && secondOppTargetY == 1) {
                    nextTarget.put("x", 0);
                    nextTarget.put("y", 2);
                } else if (secondOppTargetX == 2 && secondOppTargetY == 0) {
                    nextTarget.put("x", 1);
                    nextTarget.put("y", 0);
                } else if (secondOppTargetX == 1 && secondOppTargetY == 0) {
                    nextTarget.put("x", 2);
                    nextTarget.put("y", 0);
                }
                break;
            case UP_RIGHT:
                if (secondOppTargetX == 0 && secondOppTargetY == 0) {
                    nextTarget.put("x", 1);
                    nextTarget.put("y", 0);
                } else if (secondOppTargetX == 1 && secondOppTargetY == 0) {
                    nextTarget.put("x", 0);
                    nextTarget.put("y", 0);
                } else if (secondOppTargetX == 2 && secondOppTargetY == 2) {
                    nextTarget.put("x", 2);
                    nextTarget.put("y", 1);
                } else if (secondOppTargetX == 2 && secondOppTargetY == 1) {
                    nextTarget.put("x", 2);
                    nextTarget.put("y", 2);
                }
                break;
            case DOWN_RIGHT:
                if (secondOppTargetX == 2 && secondOppTargetY == 0) {
                    nextTarget.put("x", 2);
                    nextTarget.put("y", 1);
                } else if (secondOppTargetX == 2 && secondOppTargetY == 1) {
                    nextTarget.put("x", 2);
                    nextTarget.put("y", 0);
                } else if (secondOppTargetX == 0 && secondOppTargetY == 2) {
                    nextTarget.put("x", 1);
                    nextTarget.put("y", 2);
                } else if (secondOppTargetX == 1 && secondOppTargetY == 2) {
                    nextTarget.put("x", 0);
                    nextTarget.put("y", 2);
                }
                break;
        }
        if (nextTarget.size() == 0) {
            // a corner has not been targeted

        }
        return nextTarget;
    }

    @Override
    public Map<String, Integer> attackCorners(Recorder r, Board b) {
        return null;
    }

    @Override
    public Map<String, Integer> defendMiddle(Recorder r, Board b) {
        Map<String, Integer> nextTarget = new HashMap<>();
        Map<String, Integer> secondOppTarget = r.playerOneMoves.get(1);
        int secondOppTargetX = secondOppTarget.get("x");
        int secondOppTargetY = secondOppTarget.get("y");
        switch (secondOppTargetX) {
            case 0:
                if (secondOppTargetY == 1) {
                    nextTarget.put("x", 2);
                    nextTarget.put("y", 1);
                } else if (secondOppTargetY == 2) {
                    nextTarget.put("x", 2);
                    nextTarget.put("y", 0);
                }
                break;
            case 1:
                if (secondOppTargetY == 0) {
                    nextTarget.put("x", 1);
                    nextTarget.put("y", 2);
                } else if (secondOppTargetY == 2) {
                    nextTarget.put("x", 1);
                    nextTarget.put("y", 0);
                }
                break;
            case 2:
                if (secondOppTargetY == 0) {
                    nextTarget.put("x", 0);
                    nextTarget.put("y", 2);
                } else if (secondOppTargetY == 1) {
                    nextTarget.put("x", 0);
                    nextTarget.put("y", 1);
                }
                break;
        }
        return nextTarget;
    }

    public Map<String, Integer> defendCross(Recorder r, Board b) {
        Map<String, Integer> nextTarget = new HashMap<>();
        Map<String, Integer> secondOppTarget = r.playerOneMoves.get(1);
        int secondOppTargetX = secondOppTarget.get("x");
        int secondOppTargetY = secondOppTarget.get("y");
        nextTarget.put("x", 1);
        nextTarget.put("y", 2);
        if (secondOppTargetX == 1 || TicTacToeAi.targetHasBeenUsed(nextTarget, b)) {
            nextTarget.replace("x", 2);
            nextTarget.replace("y", 1);
        }
        return nextTarget;
    }

    @Override
    public Map<String, Integer> attack(Recorder r, Board b) {
        Map<String, Integer> nextTarget = new HashMap<>();
        nextTarget.put("x", 2);
        nextTarget.put("y", 2);
        if (TicTacToeAi.targetHasBeenUsed(nextTarget, b)) {
            nextTarget.replace("x", 2);
            nextTarget.replace("y", 0);
        }
        return null;
    }

    @Override
    public Map<String, Integer> defend(Recorder r, Board b) {
        Map<String, Integer> nextTarget = new HashMap<>();
        Map<String, Integer> firstOppTarget = r.playerOneMoves.get(0);
        int firstOppTargetX = firstOppTarget.get("x");
        int firstOppTargetY = firstOppTarget.get("y");

        if (firstOppTargetX == 0) {
            nextTarget.put("x", 2);
            nextTarget.put("y", 1);
        } else if (firstOppTargetX == 1) {
            if (firstOppTargetY == 0) {
                nextTarget.put("x", 1);
                nextTarget.put("y", 2);
            } else {
                nextTarget.put("x", 1);
                nextTarget.put("y", 0);
            }
        } else if (firstOppTargetX == 2) {
            nextTarget.put("x", 0);
            nextTarget.put("y", 1);
        }
        return nextTarget;
    }

    public boolean opponentTargetedASafePosition(Recorder r) {
        Map<String, Integer> firstOppTarget = r.playerOneMoves.get(0);
        int firstOppTargetX = firstOppTarget.get("x");
        int firstOppTargetY = firstOppTarget.get("y");
        Corner corner = getCorner(firstOppTargetX, firstOppTargetY);
        Map<String, Integer> secondOppTarget = r.playerOneMoves.get(1);
        int x = secondOppTarget.get("x");
        int y = secondOppTarget.get("y");

        switch (corner){
            case DOWN_RIGHT:
                if ((x == 0 && y == 0) || (x == 1 && y == 0) || (x == 0 && y == 1)) return true;
                break;
            case UP_RIGHT:
                if ((x == 0 && y == 2) || (x == 0 && y == 1) || (x == 1 && y == 2)) return true;
                break;
            case UP_LEFT:
                if ((x == 2 && y == 1) || (x == 2 && y == 2) || (x == 1 && y == 2)) return true;
                break;
            case DOWN_LEFT:
                if ((x == 1 && y == 0) || (x == 2 && y == 0) || (x == 2 && y == 1)) return true;
                break;
        }

        return false;
    }


}
