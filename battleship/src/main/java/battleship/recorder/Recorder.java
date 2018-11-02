package battleship.recorder;

import java.util.ArrayList;
import java.util.Map;

public class Recorder {
    public ArrayList<Map<String, Integer>> playerOneMoves;
    public ArrayList<Map<String, Integer>> playerTwoMoves;

    public Recorder() {

    }

    public Recorder(ArrayList<Map<String, Integer>> playerOneMoves, ArrayList<Map<String, Integer>> playerTwoMoves) {
        this.playerOneMoves = playerOneMoves;
        this.playerTwoMoves = playerTwoMoves;
    }

    public ArrayList<Map<String, Integer>> getPlayerOneMoves() {
        return playerOneMoves;
    }

    public void setPlayerOneMoves(ArrayList<Map<String, Integer>> playerOneMoves) {
        this.playerOneMoves = playerOneMoves;
    }

    public ArrayList<Map<String, Integer>> getPlayerTwoMoves() {
        return playerTwoMoves;
    }

    public void setPlayerTwoMoves(ArrayList<Map<String, Integer>> playerTwoMoves) {
        this.playerTwoMoves = playerTwoMoves;
    }
}
