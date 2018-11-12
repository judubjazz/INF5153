package battleship.entities;

import battleship.entities.ships.*;
import java.util.Map;

public class Player {
    public Ship carrier;
    public Ship submarine;
    public Ship destroyer;
    public Ship battleship;
    public Ship cruiser;
    public Board playerBoard;
    public Board ennemyBoard;
    public String name;
    public Map<String, Ship> fleet;
    public int targetX;
    public int targetY;
    public int shipsRemaining;
    public boolean winner;

    public Player () {
        this.carrier = new Carrier();
        this.submarine = new Submarine();
        this.destroyer = new Destroyer();
        this.battleship = new Battleship();
        this.cruiser = new Cruiser();
        this.playerBoard = new Board();
        this.ennemyBoard = new Board();
        this.shipsRemaining = 17;
        this.winner = false;
    }
    public Player (String name) {
        this.carrier = new Carrier();
        this.submarine = new Submarine();
        this.destroyer = new Destroyer();
        this.battleship = new Battleship();
        this.cruiser = new Cruiser();
        this.playerBoard = new Board();
        this.ennemyBoard = new Board();
        this.shipsRemaining = 17;
        this.name = name;
        this.winner = false;
    }

    public Player(Map fleet, Board playerBoard, Board ennemyBoard, String name) {
        this.carrier = (Ship) fleet.get("carrier");
        this.submarine = (Ship) fleet.get("submarine");
        this.destroyer = (Ship) fleet.get("destroyer");
        this.battleship = (Ship) fleet.get("battleship");
        this.cruiser = (Ship) fleet.get("cruiser");
        this.playerBoard = playerBoard;
        this.ennemyBoard = ennemyBoard;
        this.shipsRemaining = 17;
        this.fleet = fleet;
        this.name = name;
        this.winner = false;
        ennemyBoard.hidden = true;
    }

    // Getters & Setters
    public Ship getCarrier() {
        return carrier;
    }
    public void setCarrier(Ship carrier) {
        this.carrier = carrier;
    }
    public Ship getSubmarine() {
        return submarine;
    }
    public void setSubmarine(Ship submarine) {this.submarine = submarine;}
    public Ship getDestroyer() {
        return destroyer;
    }
    public void setDestroyer(Ship destroyer) {
        this.destroyer = destroyer;
    }
    public Ship getBattleship() {
        return battleship;
    }
    public void setBattleship(Ship battleship) {this.battleship = battleship;}
    public Ship getCruiser() {
        return cruiser;
    }
    public void setCruiser(Ship cruiser) {
        this.cruiser = cruiser;
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
    public int getShipsRemaining() {
        return shipsRemaining;
    }
    public void setShipsRemaining(int shipsRemaining) {
        this.shipsRemaining = shipsRemaining;
    }
    public Map<String, Ship> getFleet() {return fleet; }
    public void setFleet(Map<String, Ship> fleet) {this.fleet = fleet; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }
}
