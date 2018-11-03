package battleship.routes;

import battleship.controllers.Cpu;
import battleship.controllers.StartForm;
import battleship.entities.Game;
import battleship.entities.Grid;
import battleship.entities.Player;
import battleship.entities.ships.*;
import battleship.recorder.Recorder;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        // fleet has to be validated with the grid, therefore the grid is initialise inside generateFleet
        // maybe change the function name
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
        Player playerOne = game.playerOne;
        Player playerTwo = game.playerTwo;

        // player one attacks
        Map<String, Integer> target = new HashMap<>();
        int targetX = playerOne.targetX;
        int targetY = playerOne.targetY;
        target.put("x", targetX);
        target.put("y", targetY);
        game.recorder.playerOneMoves.add(target);

        int targetedArea = playerOne.ennemyGrid.map[targetX][targetY];

        // not water
        if(targetedArea > 0){playerTwo.shipsRemaining--;}

        playerOne.ennemyGrid.map[targetX][targetY] = -1;
        playerTwo.playerGrid.map[targetX][targetY] = -1;

        model.addAttribute("game", game);
        if(playerTwo.shipsRemaining == 0){return "you-won";}

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

        targetedArea = playerOne.playerGrid.map[cpuTargetX][cpuTargetY];

        if(targetedArea > 0){playerOne.shipsRemaining--;}

        playerTwo.ennemyGrid.map[cpuTargetX][cpuTargetY] = -1;
        playerOne.playerGrid.map[cpuTargetX][cpuTargetY] = -1;

        if(playerOne.shipsRemaining == 0){return "you-lost";}
        return "result";
    }

    @PostMapping("/save")
    public String saveGame(@ModelAttribute Game game, Model model){
        Player playerOne = game.playerOne;
        Player playerTwo = game.playerTwo;
//        try {
//            // TODO save to the current path instead of absolute
//            FileWriter fw = new FileWriter("/home/ju/JetBrainsProjects/IdeaProjects/INF5153/INF5153/battleship/src/main/db/db.xml");
//            fw.write("test");
//            fw.close();
//        } catch (Exception e){
//            System.out.println(e);
//        }
        System.out.println(game.recorder.playerOneMoves);
//        for (Map<String,Integer> r1: game.recorder.playerOneMoves) {
//            System.out.println("player one");
//            System.out.print(r1.get("x"));
//            System.out.println(r1.get("y"));
//        }
//        for (Map<String,Integer> r2: game.recorder.playerTwoMoves) {
//            System.out.println("player two");
//            System.out.print(r2.get("x"));
//            System.out.println(r2.get("y"));
//        }
        model.addAttribute("game", game);
        return "result";
    }

    @GetMapping("/load")
    public String loadGame(@ModelAttribute Game game, Model model){
        // TODO add an id to request params to load game, then construct the game from the xml db

        model.addAttribute("game", game);
        return "result";
    }

    @GetMapping("/replay")
    public String replayGame(@ModelAttribute Game game, Model model){
        Player playerOne = game.playerOne;
        Player playerTwo = game.playerTwo;

        JSONObject playerOneFleetJSON = playerOne.fleet;
        Grid playerOneGrid = new Grid();
        Map<String, Ship> playerOneFleet = Ship.buildFleet(playerOneFleetJSON);
        playerOneGrid.locateFleet(playerOneFleet);

        Grid cpuGrid = new Grid();
        // fleet has to be validated with the grid, therefore the grid is initialise inside generateFleet maybe change the function name
        Map<String, Ship> cpuFleet = Cpu.generateFleet(cpuGrid);

        ArrayList<Map<String,Integer>> list1 = game.recorder.playerOneMoves;
        ArrayList<Map<String,Integer>> list2 = game.recorder.playerTwoMoves;
        Map<String,Integer> target = list1.get(game.recorder.index);
        int targetX = target.get("x");
        int targetY = target.get("y");
        playerOne.ennemyGrid.map[targetX][targetY] = -1;
        playerTwo.playerGrid.map[targetX][targetY] = -1;
        model.addAttribute("game", game);
        return "result";
    }
}

