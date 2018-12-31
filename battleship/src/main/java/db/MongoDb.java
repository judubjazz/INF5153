package db;

import battleship.entities.games.TicTacToeGame;
import com.mongodb.*;
import battleship.middlewares.converters.toStringConverters;

public class MongoDb extends Db<TicTacToeGame> {
    static private MongoDb db= null;
    static private MongoClient mongoClient = null;

    private MongoDb() {
        if(mongoClient == null){
            try {
                mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static MongoDb getDb() {
        if (db == null) {
            db = new MongoDb();
        }
        return db;
    }

    private DB getMongoDB(String name){
        return mongoClient.getDB(name);
    }

    private DBCollection getCollection(String name){
        return getMongoDB("inf5153").getCollection(name);
    }

    public void save(TicTacToeGame ticTacToeGame) {
        DBCollection collection = getCollection("games");
        String id = String.valueOf(ticTacToeGame.id);
        String map1 = toStringConverters.mapToString(ticTacToeGame.playerOne.playerBoard.map);
        String map2 = toStringConverters.mapToString(ticTacToeGame.playerTwo.playerBoard.map);
        String playerOneMoves = ticTacToeGame.recorder.playerOneMoves.toString();
        String playerTwoMoves = ticTacToeGame.recorder.playerTwoMoves.toString();
        String state = String.valueOf(ticTacToeGame.ai.state);
        String strategy = String.valueOf(ticTacToeGame.ai.strategy);

        BasicDBObject playerOne = new BasicDBObject("name", "playerOne")
                .append("id", 1)
                .append("map", map1)
                .append("sign", 1);
        BasicDBObject playerTwo = new BasicDBObject("name", "playerTwo")
                .append("id", 2)
                .append("map", map2)
                .append("sign", -1);
        BasicDBObject ai = new BasicDBObject("state", state)
                .append("strategy", strategy)
                .append("difficulty", ticTacToeGame.ai.difficulty);
        BasicDBObject recorder = new BasicDBObject("playerOneMoves", playerOneMoves)
                .append("playerTwoMoves", playerTwoMoves)
                .append("index", ticTacToeGame.recorder.index);
        DBObject game = new BasicDBObject("_id", id)
                .append("name", ticTacToeGame.name)
                .append("playerOne", playerOne)
                .append("playerTwo", playerTwo)
                .append("ai", ai)
                .append("recorder", recorder);
        collection.insert(game);

    }

    public void load(int gameID) {
        DBCollection collection = getCollection("games");
        DBObject query = new BasicDBObject("_id", gameID);
        DBCursor cursor = collection.find(query);;
        DBObject game = cursor.one();

    }

    @Override
    public void load(TicTacToeGame game) {

    }
}
