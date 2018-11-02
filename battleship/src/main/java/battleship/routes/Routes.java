package battleship.routes;

import battleship.controller.Cpu;
import battleship.controller.StartForm;
import battleship.entities.Game;
import battleship.entities.Grid;
import battleship.entities.Player;
import battleship.entities.ships.*;
import battleship.recorder.Recorder;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//import org.springframework.web.bind.annotation.HelloController;



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
    public String greetingSubmit(@ModelAttribute StartForm startForm, Model model) {
        String data = startForm.getData();

        // TODO validation of the player fleet, some validation methods are available in Grid class, maybe add a Validation class

        JSONObject playerOneSettings = JSONObject.fromObject(data);
        Grid playerGrid = new Grid();
        Map<String, Ship> fleet = Ship.buildFleet(playerOneSettings);
        playerGrid.locateFleet(fleet);


        Grid cpuGrid = new Grid();
        // fleet has to be validated with the grid, therefore the grid is initialise inside generateFleet
        // maybe change the function name
        Map<String, Ship> cpuFleet = Cpu.generateFleet(cpuGrid);

        Player cpu = new Player(fleet, cpuGrid, playerGrid);
        Player player = new Player(fleet, playerGrid, cpuGrid);
        Recorder recorder = new Recorder(new ArrayList<>(), new ArrayList<>());

        // TODO add the references as an id to the game
        Game game = new Game(player, cpu, recorder);

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

        if(playerTwo.shipsRemaining == 0){return "you-won";}

        // cpu counter attacks
        target = Cpu.targetRandomPosition();
        while(true){
            if(game.recorder.playerTwoMoves.contains(target)){
                target = Cpu.targetRandomPosition();
            } else {
                game.recorder.playerTwoMoves.add(target);
                break;
            }
        }
        // TODO record the targets to replay the game and prevents the cpu from targeting two time the same position, maybe create a Recorder class
        // TODO refactor the code below because it repeats itself, maybe a Game class playTurn() function
        int cpuTargetX = target.get("x");
        int cpuTargetY = target.get("y");

        targetedArea = playerOne.playerGrid.map[cpuTargetX][cpuTargetY];

        if(targetedArea > 0){playerOne.shipsRemaining--;}

        playerTwo.ennemyGrid.map[cpuTargetX][cpuTargetY] = -1;
        playerOne.playerGrid.map[cpuTargetX][cpuTargetY] = -1;

        if(playerOne.shipsRemaining == 0){return "you-lost";}

        model.addAttribute("game", game);
        return "result";
    }
}

