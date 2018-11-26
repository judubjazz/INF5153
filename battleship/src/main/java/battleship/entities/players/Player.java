package battleship.entities.players;

import battleship.entities.boards.BattleshipBoard;
import battleship.entities.boards.Board;
import battleship.entities.ships.*;
import java.util.Map;

public class Player {
    public String name;
    public int targetX;
    public int targetY;
    public boolean winner;
    public Board playerBoard;
    public Board ennemyBoard;


    public Player () {}

    public Player (String name) {
        this.name = name;
        this.winner = false;
    }

    public Player (String name, Board playerBoard, Board ennemyBoard) {
        this.name = name;
        this.winner = false;
        this.playerBoard = playerBoard;
        this.ennemyBoard = ennemyBoard;
    }

    // Getters & Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTargetX() {
        return targetX;
    }

    public void setTargetX(int targetX) {
        this.targetX = targetX;
    }

    public int getTargetY() {
        return targetY;
    }

    public void setTargetY(int targetY) {
        this.targetY = targetY;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public Board getPlayerBoard() {
        return playerBoard;
    }

    public void setPlayerBoard(Board playerBoard) {
        this.playerBoard = playerBoard;
    }

    public Board getEnnemyBoard() {
        return ennemyBoard;
    }

    public void setEnnemyBoard(Board ennemyBoard) {
        this.ennemyBoard = ennemyBoard;
    }
}
