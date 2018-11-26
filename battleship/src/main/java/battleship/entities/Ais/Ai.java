package battleship.entities.Ais;

import battleship.entities.boards.Board;
import battleship.entities.games.Game;
import battleship.entities.ships.*;
import battleship.middlewares.Validation;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Ai {
    public boolean difficulty;
    public Map<String, Integer> startPosition;

    public Ai() {}

    public Ai(boolean difficulty, Map<String, Integer> startPosition) {
        this.difficulty = difficulty;
        this.startPosition = startPosition;
    }

    public boolean isDifficulty() {
        return difficulty;
    }

    public void setDifficulty(boolean difficulty) {
        this.difficulty = difficulty;
    }

    public Map<String, Integer> getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Map<String, Integer> startPosition) {
        this.startPosition = startPosition;
    }

}
