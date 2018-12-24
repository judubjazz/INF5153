package battleship.entities.Ais.Strategy;

import battleship.entities.Recorder;
import battleship.entities.boards.Board;

import java.util.HashMap;
import java.util.Map;

public class FirstStrategy extends Strategy  implements StrategyController{

    @Override
    public boolean hasToDefend(Board b) {
        return false;
    }

    @Override
    public Map<String, Integer> defendCorners(Recorder r, Board b) {
        Map<String, Integer> nextTarget = new HashMap<>();
        // cpu play in the middle because its the best way to defend
        nextTarget.put("x", 1);
        nextTarget.put("y", 1);
        return nextTarget;
    }

    @Override
    public Map<String, Integer> defendMiddle(Recorder r, Board b) {
        Map<String, Integer> nextTarget = new HashMap<>();
        // cpu play in a left upper corner because its the best way to attack
        nextTarget.put("x", 0);
        nextTarget.put("y", 0);
        return nextTarget;
    }

    @Override
    public Map<String, Integer> attack(Recorder r, Board b) {
        Map<String, Integer> nextTarget = new HashMap<>();
        // cpu play in a left upper corner because its the best way to attack
        nextTarget.put("x", 0);
        nextTarget.put("y", 0);
        return nextTarget;
    }

    @Override
    public Map<String, Integer> defend(Recorder r, Board b) {
        return null;
    }

    @Override
    public Map<String, Integer> attackCorners(Recorder r, Board b) {
        return null;
    }
}
