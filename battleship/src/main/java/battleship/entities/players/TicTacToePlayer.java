package battleship.entities.players;

import battleship.entities.boards.BattleshipBoard;
import battleship.entities.boards.TicTacToeBoard;
import battleship.entities.ships.*;

import java.util.Map;

public class TicTacToePlayer extends Player {
    public Map<String, Integer> marks;
    public char sign;

    public TicTacToePlayer() {super();}

    public TicTacToePlayer(String name) {
        super(name);
        this.playerBoard = new BattleshipBoard();
    }


    public TicTacToePlayer(Map<String,Integer> marks, TicTacToeBoard playerBoard, TicTacToeBoard ennemyBoard, String name, char sign) {
        super(name, playerBoard, null);
        this.marks = marks;
        this.sign = sign;
    }

    // Getters & Setters


    public Map<String, Integer> getMarks() {
        return marks;
    }

    public void setMarks(Map<String, Integer> marks) {
        this.marks = marks;
    }

    public char getSign() {
        return sign;
    }

    public void setSign(char sign) {
        this.sign = sign;
    }
}
