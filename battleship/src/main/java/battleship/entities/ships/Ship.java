package battleship.entities.ships;
import battleship.entities.Board;
import net.sf.json.JSONObject;

import java.util.*;

import static org.apache.commons.lang.math.RandomUtils.nextInt;

public class Ship {
    public boolean hasSank;
    public int id;
    public int size;
    public int stemX;
    public int stemY;
    public int bowX;
    public int bowY;


    public Ship() {
        this.hasSank = false;
        this.id = 0;
        this.size = 0;
        this.stemX = 0;
        this.stemY = 0;
        this.bowX = 0;
        this.bowY = 0;
    }

    public Ship(int id, int size, int stemX, int stemY, int bowX, int bowY) {
        this.hasSank = false;
        this.id = id;
        this.size = size;
        this.stemX = stemX;
        this.stemY = stemY;
        this.bowX = bowX;
        this.bowY = bowY;
    }

//    public static Map<String, Ship> buildFleet(JSONObject options){
//        Map<String, Ship> fleet = new HashMap<>();
//        Iterator<String> keys = options.keys();
//        while(keys.hasNext()) {
//            String key = keys.next();
//            Ship buildedShip = buildShipFromJSON(options, key);
//            fleet.put(key, buildedShip);
//        }
//        return fleet;
//    }

    public static Map<String, Ship> buildFleetFromShips(Ship carrier, Ship battleship, Ship cruiser, Ship destroyer, Ship submarine){
        Map<String, Ship> fleet = new HashMap<>();
        fleet.put("carrier", carrier);
        fleet.put("battleship", battleship);
        fleet.put("cruiser", cruiser);
        fleet.put("destroyer", destroyer);
        fleet.put("submarine", submarine);
        return fleet;
    }

//    private static Ship buildShipFromJSON(JSONObject options, String key) {
//        JSONObject ship = options.getJSONObject(key);
//        JSONObject bow = ship.getJSONObject("bow");
//        JSONObject stem = ship.getJSONObject("stem");
//        int bowX = bow.getInt("x");
//        int bowY = bow.getInt("y");
//        int stemX = stem.getInt("x");
//        int stemY = stem.getInt("y");
//
//        switch(key) {
//            case "carrier":
//                return new Carrier(stemX, stemY, bowX, bowY);
//            case "submarine":
//                return new Submarine(stemX, stemY, bowX, bowY);
//            case "destroyer":
//                return new Destroyer(stemX, stemY, bowX, bowY);
//            case "cruiser":
//                return new Cruiser(stemX, stemY, bowX, bowY);
//            case "battleship":
//                return new Battleship(stemX, stemY, bowX, bowY);
//            default:
//                return null;
//        }
//    }

    public Ship generateRandomPosition(Board board) {
        Random random = new Random();
        this.stemX = random.nextInt(board.getWidth());
        this.stemY = random.nextInt(board.getHeight());
        boolean horizontalDirection = random.nextBoolean();
        boolean hasValidPosition;
        if(horizontalDirection){
            this.bowY = stemY;
            this.bowX = stemX + this.size - 1 ;
            if(this.bowX > 9)generateRandomPosition(board);
            hasValidPosition = board.validateRow(this);
        } else {
            this.bowY = stemY + this.size - 1;
            this.bowX = stemX;
            if(this.bowY > 9)generateRandomPosition(board);
            hasValidPosition = board.validateCol(this);
        }
        if(!hasValidPosition)generateRandomPosition(board);
        return this;
    }

    // Getters & Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
    public boolean isHasSank() {
        return hasSank;
    }
    public void setHasSank(boolean hasSank) {
        this.hasSank = hasSank;
    }
    public int getStemX() {
        return stemX;
    }
    public void setStemX(int stemX) {
        this.stemX = stemX;
    }
    public int getStemY() {
        return stemY;
    }
    public void setStemY(int stemY) {
        this.stemY = stemY;
    }
    public int getBowX() {
        return bowX;
    }
    public void setBowX(int bowX) {
        this.bowX = bowX;
    }
    public int getBowY() {
        return bowY;
    }
    public void setBowY(int bowY) {
        this.bowY = bowY;
    }

}
