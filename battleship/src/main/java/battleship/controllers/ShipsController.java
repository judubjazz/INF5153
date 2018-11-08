package battleship.controllers;

import battleship.entities.ships.*;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ShipsController {
    public static Map<String, Ship> buildFleet(JSONObject options){
        Map<String, Ship> fleet = new HashMap<>();
        Iterator<String> keys = options.keys();
        while(keys.hasNext()) {
            String key = keys.next();
            Ship buildedShip = buildShipFromJSON(options, key);
            fleet.put(key, buildedShip);
        }
        return fleet;
    }

    private static Ship buildShipFromJSON(JSONObject options, String key) {
        JSONObject ship = options.getJSONObject(key);
        JSONObject bow = ship.getJSONObject("bow");
        JSONObject stem = ship.getJSONObject("stem");
        int bowX = bow.getInt("x");
        int bowY = bow.getInt("y");
        int stemX = stem.getInt("x");
        int stemY = stem.getInt("y");

        switch(key) {
            case "carrier":
                return new Carrier(stemX, stemY, bowX, bowY);
            case "submarine":
                return new Submarine(stemX, stemY, bowX, bowY);
            case "destroyer":
                return new Destroyer(stemX, stemY, bowX, bowY);
            case "cruiser":
                return new Cruiser(stemX, stemY, bowX, bowY);
            case "battleship":
                return new Battleship(stemX, stemY, bowX, bowY);
            default:
                return null;
        }
    }
}
