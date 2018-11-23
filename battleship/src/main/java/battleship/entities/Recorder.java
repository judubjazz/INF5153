package battleship.entities;

import java.util.ArrayList;
import java.util.Map;

public class Recorder {
    public ArrayList<Map<String, Integer>> playerOneMoves;
    public ArrayList<Map<String, Integer>> playerTwoMoves;
    public int index;

    public Recorder() {}

    public Recorder(ArrayList<Map<String, Integer>> playerOneMoves, ArrayList<Map<String, Integer>> playerTwoMoves) {
        this.playerOneMoves = playerOneMoves;
        this.playerTwoMoves = playerTwoMoves;
    }

    // GETTERS & SETTERS
    public ArrayList<Map<String, Integer>> getPlayerOneMoves() {return playerOneMoves; }
    public void setPlayerOneMoves(ArrayList<Map<String, Integer>> playerOneMoves) {this.playerOneMoves = playerOneMoves; }
    public ArrayList<Map<String, Integer>> getPlayerTwoMoves() {return playerTwoMoves; }
    public void setPlayerTwoMoves(ArrayList<Map<String, Integer>> playerTwoMoves) {this.playerTwoMoves = playerTwoMoves; }
    public int getIndex() {return index; }
    public void setIndex(int index) {this.index = index;}
}
