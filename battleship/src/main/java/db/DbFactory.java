package db;

public interface DbFactory {
    static Db getDb(String key){
        switch (key){
            case "battleship":
                return XMLDb.getXMLDb();
            case "tictactoe":
                return MongoDb.getDb();
            default:
                return null;
        }
    }


}
