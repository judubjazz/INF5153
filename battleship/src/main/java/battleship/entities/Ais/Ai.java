package battleship.entities.Ais;

import java.util.Map;


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
