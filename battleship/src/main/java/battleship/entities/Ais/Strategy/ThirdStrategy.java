package battleship.entities.Ais.Strategy;

import battleship.entities.Ais.TicTacToeAi;
import battleship.entities.Recorder;
import battleship.entities.boards.Board;

import java.util.HashMap;
import java.util.Map;

public class ThirdStrategy extends Strategy implements StrategyController {

    @Override
    public boolean hasToDefend(Board b) {
        return false;
    }

    @Override
    public Map<String, Integer> defendCorners(Recorder r, Board b) {

        return null;
    }

    @Override
    public Map<String, Integer> defendMiddle(Recorder r, Board b) {
        Map<String, Integer> nextTarget = new HashMap<>();
        Map<String, Integer> thirdOppTarget = r.playerOneMoves.get(2);
        int thirdOppTargetX = thirdOppTarget.get("x");
        int thirdOppTargetY = thirdOppTarget.get("y");

        return nextTarget;
    }

    public Map<String, Integer>getCrossTarget(Recorder r, Board b){
        Map<String, Integer> nextTarget = new HashMap<>();
        Map<String, Integer> secondTarget = r.playerTwoMoves.get(1);
        int secondTargetX = secondTarget.get("x");
        int secondTargetY = secondTarget.get("y");

        Corner corner = getCorner(secondTargetX, secondTargetY);
        if(corner == null){
            nextTarget.put("x", 0);
            nextTarget.put("y", 1);
            if(TicTacToeAi.targetHasBeenUsed(nextTarget, b)){
                nextTarget.replace("x", 2);
                nextTarget.replace("y", 1);
                if(TicTacToeAi.targetHasBeenUsed(nextTarget, b)){
                    nextTarget.replace("x", 1);
                    nextTarget.replace("y", 0);
                    if(TicTacToeAi.targetHasBeenUsed(nextTarget, b)){
                        nextTarget.replace("x", 1);
                        nextTarget.replace("y", 2);
                    }
                }
            }
        } else {
            switch (corner){
                case DOWN_RIGHT:
                    if(b.map[0][2] ==1){
                        nextTarget.put("x", 0);
                        nextTarget.put("y", 1);
                    } else {
                        nextTarget.put("x", 1);
                        nextTarget.put("y", 0);
                    }
                    break;
                case UP_RIGHT:
                    if(b.map[0][0] ==1){
                        nextTarget.put("x", 0);
                        nextTarget.put("y", 1);
                    } else {
                        nextTarget.put("x", 1);
                        nextTarget.put("y", 2);
                    }
                    break;
                case UP_LEFT:
                    if(b.map[0][2] ==1){
                        nextTarget.put("x", 1);
                        nextTarget.put("y", 2);
                    } else {
                        nextTarget.put("x", 2);
                        nextTarget.put("y", 1);
                    }
                    break;
                case DOWN_LEFT:
                    if(b.map[0][0] ==1){
                        nextTarget.put("x", 1);
                        nextTarget.put("y", 0);
                    } else {
                        nextTarget.put("x", 2);
                        nextTarget.put("y", 1);
                    }
                    break;
            }
        }
        return nextTarget;
    }

    @Override
    public Map<String, Integer> attackCorners(Recorder r, Board b) {

        int[][] map = b.map;
        Map<String, Integer> nextTarget = new HashMap<>();

        Map<String, Integer> firstOppTarget = r.playerOneMoves.get(0);
        int firstOppTargetX = firstOppTarget.get("x");
        int firstOppTargetY = firstOppTarget.get("y");
        Map<String, Integer> thirdOppTarget = r.playerOneMoves.get(2);
        int thirdOppTargetX = thirdOppTarget.get("x");
        int thirdOppTargetY = thirdOppTarget.get("y");
        Corner corner = getCorner(firstOppTargetX, firstOppTargetY);
        switch (corner){
            case DOWN_RIGHT:
                if (thirdOppTargetX == 0){
                    nextTarget.put("x", 0);
                    nextTarget.put("y", 2);
                } else {
                    nextTarget.put("x", 2);
                    nextTarget.put("y", 0);
                }
                break;
            case UP_LEFT:
                if (thirdOppTargetX == 2){
                    nextTarget.put("x", 2);
                    nextTarget.put("y", 0);
                } else {
                    if(thirdOppTargetY == 0){
                        nextTarget.put("x", 2);
                        nextTarget.put("y", 0);
                    } else {
                        nextTarget.put("x", 0);
                        nextTarget.put("y", 2);
                    }
                }
                break;
            case DOWN_LEFT:
                if (thirdOppTargetY == 0){
                    if(thirdOppTargetX == 1){
                        nextTarget.put("x", 0);
                        nextTarget.put("y", 0);
                    } else {
                        nextTarget.put("x", 2);
                        nextTarget.put("y", 2);
                    }
                } else {
                    nextTarget.put("x", 0);
                    nextTarget.put("y", 0);
                }
                break;
            case UP_RIGHT:
                if (thirdOppTargetY == 2){
                    nextTarget.put("x", 2);
                    nextTarget.put("y", 2);
                } else {
                    nextTarget.put("x", 0);
                    nextTarget.put("y", 0);
                }
                break;
        }
        return nextTarget;
    }

    @Override
    public Map<String, Integer> attack(Recorder r, Board b) {

        int[][] map = b.map;
        Map<String, Integer> nextTarget = new HashMap<>();
        Map<String, Integer> firstTarget = r.playerTwoMoves.get(0);
        Map<String, Integer> secondTarget = r.playerTwoMoves.get(1);
        Map<String, Integer> thirdOppTarget = r.playerOneMoves.get(2);
        int firstTargetX = firstTarget.get("x");
        int firstTargetY = firstTarget.get("y");
        int secondTargetX = secondTarget.get("x");
        int secondTargetY = secondTarget.get("y");
        int thirdOppTargetX = thirdOppTarget.get("x");
        int thirdOppTargetY = thirdOppTarget.get("y");

        if (firstTargetX == secondTargetX) {
            // marks are one to each other
            if (firstTargetY - secondTargetY == -1) {
                if(secondTargetY != 2){
                    if (map[secondTargetX][secondTargetY + 1] == 0) {
                        nextTarget.put("x", secondTargetX);
                        nextTarget.put("y", secondTargetY + 1);
                    }
                } else {
                    if (map[secondTargetX][secondTargetY -2] == 0) {
                        nextTarget.put("x", secondTargetX);
                        nextTarget.put("y", secondTargetY -2);
                    }
                }
            } else {
                // marks are spaced to each other
                if(secondTargetY != 0){
                    if (map[secondTargetX][secondTargetY - 1] == 0) {
                        nextTarget.put("x", secondTargetX);
                        nextTarget.put("y", secondTargetY - 1);
                    }

                } else {
                    if (map[secondTargetX][secondTargetY +2] == 0) {
                        nextTarget.put("x", secondTargetX);
                        nextTarget.put("y", secondTargetY +2);
                    }
                }
            }
        } else if (firstTargetY == secondTargetY) {
            // marks are one to each other
            if (firstTargetX - secondTargetX == -1) {
                if(secondTargetX !=2){
                    if (map[secondTargetX + 1][secondTargetY] == 0) {
                        nextTarget.put("x", secondTargetX + 1);
                        nextTarget.put("y", secondTargetY);
                    }

                } else {
                    if (map[secondTargetX - 2][secondTargetY] == 0) {
                        nextTarget.put("x", secondTargetX -2);
                        nextTarget.put("y", secondTargetY);
                    }
                }
            } else {
                // marks are spaced to each other
                if(secondTargetX != 0){
                    if (map[secondTargetX - 1][secondTargetY] == 0) {
                        nextTarget.put("x", secondTargetX - 1);
                        nextTarget.put("y", secondTargetY);
                    }
                } else {
                    if (map[secondTargetX + 2][secondTargetY] == 0) {
                        nextTarget.put("x", secondTargetX + 2);
                        nextTarget.put("y", secondTargetY);
                    }
                }
            }

        }

        if(nextTarget.size() == 0){
            Corner corner = getCorner(secondTargetX, secondTargetY);
            switch (corner){
                case DOWN_LEFT:
                    if(b.map[2][0] == 0){
                        nextTarget.put("x", 2);
                        nextTarget.put("y", 0);
                    }
                    break;
                case UP_LEFT:
                    if(b.map[2][2] == 0){
                        nextTarget.put("x", 2);
                        nextTarget.put("y", 2);
                    }
                    break;
                case UP_RIGHT:
                    if(b.map[0][2] == 0){
                        nextTarget.put("x", 0);
                        nextTarget.put("y", 2);
                    }
                    break;
                case DOWN_RIGHT:
                    if(b.map[0][0] == 0){
                        nextTarget.put("x", 0);
                        nextTarget.put("y", 0);
                    }
                    break;
            }
        }
        return nextTarget;
    }

    @Override
    public Map<String, Integer> defend(Recorder r, Board b) {
        Map<String, Integer> nextTarget = new HashMap<>();
        Map<String, Integer> thirdOppTarget = r.playerOneMoves.get(2);
        int thirdOppTargetX = thirdOppTarget.get("x");
        int thirdOppTargetY = thirdOppTarget.get("y");

        switch (thirdOppTargetX) {
            case 0:
                if (thirdOppTargetY == 1) {
                    nextTarget.put("x", 2);
                    nextTarget.put("y", 1);
                } else if (thirdOppTargetY == 2) {
                    nextTarget.put("x", 2);
                    nextTarget.put("y", 0);
                }
                break;
            case 1:
                if (thirdOppTargetY == 0) {
                    nextTarget.put("x", 1);
                    nextTarget.put("y", 2);
                } else if (thirdOppTargetY == 2) {
                    nextTarget.put("x", 1);
                    nextTarget.put("y", 0);
                }
                break;
            case 2:
                if (thirdOppTargetY == 0) {
                    nextTarget.put("x", 0);
                    nextTarget.put("y", 2);
                } else if (thirdOppTargetY == 1) {
                    nextTarget.put("x", 0);
                    nextTarget.put("y", 1);
                }
                break;
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
