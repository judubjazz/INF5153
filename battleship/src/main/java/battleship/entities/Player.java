package battleship.entities;

import battleship.entities.ships.*;
import net.sf.json.JSONObject;
import java.util.Map;

public class Player {
    public Ship carrier;
    public Ship submarine;
    public Ship destroyer;
    public Ship battleship;
    public Ship cruiser;
    public Grid playerGrid;
    public Grid ennemyGrid;
    public JSONObject fleet;
    public int targetX;
    public int targetY;
    public int shipsRemaining;

    public Player () {
        this.carrier = new Carrier();
        this.submarine = new Submarine();
        this.destroyer = new Destroyer();
        this.battleship = new Battleship();
        this.cruiser = new Cruiser();
        this.playerGrid = new Grid();
        this.ennemyGrid = new Grid();
        this.shipsRemaining = 17;
    }

    public Player(JSONObject fleet, Grid playerGrid, Grid ennemyGrid) {
        this.carrier = (Ship) fleet.get("carrier");
        this.submarine = (Ship) fleet.get("submarine");
        this.destroyer = (Ship) fleet.get("destroyer");
        this.battleship = (Ship) fleet.get("battleship");
        this.cruiser = (Ship) fleet.get("cruiser");
        this.fleet = fleet;
    }

    public Player(Map fleet, Grid playerGrid, Grid ennemyGrid) {
        this.carrier = (Ship) fleet.get("carrier");
        this.submarine = (Ship) fleet.get("submarine");
        this.destroyer = (Ship) fleet.get("destroyer");
        this.battleship = (Ship) fleet.get("battleship");
        this.cruiser = (Ship) fleet.get("cruiser");
        this.playerGrid = playerGrid;
        this.ennemyGrid = ennemyGrid;
        this.shipsRemaining = 17;
        ennemyGrid.setHidden(true);
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
    public Grid getPlayerGrid() {
        return playerGrid;
    }
    public void setPlayerGrid(Grid playerGrid) {
        this.playerGrid = playerGrid;
    }
    public Grid getEnnemyGrid() {
        return ennemyGrid;
    }
    public void setEnnemyGrid(Grid ennemyGrid) {
        this.ennemyGrid = ennemyGrid;
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
}
