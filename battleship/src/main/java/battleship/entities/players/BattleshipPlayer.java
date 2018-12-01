package battleship.entities.players;

import battleship.entities.boards.BattleshipBoard;
import battleship.entities.ships.*;

import java.util.Map;

public class BattleshipPlayer extends Player {
    public Ship carrier;
    public Ship submarine;
    public Ship destroyer;
    public Ship battleship;
    public Ship cruiser;
    public Map<String, Ship> fleet;
    public int shipsRemaining;


    public BattleshipPlayer() {super();}

    public BattleshipPlayer(String name) {
        super(name);
        this.carrier = new Carrier();
        this.submarine = new Submarine();
        this.destroyer = new Destroyer();
        this.battleship = new Battleship();
        this.cruiser = new Cruiser();
        this.playerBoard = new BattleshipBoard();
        this.ennemyBoard = new BattleshipBoard();
        this.shipsRemaining = 17;
    }


    public BattleshipPlayer(Map<String,Ship> fleet, BattleshipBoard playerBoard, BattleshipBoard ennemyBoard, String name) {
        super(name, playerBoard, ennemyBoard);
        this.carrier = fleet.get("carrier");
        this.submarine = fleet.get("submarine");
        this.destroyer = fleet.get("destroyer");
        this.battleship = fleet.get("battleship");
        this.cruiser = fleet.get("cruiser");
        this.shipsRemaining = 17;
        this.fleet = fleet;
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

    public void setSubmarine(Ship submarine) {
        this.submarine = submarine;
    }

    public Ship getDestroyer() {
        return destroyer;
    }

    public void setDestroyer(Ship destroyer) {
        this.destroyer = destroyer;
    }

    public Ship getBattleship() {
        return battleship;
    }

    public void setBattleship(Ship battleship) {
        this.battleship = battleship;
    }

    public Ship getCruiser() {
        return cruiser;
    }

    public void setCruiser(Ship cruiser) {
        this.cruiser = cruiser;
    }

    public Map<String, Ship> getFleet() {
        return fleet;
    }

    public void setFleet(Map<String, Ship> fleet) {
        this.fleet = fleet;
    }

    public int getShipsRemaining() {
        return shipsRemaining;
    }

    public void setShipsRemaining(int shipsRemaining) {
        this.shipsRemaining = shipsRemaining;
    }
}
