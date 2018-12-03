package db;

import battleship.entities.games.TicTacToeGame;
import com.mongodb.*;

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

    public void save(TicTacToeGame battleshipGame) {
        DBCollection collection = getCollection("games");
        DBObject game = new BasicDBObject("_id", "jo")
                .append("name", "Jo Bloggs")
                .append("address", new BasicDBObject("street", "123 Fake St")
                        .append("city", "Faketon")
                        .append("state", "MA")
                        .append("zip", 12345));
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
