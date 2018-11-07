package battleship.routes;

import battleship.controllers.Cpu;
import battleship.controllers.StartForm;
import battleship.entities.Board;
import battleship.entities.Game;
import battleship.entities.Player;
import battleship.entities.ships.*;
import battleship.entities.Recorder;
import db.Db;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

        // TODO validation of the player fleet, some validation methods are available in Board class, maybe add a Validation class

        JSONObject playerOneSettings = JSONObject.fromObject(data);
        JSONObject playerOneFleetJSON = playerOneSettings.getJSONObject("fleet");
        Board playerOneBoard = new Board();
        Map<String, Ship> playerOneFleet = Ship.buildFleet(playerOneFleetJSON);
        playerOneBoard.locateFleet(playerOneFleet);
        Board cpuBoard = new Board();
        // fleet has to be validated with the grid, therefore the grid is initialise inside generateFleet maybe change the function name
        Map<String, Ship> cpuFleet = Cpu.generateFleet(cpuBoard);

        Player cpu = new Player(cpuFleet, cpuBoard, playerOneBoard, "player2");
        Player player = new Player(playerOneFleet, playerOneBoard, cpuBoard, "player1");
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

        int targetedArea = p1.ennemyBoard.map[targetX][targetY];
        if(targetedArea > 0){p2.shipsRemaining--;}

        p1.ennemyBoard.map[targetX][targetY] = -1;
        p2.playerBoard.map[targetX][targetY] = -1;

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

        targetedArea = p1.playerBoard.map[cpuTargetX][cpuTargetY];
        if(targetedArea > 0){p1.shipsRemaining--;}

        p2.ennemyBoard.map[cpuTargetX][cpuTargetY] = -1;
        p1.playerBoard.map[cpuTargetX][cpuTargetY] = -1;

        if(p1.shipsRemaining == 0){return "you-lost";}
        return "result";
    }

    @PostMapping("/save")
    public String saveGame(@ModelAttribute Game game, Model model){
        Db.save(game);
        model.addAttribute("game", game);
        return "save";
    }

    @GetMapping("/load")
    public String loadGame(Model model){
        // TODO add game id to request params to load game

        Recorder r = new Recorder();
        Player p1 = new Player("player1");
        Player p2 = new Player("player2");
        Game game = new Game(1,p1,p2,r,false);
        Db.load(game);
        model.addAttribute("game", game);
        return "result";
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
        int targetedArea = p1.ennemyBoard.map[targetX][targetY];
        if(targetedArea > 0){p2.shipsRemaining--;}
        p1.ennemyBoard.map[targetX][targetY] = -1;
        p2.playerBoard.map[targetX][targetY] = -1;
        if(p2.shipsRemaining == 0){return "you-won";}

        // playerTwo turn
        target = p2Moves.get(game.recorder.index);
        targetX = target.get("x");
        targetY = target.get("y");
        targetedArea = p2.ennemyBoard.map[targetX][targetY];
        if(targetedArea > 0){p1.shipsRemaining--;}
        p2.ennemyBoard.map[targetX][targetY] = -1;
        p1.playerBoard.map[targetX][targetY] = -1;
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
        Board p1Board = new Board();
        Board p2Board = new Board();
        p1.playerBoard = p2.ennemyBoard = p1Board;
        p2.playerBoard = p1.ennemyBoard = p2Board;

        p1.playerBoard.locateFleet(p1Fleet);
        p1.ennemyBoard.locateFleet(p2Fleet);
        p2.playerBoard.locateFleet(p2Fleet);
        p2.ennemyBoard.locateFleet(p1Fleet);

        p1.shipsRemaining = p2.shipsRemaining = 17;

        model.addAttribute("game", game);
        return "replay";
    }
}

