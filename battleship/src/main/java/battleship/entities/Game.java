package battleship.entities;

import battleship.recorder.Recorder;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Game {
    public int id;
    public boolean difficulty;
    public Player playerOne;
    public Player playerTwo;
    public Recorder recorder;

    public Game(){

    }

    public Game(Player playerOne, Player playerTwo) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
    }

    public Game(int id, Player playerOne, Player playerTwo, Recorder recorder, boolean difficulty) {
        this.id = id;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.recorder = recorder;
        this.difficulty = difficulty;
    }

    private void setPlayerFromNodeList(NodeList nodeList, String playerID){
        for(int j = 0; j < nodeList.getLength(); ++j ){
            Node playerSettings = nodeList.item(j);
            if (playerSettings.getNodeType() == Node.ELEMENT_NODE) {

                if(playerSettings.getNodeName().equals("shipsRemaining")){
                    int shipsRemaining = Integer.parseInt(playerSettings.getTextContent());
                    if(playerID.equals("player1")) {
                        this.playerOne.shipsRemaining = shipsRemaining;
                    } else {
                        this.playerTwo.shipsRemaining = shipsRemaining;
                    }
                }else if(playerSettings.getNodeName().equals("map")){
                    String mapToString = playerSettings.getTextContent();
                    int [][] map = Grid.Stringto2DArray(mapToString);
                    if(playerID.equals("player1")) {
                        this.playerOne.playerGrid.map = this.playerTwo.ennemyGrid.map = map;
                    } else {
                        this.playerTwo.playerGrid.map = this.playerOne.ennemyGrid.map = map;
                    }
                }else if(playerSettings.getNodeName().equals("fleet")){
                    if(playerID.equals("player1")) {
//                        this.playerOne.playerGrid.map = this.playerTwo.ennemyGrid.map = map;
//                    } else {
//                        this.playerTwo.playerGrid.map = this.playerOne.ennemyGrid.map = map;
                    }
                }else if(playerSettings.getNodeName().equals("recorder")){

                }
            }
        }
    }

    public void buildGameFromNodeList(NodeList root, int id, String username){
        for (int count = 0; count < root.getLength(); count++) {
            Node gameNode = root.item(count);
            // make sure it's element node.
            if (gameNode.getNodeType() == Node.ELEMENT_NODE) {
                // get node name and value
                if(gameNode.getNodeName().equals("game")){
                    Node gameIDNode = gameNode.getFirstChild();
                    String gameID = gameIDNode.getTextContent();
                    if(gameID.equals(String.valueOf(id))){
                        NodeList gameNodeList = gameNode.getChildNodes();
                        for (int i = 0; i < gameNodeList.getLength(); i++) {
                            Node gameSettings = gameNodeList.item(i);
                            if (gameSettings.getNodeType() == Node.ELEMENT_NODE) {
                                if(gameSettings.getNodeName().equals("player1")){
                                    NodeList player1NodeList = gameSettings.getChildNodes();
                                    setPlayerFromNodeList(player1NodeList, "player1");

                                }
                                if(gameSettings.getNodeName().equals("player2")){
                                    NodeList player2NodeList = gameSettings.getChildNodes();
                                    setPlayerFromNodeList(player2NodeList, "player2");
                                }
                            }
                        }
//                        this.playerOne.shipsRemaining = Integer.parseInt(data[0]);
//                        String [] tempMap;
//                        tempMap = data[0].split(",");
//
//                        for(int i=0; i < 10; ++i){
//                            for(int j = 0; j < 10; ++j){
//                                this.playerOne.playerGrid.map[i][j] = this.playerTwo.ennemyGrid.map[i][j] = Integer.parseInt(tempMap[(j) + (i*10)]);
//                            }
//                        }
//
//                        this.difficulty = Boolean.parseBoolean(data[0]);
//
//                        this.playerOne.cruiser.stemX = Integer.parseInt(data[0]);
//                        this.playerOne.cruiser.stemY = Integer.parseInt(data[0]);
//                        this.playerOne.cruiser.bowX = Integer.parseInt(data[0]);
//                        this.playerOne.cruiser.bowY = Integer.parseInt(data[0]);
//
//
//                        this.playerOne.carrier.stemX = Integer.parseInt(data[0]);
//                        this.playerOne.carrier.stemY = Integer.parseInt(data[0]);
//                        this.playerOne.carrier.bowX = Integer.parseInt(data[0]);
//                        this.playerOne.carrier.bowY = Integer.parseInt(data[0]);
//
//                        this.playerOne.battleship.stemX = Integer.parseInt(data[0]);
//                        this.playerOne.battleship.stemY = Integer.parseInt(data[0]);
//                        this.playerOne.battleship.bowX = Integer.parseInt(data[0]);
//                        this.playerOne.battleship.bowY = Integer.parseInt(data[0]);
//
//                        this.playerOne.destroyer.stemX = Integer.parseInt(data[0]);
//                        this.playerOne.destroyer.stemY = Integer.parseInt(data[0]);
//                        this.playerOne.destroyer.bowX = Integer.parseInt(data[0]);
//                        this.playerOne.destroyer.bowY = Integer.parseInt(data[0]);
//
//                        this.playerOne.submarine.stemX = Integer.parseInt(data[0]);
//                        this.playerOne.submarine.stemY = Integer.parseInt(data[0]);
//                        this.playerOne.submarine.bowX = Integer.parseInt(data[0]);
//                        this.playerOne.submarine.bowY = Integer.parseInt(data[0]);
//
//                        this.playerTwo.shipsRemaining = Integer.parseInt(data[0]);
//                        this.playerTwo.cruiser.stemX = Integer.parseInt(data[0]);
//                        this.playerTwo.cruiser.stemY = Integer.parseInt(data[0]);
//                        this.playerTwo.cruiser.bowX = Integer.parseInt(data[0]);
//                        this.playerTwo.cruiser.bowY = Integer.parseInt(data[0]);
//
//                        this.playerTwo.carrier.stemX = Integer.parseInt(data[0]);
//                        this.playerTwo.carrier.stemY = Integer.parseInt(data[0]);
//                        this.playerTwo.carrier.bowX = Integer.parseInt(data[0]);
//                        this.playerTwo.carrier.bowY = Integer.parseInt(data[0]);
//
//                        this.playerTwo.battleship.stemX = Integer.parseInt(data[0]);
//                        this.playerTwo.battleship.stemY = Integer.parseInt(data[0]);
//                        this.playerTwo.battleship.bowX = Integer.parseInt(data[0]);
//                        this.playerTwo.battleship.bowY = Integer.parseInt(data[0]);
//
//                        this.playerTwo.destroyer.stemX = Integer.parseInt(data[0]);
//                        this.playerTwo.destroyer.stemY = Integer.parseInt(data[0]);
//                        this.playerTwo.destroyer.bowX = Integer.parseInt(data[0]);
//                        this.playerTwo.destroyer.bowY = Integer.parseInt(data[0]);
//
//                        this.playerTwo.submarine.stemX = Integer.parseInt(data[0]);
//                        this.playerTwo.submarine.stemY = Integer.parseInt(data[0]);
//                        this.playerTwo.submarine.bowX = Integer.parseInt(data[0]);
//                        this.playerTwo.submarine.bowY = Integer.parseInt(data[0]);
                        break;
                    }
                }else if (gameNode.hasChildNodes()) {
                    // loop again if has child nodes
                    buildGameFromNodeList(gameNode.getChildNodes(), id, username);
                }
                System.out.println("Node Name =" + gameNode.getNodeName() + " [CLOSE]");
            }
        }
    }

    public Player getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(Player playerOne) {
        this.playerOne = playerOne;
    }

    public Player getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(Player playerTwo) {
        this.playerTwo = playerTwo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Recorder getRecorder() {
        return recorder;
    }

    public void setRecorder(Recorder recorder) {
        this.recorder = recorder;
    }
}
