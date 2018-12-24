package battleship.entities.Ais.Strategy;

import battleship.entities.Ais.TicTacToeAi;
import battleship.entities.Recorder;
import battleship.entities.boards.Board;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FourthStrategy extends Strategy implements StrategyController {

    @Override
    public boolean hasToDefend(Board b) {
        return false;
    }

    private Map<String, Integer> nextTargetIsAFreeTarget(Recorder r, Board b, Map<String, Integer> nextTarget){
        ArrayList<Map<String, Integer>> list = getFreeTargetsList(r, b);
        Map<String, Integer> firstFreeTarget = list.get(0);
        Map<String, Integer> secondFreeTarget = list.get(1);
        if(nextTarget.equals(firstFreeTarget)) return firstFreeTarget;
        if(nextTarget.equals(secondFreeTarget)) return secondFreeTarget;
        nextTarget.clear();
        return nextTarget;
    }

    @Override
    public Map<String, Integer> defendCorners(Recorder r, Board b) {
        Map<String, Integer> nextTarget = new HashMap<>();

        Map<String, Integer> fourthOppTarget = r.playerOneMoves.get(3);
        int fourthOppTargetX = fourthOppTarget.get("x");
        int fourthOppTargetY = fourthOppTarget.get("y");
        Corner corner = getCorner(fourthOppTargetX, fourthOppTargetY);
        switch (corner) {
            case DOWN_LEFT:
                if (b.map[0][0] == 1) {
                    if (b.map[2][0] == 0) {
                        nextTarget.put("x", 2);
                        nextTarget.put("y", 0);
                    }
                } else if (b.map[0][1] == 1) {
                    if (b.map[0][0] == 0) {
                        nextTarget.put("x", 0);
                        nextTarget.put("y", 0);
                    }
                } else if (b.map[2][2] == 1) {
                    if (b.map[1][2] == 0) {
                        nextTarget.put("x", 1);
                        nextTarget.put("y", 2);

                    }
                } else if (b.map[1][2] == 1) {
                    if (b.map[2][2] == 0) {
                        nextTarget.put("x", 2);
                        nextTarget.put("y", 2);
                    }
                }
                break;
            case UP_LEFT:
                if (b.map[0][2] == 1) {
                    if (b.map[0][1] == 0) {
                        nextTarget.put("x", 0);
                        nextTarget.put("y", 1);
                    }
                } else if (b.map[0][1] == 1) {
                    if (b.map[0][2] == 0) {
                        nextTarget.put("x", 0);
                        nextTarget.put("y", 2);
                    }
                } else if (b.map[2][0] == 1) {
                    if (b.map[1][0] == 0) {
                        nextTarget.put("x", 1);
                        nextTarget.put("y", 0);

                    }
                } else if (b.map[1][0] == 1) {
                    if (b.map[2][0] == 0) {
                        nextTarget.put("x", 2);
                        nextTarget.put("y", 0);
                    }
                }
                break;
            case UP_RIGHT:
                if (b.map[0][0] == 1) {
                    if (b.map[1][0] == 0) {
                        nextTarget.put("x", 1);
                        nextTarget.put("y", 0);
                    }
                } else if (b.map[1][0] == 1) {
                    if (b.map[0][0] == 0) {
                        nextTarget.put("x", 0);
                        nextTarget.put("y", 0);
                    }
                } else if (b.map[2][2] == 1) {
                    if (b.map[2][1] == 0) {
                        nextTarget.put("x", 2);
                        nextTarget.put("y", 1);

                    }
                } else if (b.map[2][1] == 1) {
                    if (b.map[2][2] == 0) {
                        nextTarget.put("x", 2);
                        nextTarget.put("y", 2);
                    }
                }
                break;
            case DOWN_RIGHT:
                if (b.map[2][0] == 1) {
                    if (b.map[2][1] == 0) {
                        nextTarget.put("x", 2);
                        nextTarget.put("y", 1);

                    }
                } else if (b.map[2][1] == 1) {
                    if (b.map[2][0] == 0) {
                        nextTarget.put("x", 2);
                        nextTarget.put("y", 0);
                    }
                } else if (b.map[0][2] == 1) {
                    if (b.map[0][1] == 0) {
                        nextTarget.put("x", 0);
                        nextTarget.put("y", 1);

                    }
                } else if (b.map[1][2] == 1) {
                    if (b.map[0][2] == 0) {
                        nextTarget.put("x", 0);
                        nextTarget.put("y", 2);
                    }
                }
                break;
            default:
                nextTarget = null;
                break;
        }
        if (nextTarget.size() == 0 ){
            ArrayList<Map<String, Integer>> list = getFreeTargetsList(r, b);
            // it will draw anyway
            nextTarget = list.get(0);
        }
        return nextTarget;
    }

    @Override
    public Map<String, Integer> defendMiddle(Recorder r, Board b) {
        return null;
    }

    @Override
    public Map<String, Integer> attack(Recorder r, Board b) {
        Map<String, Integer> nextTarget = new HashMap<>();
        Map<String, Integer> thirdTarget = r.playerTwoMoves.get(2);
        int thirdTargetX = thirdTarget.get("x");
        int thirdTargetY = thirdTarget.get("y");

        switch (thirdTargetX) {
            case 0:
                if (thirdTargetY == 1) {
                    nextTarget.put("x", 2);
                    nextTarget.put("y", 1);
                }
                break;
            case 1:
                if (thirdTargetY == 0) {
                    nextTarget.put("x", 1);
                    nextTarget.put("y", 2);
                } else if (thirdTargetY == 2) {
                    nextTarget.put("x", 1);
                    nextTarget.put("y", 0);
                }
                break;
            case 2:
                if (thirdTargetY == 1) {
                    nextTarget.put("x", 0);
                    nextTarget.put("y", 1);
                }
                break;

        }

        if (TicTacToeAi.targetHasBeenUsed(nextTarget, b)) {
            nextTarget = defend(r, b);
        }
        return nextTarget;
    }


    private ArrayList<Map<String, Integer>> getFreeTargetsList(Recorder r, Board b) {
        ArrayList<Map<String, Integer>> list = new ArrayList<>();
        Map<String, Integer> target1 = new HashMap<>();
        Map<String, Integer> target2 = new HashMap<>();
        int freeTarget1X = 0;
        int freeTarget1Y = 0;
        int freeTarget2X = 0;
        int freeTarget2Y = 0;
        int loop = 0;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                if (b.map[i][j] == 0) {
                    if (loop == 0) {
                        freeTarget1X = i;
                        freeTarget1Y = j;
                        ++loop;
                    } else {
                        freeTarget2X = i;
                        freeTarget2Y = j;
                    }
                }
            }
        }

        target1.put("x", freeTarget1X);
        target1.put("y", freeTarget1Y);
        target2.put("x", freeTarget2X);
        target2.put("y", freeTarget2Y);
        list.add(target1);
        list.add(target2);
        return list;
    }

    @Override
    public Map<String, Integer> defend(Recorder r, Board b) {
        Map<String, Integer> nextTarget = new HashMap<>();

        Map<String, Integer> fourthOppTarget = r.playerOneMoves.get(3);
        int fourthOppTargetX = fourthOppTarget.get("x");
        int fourthOppTargetY = fourthOppTarget.get("y");

        if (TicTacToeAi.opponentPlayedInCorners(r, 3)) {
            Corner corner = getCorner(fourthOppTargetX, fourthOppTargetY);
            switch (corner) {
                case DOWN_LEFT:
                    if (b.map[1][1] == 1) {
                        if (b.map[2][0] == 0) {
                            nextTarget.put("x", 2);
                            nextTarget.put("y", 0);
                        }
                    } else if (b.map[2][0] == 1) {
                        // TODO maybe this might never happen because of defend middle strategy
                        if (b.map[1][1] == 0) {
                            nextTarget.put("x", 1);
                            nextTarget.put("y", 1);
                        }
                    }
                    break;
                case UP_LEFT:
                    if (b.map[1][1] == 1) {
                        if (b.map[2][2] == 0) {
                            nextTarget.put("x", 2);
                            nextTarget.put("y", 2);
                        }
                    } else if (b.map[2][2] == 1) {
                        // TODO maybe this might never happen because of defend middle strategy
                        if (b.map[1][1] == 0) {
                            nextTarget.put("x", 1);
                            nextTarget.put("y", 1);
                        }
                    }
                    break;
                case UP_RIGHT:
                    if (b.map[1][1] == 1) {
                        if (b.map[0][2] == 0) {
                            nextTarget.put("x", 0);
                            nextTarget.put("y", 2);
                        }
                    } else if (b.map[0][2] == 1) {
                        // TODO maybe this might never happen because of defend middle strategy
                        if (b.map[1][1] == 0) {
                            nextTarget.put("x", 1);
                            nextTarget.put("y", 1);
                        }
                    }
                    break;
                case DOWN_RIGHT:
                    if (b.map[1][1] == 1) {
                        if (b.map[0][0] == 0) {
                            nextTarget.put("x", 0);
                            nextTarget.put("y", 0);
                        }
                    } else if (b.map[0][0] == 1) {
                        // TODO maybe this might never happen because of defend middle strategy
                        if (b.map[1][1] == 0) {
                            nextTarget.put("x", 1);
                            nextTarget.put("y", 1);
                        }
                    }
                    break;
                default:
                    nextTarget = null;
                    break;
            }
        } else {
            ArrayList<Map<String, Integer>> list = getFreeTargetsList(r, b);
            Map<String, Integer> firstFreeTarget = list.get(0);
            int freeTarget1X = firstFreeTarget.get("x");
            int freeTarget1Y = firstFreeTarget.get("y");

            Map<String, Integer> secondFreeTarget = list.get(1);
            int freeTarget2X = secondFreeTarget.get("x");
            int freeTarget2Y = secondFreeTarget.get("y");

            if ((fourthOppTargetX == freeTarget1X) || (fourthOppTargetY == freeTarget1Y)) {
                nextTarget.put("x", freeTarget1X);
                nextTarget.put("y", freeTarget1Y);
            } else if ((fourthOppTargetX == freeTarget2X) || (fourthOppTargetY == freeTarget2Y)) {
                nextTarget.put("x", freeTarget2X);
                nextTarget.put("y", freeTarget2Y);
            }
        }
        return nextTarget;
    }

    @Override
    public Map<String, Integer> attackCorners(Recorder r, Board b) {
        Map<String, Integer> nextTarget = new HashMap<>();
        Map<String, Integer> thirdTarget = r.playerTwoMoves.get(2);
        int thirdTargetX = thirdTarget.get("x");
        int thirdTargetY = thirdTarget.get("y");
        Corner corner = getCorner(thirdTargetX, thirdTargetY);
        switch (corner) {
            case DOWN_RIGHT:
                nextTarget.put("x", 0);
                nextTarget.put("y", 0);
                break;
            case UP_RIGHT:
                nextTarget.put("x", 0);
                nextTarget.put("y", 2);
                break;
            case UP_LEFT:
                nextTarget.put("x", 2);
                nextTarget.put("y", 2);
                break;
            case DOWN_LEFT:
                nextTarget.put("x", 2);
                nextTarget.put("y", 0);
                break;
        }
        if (TicTacToeAi.targetHasBeenUsed(nextTarget, b)) nextTarget.clear();

        return nextTarget;
    }
}
