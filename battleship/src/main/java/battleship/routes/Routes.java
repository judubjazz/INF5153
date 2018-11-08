package battleship.routes;

import battleship.controllers.*;
import battleship.entities.BattleshipGame;
//import db.Db;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Map;


@Controller
public class Routes {

    @GetMapping("/")
    public String home() {;
        return "home";
    }

    @GetMapping("/start")
    public String getStart(Model model) {
        FormController form = new FormController();
        model.addAttribute("formController", form);
        return "start";
    }

    @PostMapping("/start")
    public String postStart(@ModelAttribute FormController formController, Model model) {
        String data = formController.data;
        GameController controller = new BattleShipGameController();
        BattleshipGame battleshipGame = controller.start(data);
        model.addAttribute("battleshipGame", battleshipGame);
        return "play";
    }

    @PostMapping("/torpedo")
    public String torpedo(@ModelAttribute BattleshipGame battleshipGame, Model model){
        GameController controller = new BattleShipGameController();
        battleshipGame = controller.playTurn(battleshipGame);
        model.addAttribute("battleshipGame", battleshipGame);
        if(battleshipGame.playerOne.winner) return "you-won";
        if(battleshipGame.playerTwo.winner) return "you-lost";
        return "play";
    }

    @PostMapping("/save")
    public String postSave(@ModelAttribute BattleshipGame battleshipGame, Model model){
        GameController controller = new BattleShipGameController();
        battleshipGame = controller.save(battleshipGame);
//        Db.getInstance().save(battleshipGame);
        model.addAttribute("game", battleshipGame);
        return "save";
    }

    @GetMapping("/load")
    public String getLoad(Model model){
        // TODO add battleshipGame id to request params to load battleshipGame

        GameController controller = new BattleShipGameController();
        BattleshipGame battleshipGame = controller.load();
        model.addAttribute("battleshipGame", battleshipGame);
        return "play";
    }

    @PostMapping("/replay")
    public String replayGame(@ModelAttribute BattleshipGame battleshipGame, Model model){
        GameController controller = new BattleShipGameController();
        battleshipGame = controller.replay(battleshipGame);
        model.addAttribute("game", battleshipGame);
        if(battleshipGame.playerOne.winner) return "you-won";
        if(battleshipGame.playerTwo.winner) return "you-lost";
        return "replay";
    }

    @GetMapping("/replay")
    public String getReplayGame(@ModelAttribute BattleshipGame battleshipGame, Model model){
        GameController controller = new BattleShipGameController();
        battleshipGame = controller.restart(battleshipGame);
        model.addAttribute("game", battleshipGame);
        return "replay";
    }
}

