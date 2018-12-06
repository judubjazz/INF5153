package db;

import battleship.entities.*;
import battleship.entities.Ais.BattleshipAi;
import battleship.entities.games.BattleshipGame;
import battleship.entities.players.BattleshipPlayer;
import battleship.entities.ships.Ship;
import battleship.middlewares.converters.StringTo2DArrayConverter;
import battleship.middlewares.converters.StringToArrayListConverter;
import battleship.middlewares.converters.toStringConverters;

import net.sf.json.JSONObject;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.nio.file.Paths;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// TODO in the XMLDb.xml file, set an attribute to game instead of an inner element
public class XMLDb extends Db<BattleshipGame>{
    static private XMLDb XMLDb = null;
    private Document document;
    private String path;

    private XMLDb() {
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        String path = (s + "/src/main/java/db/db.xml");
        this.path = path;
        this.document = parseXmlDocument(path);
    }

    public static XMLDb getXMLDb() {
        if (XMLDb == null) {
            XMLDb = new XMLDb();
        }
        return XMLDb;
    }

    private Document parseXmlDocument(String documentFilePath) {
        try {
            DocumentBuilderFactory documentFactory = initializeDocumentFactory();
            DocumentBuilder parser = documentFactory.newDocumentBuilder();
            Document document = parser.parse(documentFilePath);
            return document;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private DocumentBuilderFactory initializeDocumentFactory() {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        documentFactory.setIgnoringComments(true);
        documentFactory.setCoalescing(true);
        documentFactory.setNamespaceAware(true);
        documentFactory.setValidating(true);
        return documentFactory;
    }

    @Override
    public void save(BattleshipGame battleshipGame) {
        try {
            saveGame(battleshipGame);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void load(BattleshipGame battleshipGame) {
        NodeList gameSettings = getGameNodeListById(battleshipGame.id);
        setGame(gameSettings, battleshipGame);
    }

    private NodeList getGameNodeListById(int id) {
        try {
            NodeList games = document.getElementsByTagName("game");
            for (int j = 0; j < games.getLength(); ++j) {
                Node game = games.item(j);
                NodeList gameSettings = game.getChildNodes();
                for (int i = 0; i < gameSettings.getLength(); i++) {
                    Node gameSetting = gameSettings.item(i);
                    if (gameSetting.getNodeType() == Node.ELEMENT_NODE && gameSetting.getNodeName().equals("id")) {
                        String gameID = gameSetting.getTextContent();
                        if (gameID.equals(String.valueOf(id))) {
                            return gameSettings;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private void setAi(NodeList aiSettings, BattleshipAi ai) {
        for (int i = 0; i < aiSettings.getLength(); i++) {
            Node setting = aiSettings.item(i);
            if (setting.getNodeType() == Node.ELEMENT_NODE) {
                String text;
                switch (setting.getNodeName()) {
                    case "difficulty":
                        ai.difficulty = Boolean.parseBoolean(setting.getTextContent());
                        break;
                    case "startPosition":
                        text = setting.getTextContent();
                        JSONObject jo = JSONObject.fromObject(text);
                        if (jo.size() > 0){
                            Map<String, Integer> startPosition = new HashMap<>();
                            startPosition.put("x", jo.getInt("x"));
                            startPosition.put("y", jo.getInt("y"));
                            ai.startPosition = startPosition;
                        }
                        break;
                    case "state":
                        text = setting.getTextContent();
                        if (text.length() > 0) ai.state = ai.stringToState(text);
                        break;
                }
            }
        }
    }

    private void setRecorder(NodeList recorderSettings, Recorder recorder) {
        for (int i = 0; i < recorderSettings.getLength(); i++) {
            Node setting = recorderSettings.item(i);
            if (setting.getNodeType() == Node.ELEMENT_NODE) {
                String text;
                ArrayList<Map<String, Integer>> playerMoves;
                StringToArrayListConverter c = new StringToArrayListConverter();
                switch (setting.getNodeName()) {
                    case "playerOneMoves":
                        text = setting.getTextContent();
                        playerMoves = c.convert(text);
                        recorder.playerOneMoves = playerMoves;
                        break;
                    case "playerTwoMoves":
                        text = setting.getTextContent();
                        playerMoves = c.convert(text);
                        recorder.playerTwoMoves = playerMoves;
                        break;
                    case "index":
                        text = setting.getTextContent();
                        recorder.index = Integer.parseInt(text);
                        break;
                }
            }
        }
    }

    private void setGame(NodeList gameSettings, BattleshipGame battleshipGame) {
        for (int i = 0; i < gameSettings.getLength(); i++) {
            Node setting = gameSettings.item(i);
            if (setting.getNodeType() == Node.ELEMENT_NODE) {
                NodeList caseSettings = setting.getChildNodes();
                switch (setting.getNodeName()) {
                    case "id":
                        battleshipGame.id = Integer.parseInt(setting.getTextContent());
                        break;
                    case "playerOne":
                        setPlayer(caseSettings, battleshipGame.playerOne, battleshipGame);
                        break;
                    case "playerTwo":
                        setPlayer(caseSettings, battleshipGame.playerTwo, battleshipGame);
                        break;
                    case "ai":
                        setAi(caseSettings, battleshipGame.ai);
                        break;
                    case "recorder":
                        setRecorder(caseSettings, battleshipGame.recorder);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void setPlayer(NodeList playerSettings, BattleshipPlayer player, BattleshipGame battleshipGame) {
        for (int j = 0; j < playerSettings.getLength(); ++j) {
            Node setting = playerSettings.item(j);
            if (setting.getNodeType() == Node.ELEMENT_NODE) {
                switch (setting.getNodeName()) {
                    case "shipsRemaining":
                        int shipsRemaining = Integer.parseInt(setting.getTextContent());
                        player.shipsRemaining = shipsRemaining;
                        break;
                    case "map":
                        // TODO create a builder setMap()
                        String mapToString = setting.getTextContent();
                        StringTo2DArrayConverter c= new StringTo2DArrayConverter();
                        int[][] map = c.convert(mapToString);
                        if (player.name.equals("playerOne")) {
                            battleshipGame.playerOne.playerBoard.map = battleshipGame.playerTwo.ennemyBoard.map = map;
                        } else {
                            battleshipGame.playerTwo.playerBoard.map = battleshipGame.playerOne.ennemyBoard.map = map;
                        }
                        break;
                    case "fleet":
                        NodeList fleetNodeList = setting.getChildNodes();
                        setFleet(fleetNodeList, player);
                        break;
                    case "recorder":
                        // TODO create a builder setRecorder();
                        String recorderToString = setting.getTextContent();
                        StringToArrayListConverter converter = new StringToArrayListConverter();
                        ArrayList<Map<String, Integer>> playerMoves = converter.convert(recorderToString);
                        if (player.name.equals("playerOne")) {
                            battleshipGame.recorder.playerOneMoves = playerMoves;
                        } else {
                            battleshipGame.recorder.playerTwoMoves = playerMoves;
                        }
                        battleshipGame.recorder.index = 0;
                        break;
                }
            }
        }
    }

    private void setFleet(NodeList nodeList, BattleshipPlayer player) {
        for (int i = 0; i < nodeList.getLength(); ++i) {
            Node shipNode = nodeList.item(i);
            if (shipNode.getNodeType() == Node.ELEMENT_NODE) {
                NodeList shipSettings = shipNode.getChildNodes();
                switch (shipNode.getNodeName()) {
                    case "carrier":
                        setShip(shipSettings, player.carrier);
                        break;
                    case "battleship":
                        setShip(shipSettings, player.battleship);
                        break;
                    case "cruiser":
                        setShip(shipSettings, player.cruiser);
                        break;
                    case "destroyer":
                        setShip(shipSettings, player.destroyer);
                        break;
                    case "submarine":
                        setShip(shipSettings, player.submarine);
                        break;
                }
            }
        }
    }

    private void setShip(NodeList shipSettings, Ship ship) {
        for (int i = 0; i < shipSettings.getLength(); ++i) {
            Node shipLocationNode = shipSettings.item(i);
            if (shipLocationNode.getNodeType() == Node.ELEMENT_NODE) {
                int value = Integer.parseInt(shipLocationNode.getTextContent());
                switch (shipLocationNode.getNodeName()) {
                    case "stemX":
                        ship.stemX = value;
                        break;
                    case "stemY":
                        ship.stemY = value;
                        break;
                    case "bowX":
                        ship.bowX = value;
                        break;
                    case "bowY":
                        ship.bowY = value;
                        break;
                }
            }
        }

    }

    private void savePlayerShipsRemaining(Element player, int shipsRemaining) {
        Element shipsRemainingElement = document.createElement("shipsRemaining");
        String text = String.valueOf(shipsRemaining);
        shipsRemainingElement.appendChild(document.createTextNode(text));
        player.appendChild(shipsRemainingElement);
    }

    private void savePlayerMap(Element playerElement, int[][] map) {
        Element mapElement = document.createElement("map");
        String text = toStringConverters.mapToString(map);
        mapElement.appendChild(document.createTextNode(text));
        playerElement.appendChild(mapElement);
    }

    private void saveShipPosition(Element shipElement, Ship ship) {
        Element stemX = document.createElement("stemX");
        Element stemY = document.createElement("stemY");
        Element bowX = document.createElement("bowX");
        Element bowY = document.createElement("bowY");
        String stemXvalue = String.valueOf(ship.stemX);
        String stemYvalue = String.valueOf(ship.stemY);
        String bowXvalue = String.valueOf(ship.bowX);
        String bowYvalue = String.valueOf(ship.bowY);
        stemX.appendChild(document.createTextNode(stemXvalue));
        stemY.appendChild(document.createTextNode(stemYvalue));
        bowX.appendChild(document.createTextNode(bowXvalue));
        bowY.appendChild(document.createTextNode(bowYvalue));

        shipElement.appendChild(stemX);
        shipElement.appendChild(stemY);
        shipElement.appendChild(bowX);
        shipElement.appendChild(bowY);

    }

    private void savePlayerShip(Element shipElement, BattleshipPlayer player) {
        switch (shipElement.getTagName()) {
            case "carrier":
                saveShipPosition(shipElement, player.carrier);
                break;
            case "battleship":
                saveShipPosition(shipElement, player.battleship);
                break;
            case "cruiser":
                saveShipPosition(shipElement, player.cruiser);
                break;
            case "destroyer":
                saveShipPosition(shipElement, player.destroyer);
                break;
            case "submarine":
                saveShipPosition(shipElement, player.submarine);
                break;
        }
    }

    private void savePlayerFleet(Element playerElement, BattleshipPlayer player) {
        Element fleet = document.createElement("fleet");
        Element carrier = document.createElement("carrier");
        Element battleship = document.createElement("battleship");
        Element cruiser = document.createElement("cruiser");
        Element destroyer = document.createElement("destroyer");
        Element submarine = document.createElement("submarine");

        savePlayerShip(carrier, player);
        savePlayerShip(battleship, player);
        savePlayerShip(cruiser, player);
        savePlayerShip(destroyer, player);
        savePlayerShip(submarine, player);

        fleet.appendChild(carrier);
        fleet.appendChild(battleship);
        fleet.appendChild(cruiser);
        fleet.appendChild(destroyer);
        fleet.appendChild(submarine);
        playerElement.appendChild(fleet);
    }

    private void savePlayer(Element playerElement, BattleshipPlayer player) {
        savePlayerShipsRemaining(playerElement, player.shipsRemaining);
        savePlayerMap(playerElement, player.playerBoard.map);
        savePlayerFleet(playerElement, player);
    }

    private void saveId(Element idElement, int id) {
        idElement.appendChild(document.createTextNode(String.valueOf(id)));
    }

    private void saveAi(Element aiElement, BattleshipAi ai) {
        Element difficulty = document.createElement("difficulty");
        String difficultyValue = String.valueOf(ai.difficulty);
        difficulty.appendChild(document.createTextNode(difficultyValue));

        Element state = document.createElement("state");
        String stateValue = String.valueOf(ai.state);
        state.appendChild(document.createTextNode(stateValue));

        Element startPosition = document.createElement("startPosition");
        String startPositionValue = String.valueOf(ai.startPosition);
        startPosition.appendChild(document.createTextNode(startPositionValue));

        aiElement.appendChild(difficulty);
        aiElement.appendChild(state);
        aiElement.appendChild(startPosition);
    }

    private void saveRecorder(Element recorderElement, Recorder recorder) {
        Element playerOneMoves = document.createElement("playerOneMoves");
        String playerOneMovesValue = recorder.playerOneMoves.toString();
        playerOneMoves.appendChild(document.createTextNode(playerOneMovesValue));

        Element playerTwoMoves = document.createElement("playerTwoMoves");
        String playerTwoMovesValue = recorder.playerTwoMoves.toString();
        playerTwoMoves.appendChild(document.createTextNode(playerTwoMovesValue));

        Element index = document.createElement("index");
        String indexValue = Integer.toString(recorder.index);
        index.appendChild(document.createTextNode(indexValue));

        recorderElement.appendChild(index);
        recorderElement.appendChild(playerOneMoves);
        recorderElement.appendChild(playerTwoMoves);

    }

    private void saveGame(BattleshipGame battleshipGame) throws Exception {

    	Element games = document.getDocumentElement();
        Element game = document.createElement("game");
        Element id = document.createElement("id");
        //newGame.setIdAttribute(String.valueOf(battleshipGame.id),true);

		saveId(id, getMaxID());

        Element player1 = document.createElement("playerOne");
        Element player2 = document.createElement("playerTwo");
        savePlayer(player1, battleshipGame.playerOne);
        savePlayer(player2, battleshipGame.playerTwo);

        Element ai = document.createElement("ai");
        saveAi(ai, battleshipGame.ai);

        Element recorder = document.createElement("recorder");
        saveRecorder(recorder, battleshipGame.recorder);

        game.appendChild(id);
        game.appendChild(player1);
        game.appendChild(player2);
        game.appendChild(ai);
        game.appendChild(recorder);

        games.appendChild(game);
        DOMSource source = new DOMSource(document);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StreamResult result = new StreamResult(path);
        transformer.transform(source, result);
    }

    @SuppressWarnings("unused")
    private void printXML(NodeList nodeList) {
        for (int count = 0; count < nodeList.getLength(); count++) {
            Node tempNode = nodeList.item(count);
            // make sure it's element node.
            if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                // get node name and value
                System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
                System.out.println("Node Value =" + tempNode.getTextContent());
                if (tempNode.hasAttributes()) {
                    // get attributes names and values
                    NamedNodeMap nodeMap = tempNode.getAttributes();
                    for (int i = 0; i < nodeMap.getLength(); i++) {
                        Node node = nodeMap.item(i);
                        System.out.println("attr name : " + node.getNodeName());
                        System.out.println("attr value : " + node.getNodeValue());
                    }
                }
                if (tempNode.hasChildNodes()) {
                    // loop again if has child nodes
                    printXML(tempNode.getChildNodes());
                }
                System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");
            }
        }
    }

    @SuppressWarnings("unused")
    public void update(BattleshipGame battleshipGame) {
        NodeList gameSettings = getGameNodeListById(battleshipGame.id);
        updateGame(gameSettings, battleshipGame);
    }

    private void updateGame(NodeList gameSettings, BattleshipGame battleshipGame) {
        for (int i = 0; i < gameSettings.getLength(); ++i) {
            Node setting = gameSettings.item(i);
            switch (setting.getNodeName()) {
                case "playerOne":
                    updatePlayer(setting.getChildNodes(), battleshipGame.playerOne);
                    break;
                case "playerTwo":
                    updatePlayer(setting.getChildNodes(), battleshipGame.playerTwo);
                    break;
                default:
                    break;

            }
        }
    }

    @SuppressWarnings("unused")
	private void updateRecorder(NodeList recorderSettings, BattleshipGame battleshipGame){
        for(int i = 0 ; i < recorderSettings.getLength(); ++i){
            Node setting = recorderSettings.item(i);
        }
    }

    @SuppressWarnings("unused")
    private void updatePlayer(NodeList playerSettings, BattleshipPlayer player) {
        for(int i = 0; i < playerSettings.getLength(); ++i){
            Node setting = playerSettings.item(i);
            switch (setting.getNodeName()) {
                case "shipsRemaining":
                    setting.setTextContent(String.valueOf(player.shipsRemaining));
                    break;
                case "map":
                    String map = toStringConverters.mapToString(player.playerBoard.map);
                    setting.setTextContent(map);
                    break;
                case "fleet":
                    NodeList fleetNodeList = setting.getChildNodes();
                    updateFleet(fleetNodeList, player);
                    break;
            }
        }
    }

    @SuppressWarnings("unused")
    private void updateFleet(NodeList fleet, BattleshipPlayer player){
        for(int i =0 ; i < fleet.getLength(); ++i){
            Node setting = fleet.item(i);
            switch (setting.getNodeName()){
                case "carrier":
                    updateShipPosition(setting.getChildNodes(), player.carrier);
                    break;
                case "battleship":
                    updateShipPosition(setting.getChildNodes(), player.battleship);
                    break;
                case "cruiser":
                    updateShipPosition(setting.getChildNodes(), player.cruiser);
                    break;
                case "destroyer":
                    updateShipPosition(setting.getChildNodes(), player.destroyer);
                    break;
                case "submarine":
                    updateShipPosition(setting.getChildNodes(), player.submarine);
                    break;
            }
        }
    }

    @SuppressWarnings("unused")
    private void updateShipPosition(NodeList shipSettings, Ship ship) {
        for(int i = 0 ; i < shipSettings.getLength(); ++i){
            Node setting = shipSettings.item(i);
            String value;
            switch (setting.getNodeName()){
                case "stemX":
                    value = String.valueOf(ship.stemX);
                    setting.setTextContent(value);
                    break;
                case "stemY":
                    value = String.valueOf(ship.stemY);
                    setting.setTextContent(value);
                    break;
                case "bowX":
                    value = String.valueOf(ship.bowX);
                    setting.setTextContent(value);
                    break;
                case "bowY":
                    value = String.valueOf(ship.bowY);
                    setting.setTextContent(value);
                    break;
            }
        }
    }


    public int getMaxID(){
        String lastID = "";
        NodeList nodes = document.getElementsByTagName("game");
        for (int i = 0; i < nodes.getLength(); i++) {
            Element eElement = (Element) nodes.item(i);
            lastID = eElement.getElementsByTagName("id").item(0).getTextContent();
        }
   	 	return Integer.parseInt(lastID) + 1;
   } 

    private String[] getIDsList(String[] list){
     	String[] files = new String[list.length-1];
     	System.arraycopy(list,1,files,0,list.length-1);
    	return files;
    }

    public String[] getIDs(){
    	NodeList nodes = XMLDb.getXMLDb().document.getElementsByTagName("game");
    	String[] files = new String[nodes.getLength()];
    	for (int i = 0; i < nodes.getLength(); i++) {
    		Element eElement = (Element) nodes.item(i);
    		files[i] = eElement.getElementsByTagName("id").item(0).getTextContent();
    	}
    	files = getIDsList(files);
   	 	return files;
   } 

    public void deleteNode(String id) throws TransformerException{
    	NodeList nodes = XMLDb.getXMLDb().document.getElementsByTagName("game");
    	for (int i = 0; i < nodes.getLength(); i++) {
    		Element eElement = (Element) nodes.item(i);
    		Element gameId = (Element)eElement.getElementsByTagName("id").item(0);
    		String gId = gameId.getTextContent();
    		if(gId.equals(id)) {
    			eElement.getParentNode().removeChild(eElement);
    	        DOMSource source = new DOMSource(document);
    	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
    	        Transformer transformer = transformerFactory.newTransformer();
    	        StreamResult result = new StreamResult(path);
    	        transformer.transform(source, result);
    			break;
    		}
    	}
   } 
}

