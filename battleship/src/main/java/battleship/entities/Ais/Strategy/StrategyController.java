package battleship.entities.Ais.Strategy;

import battleship.entities.Recorder;
import battleship.entities.boards.Board;

import java.util.Map;

public interface StrategyController {
    boolean hasToDefend(Board b);
    Map<String, Integer> defendCorners(Recorder r, Board b);
    Map<String, Integer> defendMiddle(Recorder r, Board b);
    Map<String, Integer> attack(Recorder r, Board b);
    Map<String, Integer> attackCorners(Recorder r, Board b);
    Map<String, Integer> defend(Recorder r, Board b);
}
