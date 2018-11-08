package db;

import battleship.converters.StringToArrayListConverter;
import battleship.entities.BattleshipGame;
import battleship.entities.Board;
import battleship.entities.Player;
import battleship.entities.ships.Ship;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;

// TODO in the db.xml file, set an attribute to game instead of an inner element
public class Db {
    // TODO construct relative path
    static private Db instance = null;
    private final static String dbLocation = "/home/ju/JetBrainsProjects/IdeaProjects/INF5153/battleship/src/main/java/db/db.xml";

    private Db() {

    }

    static public Db getInstance() {
        if (instance == null ){
            instance = new Db();
        }
        return instance;
    }

    public void save(BattleshipGame battleshipGame){
        try{
            Db.getInstance().addNode(battleshipGame);
        } catch (Exception e){
            System.out.println(e);
        }

    }
    public void load(BattleshipGame battleshipGame){
        NodeList tree = getRootNodeList();
        Node root = tree.item(0);
        NodeList gamesTree = root.getChildNodes();
        for (int count = 0; count < gamesTree.getLength(); count++) {
            Node gameNode = gamesTree.item(count);
            if (gameNode.getNodeType() == Node.ELEMENT_NODE && gameNode.getNodeName().equals("battleshipGame")) {
                NodeList gameSettings = gameNode.getChildNodes();
                for (int i = 0; count < gameSettings.getLength(); i++) {
                    Node gameSetting = gameSettings.item(i);
                    if (gameNode.getNodeType() == Node.ELEMENT_NODE && gameSetting.getNodeName().equals("id")) {
                        String gameID = gameSetting.getTextContent();
                        if (gameID.equals(String.valueOf(battleshipGame.id))) {
                            setGame(gameSettings, battleshipGame);
                            break;
                        }
                    }
                }
            }
        }
    }

    private void setGame(NodeList gameSettings, BattleshipGame battleshipGame){
        for (int i = 0; i < gameSettings.getLength(); i++) {
            Node setting = gameSettings.item(i);
            if (setting.getNodeType() == Node.ELEMENT_NODE) {
                NodeList playerSettings = setting.getChildNodes();
                switch(setting.getNodeName()){
                    case "player1":
                        setPlayer(playerSettings, battleshipGame.playerOne, battleshipGame);
                        break;
                    case "player2":
                        setPlayer(playerSettings, battleshipGame.playerTwo, battleshipGame);
                        break;
                    case "difficulty":
                        // TODO set difficulty
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void setPlayer(NodeList playerSettings, Player player, BattleshipGame battleshipGame){
        for(int j = 0; j < playerSettings.getLength(); ++j ){
            Node setting = playerSettings.item(j);
            if (setting.getNodeType() == Node.ELEMENT_NODE) {
                switch(setting.getNodeName()){
                    case "shipsRemaining":
                        int shipsRemaining = Integer.parseInt(setting.getTextContent());
                        player.shipsRemaining = shipsRemaining;
                        break;
                    case "map":
                        // TODO create a function setMap()
                        String mapToString = setting.getTextContent();
                        int [][] map = Board.Stringto2DArray(mapToString);
                        if(player.name.equals("player1")) {
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
                        // TODO create a function setRecorder();
                        String recorderToString = setting.getTextContent();
                        StringToArrayListConverter c = new StringToArrayListConverter();
                        ArrayList<Map<String, Integer>> playerMoves = c.convert(recorderToString);
                        if(player.name.equals("player1")) {
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

    private void setFleet(NodeList nodeList, Player player){
        for(int i = 0; i < nodeList.getLength(); ++i){
            Node shipNode = nodeList.item(i);
            if(shipNode.getNodeType() == Node.ELEMENT_NODE) {
                NodeList shipSettings = shipNode.getChildNodes();
                switch(shipNode.getNodeName()){
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
        for(int i = 0; i < shipSettings.getLength(); ++i){
            Node shipLocationNode = shipSettings.item(i);
            if(shipLocationNode.getNodeType() == Node.ELEMENT_NODE) {
                int value = Integer.parseInt(shipLocationNode.getTextContent());
                switch(shipLocationNode.getNodeName()){
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


    private void addNode(BattleshipGame battleshipGame) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse("/home/ju/JetBrainsProjects/IdeaProjects/INF5153/battleship/src/main/java/db/db.xml");
        Element root = document.getDocumentElement();

        Element newGame = document.createElement("battleshipGame");

        Element id = document.createElement("id");
        id.appendChild(document.createTextNode(String.valueOf(battleshipGame.id)));
        newGame.appendChild(id);

        Element difficulty = document.createElement("difficulty");
        difficulty.appendChild(document.createTextNode(String.valueOf(battleshipGame.difficulty)));
        newGame.appendChild(difficulty);

        Element player1 = document.createElement("player1");

        Element shipsRemaining = document.createElement("shipsRemaining");
        shipsRemaining.appendChild(document.createTextNode(String.valueOf(battleshipGame.playerOne.shipsRemaining)));

        Element map = document.createElement("map");
        difficulty.appendChild(document.createTextNode(String.valueOf(battleshipGame.playerOne.playerBoard.map)));

        Element fleet = document.createElement("fleet");
        Element stemX = document.createElement("stemX");
        Element stemY = document.createElement("stemY");
        Element bowX = document.createElement("bowX");
        Element bowY = document.createElement("bowY");

        Element carrier = document.createElement("carrier");
        stemX.appendChild(document.createTextNode(String.valueOf(battleshipGame.playerOne.carrier.stemX)));

        Element battleship = document.createElement("batlleship");
        Element cruiser = document.createElement("cruiser");
        Element destroyer = document.createElement("destroyer");
        Element submarine = document.createElement("submarine");
        Element recorder = document.createElement("recorder");
        player1.appendChild(document.createTextNode(String.valueOf(battleshipGame.playerOne)));
        newGame.appendChild(player1);



        root.appendChild(newGame);
        DOMSource source = new DOMSource(document);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        StreamResult result = new StreamResult("/home/ju/JetBrainsProjects/IdeaProjects/INF5153/battleship/src/main/java/db/db.xml");
        transformer.transform(source, result);
    }

    public void write() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("company");
            doc.appendChild(rootElement);

            // staff elements
            Element staff = doc.createElement("Staff");
            rootElement.appendChild(staff);

            // set attribute to staff element
            Attr attr = doc.createAttribute("id");
            attr.setValue("1");
            staff.setAttributeNode(attr);

            // shorten way
            // staff.setAttribute("id", "1");

            // firstname elements
            Element firstname = doc.createElement("firstname");
            firstname.appendChild(doc.createTextNode("yong"));
            staff.appendChild(firstname);

            // lastname elements
            Element lastname = doc.createElement("lastname");
            lastname.appendChild(doc.createTextNode("mook kim"));
            staff.appendChild(lastname);

            // nickname elements
            Element nickname = doc.createElement("nickname");
            nickname.appendChild(doc.createTextNode("mkyong"));
            staff.appendChild(nickname);

            // salary elements
            Element salary = doc.createElement("salary");
            salary.appendChild(doc.createTextNode("100000"));
            staff.appendChild(salary);
            // write the content into xml file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("C:\\file.xml"));
            // Output to console for testing
            // StreamResult result = new StreamResult(System.out);
            transformer.transform(source, result);

            System.out.println("File saved!");
        } catch (ParserConfigurationException pce) {
            pce.printStackTrace();
        } catch (TransformerException tfe) {
            tfe.printStackTrace();
        }
    }

    public void read() {
        try {
            // TODO add an id to request params to load game, then construct the game from the xml db
            File file = new File("/home/ju/JetBrainsProjects/IdeaProjects/INF5153/INF5153/battleship/src/main/java/db/db.xml");
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            if (doc.hasChildNodes()) { printNote(doc.getChildNodes()); }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public NodeList getRootNodeList() {
        NodeList nodeList = null;
        try {
//            File file = new File(dbLocation);
            File file = new File("/home/ju/JetBrainsProjects/IdeaProjects/INF5153/battleship/src/main/java/db/db.xml");
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
            if (doc.hasChildNodes()) {
                nodeList = doc.getChildNodes();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return nodeList;
    }

    public void printNote(NodeList nodeList) {
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
                    printNote(tempNode.getChildNodes());
                }
                System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");
            }
        }
    }
}
