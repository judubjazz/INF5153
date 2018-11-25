package battleship.routes;

import battleship.Application;
import battleship.controllers.*;
import battleship.entities.BattleshipGame;
import javax.xml.transform.TransformerException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import battleship.SocketCS;
import db.Db;

@Controller
public class Routes {

    @GetMapping("/")
    public String main(){
        return "mainMenu";
    }
    
    @GetMapping("/home")
    public String home() {
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

    @PostMapping("/play")
    public String postPlay(@ModelAttribute BattleshipGame battleshipGame, Model model){
        GameController controller = new BattleShipGameController();
        battleshipGame = controller.play(battleshipGame);
        model.addAttribute("battleshipGame", battleshipGame);
        if(battleshipGame.playerOne.winner) return "you-won";
        if(battleshipGame.playerTwo.winner) return "you-lost";
        return "play";
    }

    @PostMapping("/save")
    public String postSave(@ModelAttribute BattleshipGame battleshipGame, Model model){
        GameController controller = new BattleShipGameController();
        battleshipGame = controller.save(battleshipGame);
        model.addAttribute("game", battleshipGame);
        return "save";
    }
    
    @GetMapping("/loadFiles")
    public String getLoadFiles(Model model){
    	model.addAttribute("listOfFiles", Db.getDb().getIDs());
        return "loadFiles";
    } 
    
    @RequestMapping(value="/load/{file}", method = RequestMethod.GET)
    public String getLoad(@PathVariable("file") String file, Model model){
    	try {
	        GameController controller = new BattleShipGameController();
	        BattleshipGame battleshipGame = controller.load(Integer.parseInt(file));
	        model.addAttribute("battleshipGame", battleshipGame);
	        return "play";
    	}catch (Exception ex) {
        	model.addAttribute("listOfFiles", Db.getDb().getIDs());
            return "mainMenu";
    	}
    }

    // TODO si qq un entre un bash url sur la route du home ex : localhost:8090/oaihsdoiahsdoihas la route du file est générée au lieu d'une page 404
    @RequestMapping(value="/{file}", method = RequestMethod.GET)
    public RedirectView getDelete(@PathVariable("file") String file, Model model) throws TransformerException{
    	Db.getDb().deleteNode(file);
    	model.addAttribute("listOfFiles", Db.getDb().getIDs());
    	return new RedirectView("loadFiles");
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

    @PostMapping("/restart")
    public String postRestartGame(@ModelAttribute BattleshipGame battleshipGame, Model model){
        GameController controller = new BattleShipGameController();
        battleshipGame = controller.restart(battleshipGame);
        model.addAttribute("game", battleshipGame);
        return "replay";
    }

    @GetMapping("/join")
    public String join(Model model){
        // TODO add a link to join/{gameID} in the html
        System.out.println(Application.gameList);
        model.addAttribute("gameList", Application.gameList);
        return "join-online-games";
    }

    @GetMapping("/create-online-game")
    public String createOnlineGame(Model model){
        try {
            SocketCS.startServer();
        }catch (Exception e){
            System.out.println(e);
        }
        return "create-online-games";
    }

    // TODO render other page if gameid is not in gamelist
    @RequestMapping(value="/join/{gameID}", method = RequestMethod.GET)
    public String joinGame(@PathVariable("gameID") int gameID, Model model){
        return "join-this-game";
    }

    @ExceptionHandler({Exception.class})
    public  String handleException(){
    	return "mainMenu";
    }
}

