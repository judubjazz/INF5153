package battleship.factories;

import battleship.entities.Ais.BattleshipAi;
import battleship.entities.Recorder;
import battleship.entities.boards.BattleshipBoard;
import battleship.entities.games.BattleshipGame;
import battleship.entities.players.BattleshipPlayer;
import battleship.entities.ships.*;
import battleship.middlewares.converters.StringTo2DArrayConverter;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BattleshipGameCreator implements GameFactory {
    @Override
    public BattleshipGame createGame(JSONObject data) {
        System.out.println(data);
        BattleshipGame battleshipGame = new BattleshipGame();
        BattleshipPlayer playerOne = new BattleshipPlayer();
        BattleshipPlayer playerTwo = new BattleshipPlayer();
        BattleshipAi ai = new BattleshipAi();

        Iterator<?> gameKeys = data.keys();
        while (gameKeys.hasNext()) {
            String gameKey = (String) gameKeys.next();
            switch (gameKey) {
                case "id":
                    int gameID = data.getInt(gameKey);
                    battleshipGame.id = gameID;
                    break;
                case "name":
                    String gameName = data.getString(gameKey);
                    battleshipGame.name = gameName;
                    break;
                case "playerOne":
                    JSONObject playerOneData = data.getJSONObject(gameKey);
                    buildPlayerFromJSON(playerOne, playerOneData);
                case "playerTwo":
                    JSONObject playerTwoData = data.getJSONObject(gameKey);
                    buildPlayerFromJSON(playerTwo, playerTwoData);
                    break;
                case "ai":
                    JSONObject aiData = data.getJSONObject(gameKey);

                    boolean difficulty = aiData.getBoolean("difficulty");
                    ai.difficulty = difficulty;

                    if(difficulty){
                        String stringState = aiData.getString("state");
                        BattleshipAi.State state = ai.stringToState(stringState);
                        ai.state = state;

                        String stringStartPosition = aiData.getString("startPosition");
                        Map<String, Integer> startPosition = new HashMap<>();
                        if(stringStartPosition.length() > 2){
                            JSONObject startPositionData = JSONObject.fromObject(stringStartPosition);
                            startPosition.put("x", startPositionData.getInt("x"));
                            startPosition.put("y", startPositionData.getInt("y"));
                        }
                        ai.startPosition = startPosition;
                    }

                    break;
                case "recorder":
                    JSONObject recorderData = data.getJSONObject(gameKey);
                    Recorder recorder = buildRecorderFromJSONObject(recorderData);
                    battleshipGame.recorder = recorder;
                    break;
                case "memento":
                    JSONObject memento = data.getJSONObject(gameKey);
                    BattleshipGame m = createGame(memento);
                    battleshipGame.memento = m;
            }
        }

        battleshipGame.setPlayerOne(playerOne);
        battleshipGame.setPlayerTwo(playerTwo);
        battleshipGame.playerOne.winner = battleshipGame.playerTwo.winner = false;
        playerOne.ennemyBoard= playerTwo.playerBoard;
        playerTwo.ennemyBoard = playerOne.playerBoard;
        battleshipGame.ai = ai;

        return battleshipGame;
    }

    private static void buildPlayerFromJSON(BattleshipPlayer player, JSONObject playerData){
        Iterator<?> playerKeys = playerData.keys();
        while (playerKeys.hasNext()){
            String playerKey = (String) playerKeys.next();
            switch (playerKey){
                case "name":
                    String playerOneName = playerData.getString(playerKey);
                    player.name = playerOneName;
                    break;
                case "fleet":
                    JSONObject playerFleet = playerData.getJSONObject(playerKey);
                    Iterator<?> fleetKeys = playerFleet.keys();
                    while (fleetKeys.hasNext()) {
                        String fleetKey = (String) fleetKeys.next();
                        buildShipFromJSON(player, playerFleet, fleetKey);
                    }
                    break;
                case "map":
                    StringTo2DArrayConverter c = new StringTo2DArrayConverter();
                    String stringMap = playerData.getString(playerKey);
                    int[][] map  = c.convert(stringMap);
                    player.playerBoard = new BattleshipBoard();
                    player.playerBoard.map = map;
                    break;
                case "target":
                    JSONObject target = playerData.getJSONObject("target");
                    int targetX = target.getInt("x");
                    int targetY = target.getInt("y");
                    player.targetX = targetX;
                    player.targetY = targetY;
                    break;
                case "shipsRemaining":
                    int p1ShipsRemaining = playerData.getInt(playerKey);
                    player.shipsRemaining = p1ShipsRemaining;
                    break;
            }
        }

    }
    private static void buildShipFromJSON(BattleshipPlayer player, JSONObject data, String key) {
        JSONObject ship = data.getJSONObject(key);
        JSONObject bow = ship.getJSONObject("bow");
        JSONObject stem = ship.getJSONObject("stem");
        int bowX = bow.getInt("x");
        int bowY = bow.getInt("y");
        int stemX = stem.getInt("x");
        int stemY = stem.getInt("y");

        switch (key) {
            case "carrier":
                player.carrier = new Carrier(stemX, stemY, bowX, bowY);
            case "submarine":
                player.submarine = new Submarine(stemX, stemY, bowX, bowY);
            case "destroyer":
                player.destroyer = new Destroyer(stemX, stemY, bowX, bowY);
            case "cruiser":
                player.cruiser = new Cruiser(stemX, stemY, bowX, bowY);
            case "battleship":
                player.battleship = new Battleship(stemX, stemY, bowX, bowY);
        }
    }

    @Override
    public Recorder buildRecorderFromJSONObject(JSONObject recorderData){
        Recorder recorder = new Recorder();
        try {
            JSONArray p1recorder = recorderData.getJSONArray("playerOneMoves");
            for(int i=0;i<p1recorder.size();i++) {
                JSONObject item = p1recorder.getJSONObject(i);
                Map<String, Integer> map = new HashMap<>();
                int targetArea = item.getInt("hit");
                int targetX = item.getInt("x");
                int targetY = item.getInt("y");
                map.put("hit", targetArea);
                map.put("x", targetX);
                map.put("y", targetY);
                recorder.playerOneMoves.add(map);
            }

            JSONArray p2recorder = recorderData.getJSONArray("playerTwoMoves");
            for(int i=0;i<p2recorder.size();i++) {
                JSONObject item = p2recorder.getJSONObject(i);
                Map<String, Integer> map = new HashMap<>();
                int targetArea = item.getInt("hit");
                int targetX = item.getInt("x");
                int targetY = item.getInt("y");
                map.put("hit", targetArea);
                map.put("x", targetX);
                map.put("y", targetY);
                recorder.playerTwoMoves.add(map);
            }
        } catch (JSONException e){
            e.printStackTrace();
            return  null;
        }
        int index = recorderData.getInt("index");
        recorder.index = index;

        return recorder;
    }
}