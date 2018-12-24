package battleship.factories;

import battleship.entities.Ais.TicTacToeAi;
import battleship.entities.Recorder;
import battleship.entities.boards.TicTacToeBoard;
import battleship.entities.games.TicTacToeGame;
import battleship.entities.players.TicTacToePlayer;
import battleship.middlewares.converters.StringTo2DArrayConverter;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TicTacToeGameCreator implements GameFactory {
    @Override
    public TicTacToeGame createGame(JSONObject data) {

        TicTacToeGame battleshipGame = new TicTacToeGame();
        TicTacToePlayer playerOne = new TicTacToePlayer();
        TicTacToePlayer playerTwo = new TicTacToePlayer();
        TicTacToeAi ai = new TicTacToeAi();

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
                    break;
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
                        TicTacToeAi.State state = ai.stringToState(stringState);
                        ai.state = state;

                        String stringStrategy = aiData.getString("strategy");
                        TicTacToeAi.Strategy strategy = ai.stringToStrategy(stringStrategy);
                        ai.strategy = strategy;

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
//                    TicTacToeGame m = createGame(memento);
//                    battleshipGame.memento = m;
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


    private static void buildPlayerFromJSON(TicTacToePlayer player, JSONObject playerData){
        Iterator<?> playerKeys = playerData.keys();
        while (playerKeys.hasNext()){
            String playerKey = (String) playerKeys.next();
            switch (playerKey){
                case "name":
                    String playerOneName = playerData.getString(playerKey);
                    player.name = playerOneName;
                    break;
                case "marks":
                    JSONObject playerMarks = playerData.getJSONObject(playerKey);
                    if(playerMarks.size() > 0){
                        Iterator<?> marksKeys = playerMarks.keys();
                        while (marksKeys.hasNext()) {
                            String key = (String) marksKeys.next();
                        }
                    }
                    break;
                case "map":
                    StringTo2DArrayConverter c = new StringTo2DArrayConverter();
                    c.arraySize = 3;
                    String stringMap = playerData.getString(playerKey);
                    int[][] map  = c.convert(stringMap);
                    player.playerBoard = new TicTacToeBoard();
                    player.playerBoard.map = map;
                    break;
                case "target":
                    JSONObject target = playerData.getJSONObject("target");
                    int targetX = target.getInt("x");
                    int targetY = target.getInt("y");
                    player.targetX = targetX;
                    player.targetY = targetY;
                    break;
                case "sign":
                    int sign = playerData.getInt("sign");;
                    player.sign = sign;
                    break;
            }
        }
    }
    
    @Override
    public Recorder buildRecorderFromJSONObject(JSONObject recorderData) {
        Recorder recorder = new Recorder();
        try {
            JSONArray p1recorder = recorderData.getJSONArray("playerOneMoves");
            for (int i = 0; i < p1recorder.size(); i++) {
                JSONObject item = p1recorder.getJSONObject(i);
                Map<String, Integer> map = new HashMap<>();
                int targetX = item.getInt("x");
                int targetY = item.getInt("y");
                map.put("x", targetX);
                map.put("y", targetY);
                recorder.playerOneMoves.add(map);
            }

            JSONArray p2recorder = recorderData.getJSONArray("playerTwoMoves");
            for (int i = 0; i < p2recorder.size(); i++) {
                JSONObject item = p2recorder.getJSONObject(i);
                Map<String, Integer> map = new HashMap<>();
                int targetX = item.getInt("x");
                int targetY = item.getInt("y");
                map.put("x", targetX);
                map.put("y", targetY);
                recorder.playerTwoMoves.add(map);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        int index = recorderData.getInt("index");
        recorder.index = index;

        return recorder;
    }
}
