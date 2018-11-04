package battleship.routes;

import battleship.controllers.Cpu;
import battleship.controllers.StartForm;
import battleship.entities.Game;
import battleship.entities.Grid;
import battleship.entities.Player;
import battleship.entities.ships.*;
import battleship.recorder.Recorder;
import db.XML;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.NodeList;

import javax.xml.stream.XMLEventWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@Controller
public class Routes {

    @GetMapping("/")
    public String home() {;
        return "home";
    }

    @GetMapping("/start")
    public String greetingForm(Model model) {
        StartForm form = new StartForm();
        model.addAttribute("startForm", form);
        return "start";
    }

    @PostMapping("/start")
    public String startGame(@ModelAttribute StartForm startForm, Model model) {
        String data = startForm.getData();

        // TODO validation of the player fleet, some validation methods are available in Grid class, maybe add a Validation class

        JSONObject playerOneSettings = JSONObject.fromObject(data);
        JSONObject playerOneFleetJSON = playerOneSettings.getJSONObject("fleet");
        Grid playerOneGrid = new Grid();
        Map<String, Ship> playerOneFleet = Ship.buildFleet(playerOneFleetJSON);
        playerOneGrid.locateFleet(playerOneFleet);


        Grid cpuGrid = new Grid();
        // fleet has to be validated with the grid, therefore the grid is initialise inside generateFleet maybe change the function name
        Map<String, Ship> cpuFleet = Cpu.generateFleet(cpuGrid);

        Player cpu = new Player(cpuFleet, cpuGrid, playerOneGrid);
        Player player = new Player(playerOneFleet, playerOneGrid, cpuGrid);
        Recorder recorder = new Recorder(new ArrayList<>(), new ArrayList<>());
        boolean difficulty = playerOneSettings.getBoolean("difficulty");

        // TODO add the references as an id to the game
        Game game = new Game(1, player, cpu, recorder, difficulty);

        model.addAttribute("game", game);
        return "result";
    }

    @PostMapping("/torpedo")
    public String torpedo(@ModelAttribute Game game, Model model){
        Player p1 = game.playerOne;
        Player p2 = game.playerTwo;
        model.addAttribute("game", game);

        // player one attacks
        Map<String, Integer> target = new HashMap<>();
        int targetX = p1.targetX;
        int targetY = p1.targetY;
        target.put("x", targetX);
        target.put("y", targetY);
        game.recorder.playerOneMoves.add(target);

        int targetedArea = p1.ennemyGrid.map[targetX][targetY];
        if(targetedArea > 0){p2.shipsRemaining--;}

        p1.ennemyGrid.map[targetX][targetY] = -1;
        p2.playerGrid.map[targetX][targetY] = -1;

        if(p2.shipsRemaining == 0){return "you-won";}

        // cpu counter attacks
        if(game.difficulty){
            // TODO implement minMax algorithm
//            target = Cpu.targetMinMaxPosition();
        } else {
            target = Cpu.targetRandomPosition();
        }

        while(true){
            if(game.recorder.playerTwoMoves.contains(target)){
                target = Cpu.targetRandomPosition();
            } else {
                game.recorder.playerTwoMoves.add(target);
                break;
            }
        }

        // TODO refactor the code below because it repeats itself, maybe a Game class playTurn() function
        int cpuTargetX = target.get("x");
        int cpuTargetY = target.get("y");

        targetedArea = p1.playerGrid.map[cpuTargetX][cpuTargetY];
        if(targetedArea > 0){p1.shipsRemaining--;}

        p2.ennemyGrid.map[cpuTargetX][cpuTargetY] = -1;
        p1.playerGrid.map[cpuTargetX][cpuTargetY] = -1;

        if(p1.shipsRemaining == 0){return "you-lost";}
        return "result";
    }

    @PostMapping("/save")
    public String saveGame(@ModelAttribute Game game, Model model){
        Player p1 = game.playerOne;
        Player p2 = game.playerTwo;

        XML.read();
        System.out.println(game.recorder.playerOneMoves);
        model.addAttribute("game", game);
        return "save";
    }

    @GetMapping("/load")
    public String loadGame(Model model){
        // TODO add an id to request params to load game, then construct the game from the xml db
//        XML.read();
        NodeList nodeList = XML.getNodeList();
        XML.printNote(nodeList);
        Game game = new Game();
        game.id = 1;
        game.buildGameFromNodeList(nodeList, game.id, "1");
        model.addAttribute("game", game);
        return "home";
    }

    @PostMapping("/replay")
    public String replayGame(@ModelAttribute Game game, Model model){
        Player p1 = game.playerOne;
        Player p2 = game.playerTwo;

        ArrayList<Map<String,Integer>> p1Moves = game.recorder.playerOneMoves;
        ArrayList<Map<String,Integer>> p2Moves = game.recorder.playerTwoMoves;

        // TODO Refactor this in one function , maybe playTurn() in Game class
        // playerOne turn
        Map<String,Integer> target = p1Moves.get(game.recorder.index);
        int targetX = target.get("x");
        int targetY = target.get("y");
        int targetedArea = p1.ennemyGrid.map[targetX][targetY];
        if(targetedArea > 0){p2.shipsRemaining--;}
        p1.ennemyGrid.map[targetX][targetY] = -1;
        p2.playerGrid.map[targetX][targetY] = -1;
        if(p2.shipsRemaining == 0){return "you-won";}

        // playerTwo turn
        target = p2Moves.get(game.recorder.index);
        targetX = target.get("x");
        targetY = target.get("y");
        targetedArea = p2.ennemyGrid.map[targetX][targetY];
        if(targetedArea > 0){p1.shipsRemaining--;}
        p2.ennemyGrid.map[targetX][targetY] = -1;
        p1.playerGrid.map[targetX][targetY] = -1;
        if(p2.shipsRemaining == 0){return "you-won";}

        game.recorder.index++;
        model.addAttribute("game", game);
        return "replay";
    }

    @GetMapping("/replay")
    public String getReplayGame(@ModelAttribute Game game, Model model){
        Player p1 = game.playerOne;
        Player p2 = game.playerTwo;

        Map<String, Ship> p1Fleet = Ship.buildFleetFromShips(p1.carrier, p1.battleship, p1.cruiser, p1.destroyer, p1.submarine);
        Map<String, Ship> p2Fleet = Ship.buildFleetFromShips(p2.carrier, p2.battleship, p2.cruiser, p2.destroyer, p2.submarine);
        Grid p1Grid = new Grid();
        Grid p2Grid = new Grid();
        p1.playerGrid = p2.ennemyGrid = p1Grid;
        p2.playerGrid = p1.ennemyGrid = p2Grid;

        p1.playerGrid.locateFleet(p1Fleet);
        p1.ennemyGrid.locateFleet(p2Fleet);
        p2.playerGrid.locateFleet(p2Fleet);
        p2.ennemyGrid.locateFleet(p1Fleet);

        p1.shipsRemaining = p2.shipsRemaining = 17;

        model.addAttribute("game", game);
        return "replay";
    }
}

