package battleship.routes;

import battleship.Application;
import battleship.controllers.*;
import battleship.entities.Recorder;
import battleship.entities.games.BattleshipGame;
import battleship.entities.games.Game;
import battleship.factories.GameFactory;
import battleship.middlewares.Validation;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import db.Db;

import static battleship.controllers.GameController.getController;

@Controller
public class Routes {

    @GetMapping("/")
    public String main(){
        return "menu/mainMenu";
    }
    
    @GetMapping("/home")
    public String home() {
        return "menu/home";
    }

    @GetMapping("/start")
    public String getStart(Model model) {
        FormController form = new FormController();
        model.addAttribute("formController", form);
        return "game/start";
    }

    @PostMapping("/start")
    public String postStart(@ModelAttribute JsonRequestController formController, Model model) {
        JSONObject data = formController.data;
        String gameName = data.getString("name");
        GameController controller = getController(gameName);
        Game game = controller.start(data);
        model.addAttribute("formController", formController);
        model.addAttribute("battleshipGame", game);
        return "game/play";
    }

    @PostMapping("/play")
    public String postPlay(@ModelAttribute JsonRequestController formController, Model model){
        JSONObject data = formController.data;
        String gameName = data.getString("name")
                ;
        GameFactory factory = GameFactory.getFactory(gameName);
        Game game = factory.createGame(data);

        GameController controller = GameController.getController(gameName);
        game = controller.play(game);

        model.addAttribute("formController", formController);
        model.addAttribute("battleshipGame", game);

        if(game.playerOne.winner) return "game/you-won";
        if(game.playerTwo.winner) return "game/you-lost";
        return "game/play";
    }

    @PostMapping("/save")
    public String postSave(@ModelAttribute BattleshipGame battleshipGame, Model model){
        GameController controller = new BattleShipGameController();
        Game game = controller.save(battleshipGame);
        model.addAttribute("battleshipGame", game);
        return "menu/save";
    }
    
    @GetMapping("/loadFiles")
    public String getLoadFiles(Model model){
    	model.addAttribute("listOfFiles", Db.getDb().getIDs());
        return "menu/loadFiles";
    } 
    
    @RequestMapping(value="/load/{file}", method = RequestMethod.GET)
    public String getLoad(@PathVariable("file") String file, Model model){
    	try {
	        GameController controller = new BattleShipGameController();
	        Game game = controller.load(Integer.parseInt(file));
	        model.addAttribute("formController", new JsonRequestController());
	        model.addAttribute("battleshipGame", game);
	        return "game/play";
    	}catch (Exception ex) {
        	model.addAttribute("listOfFiles", Db.getDb().getIDs());
            return "menu/mainMenu";
    	}
    }

    @RequestMapping(value= "/delete/{gameID}", method = RequestMethod.GET)
    public String getDelete(@PathVariable("gameID") int gameID, Model model){
        GameController controller = new BattleShipGameController();
        if(!controller.delete(gameID)){
            throw new GameNotFoundException();
        }
        model.addAttribute("listOfFiles", Db.getDb().getIDs());
        return "menu/loadFiles";
    }


    @PostMapping("/replay")
    public String replayGame(@ModelAttribute BattleshipGame battleshipGame, Model model){
        GameController controller = new BattleShipGameController();
        Game game = controller.replay(battleshipGame);
        model.addAttribute("battleshipGame", game);
        model.addAttribute("formController", new JsonRequestController());
        if(battleshipGame.playerOne.winner) return "game/you-won";
        if(battleshipGame.playerTwo.winner) return "game/you-lost";
        return "game/replay";
    }

    @PostMapping("/restart")
    public String postRestartGame(@ModelAttribute JsonRequestController formController, Model model){
        JSONObject data = formController.data;
        String gameName = data.getString("name");
        GameFactory factory = GameFactory.getFactory(gameName);
        GameController controller = GameController.getController(gameName);
        Recorder r = factory.buildRecorderFromJSONObject(JSONObject.fromObject(data));
        Game game = controller.restart(1, r);
        model.addAttribute("formController", formController);
        model.addAttribute("battleshipGame", game);
        return "game/replay";
    }

    @GetMapping("/join")
    public String join(Model model){
        model.addAttribute("gameList", Application.gameListVsHuman);
        return "game/join-online-games";
    }

    @RequestMapping(value="/join/{gameID}", method = RequestMethod.GET)
    public String joinGame(@PathVariable("gameID") int gameID){
        if (Validation.gameIDisInTheList(gameID)) return "game/join-this-game";
        throw new GameNotFoundException();
    }

    @GetMapping("/create-online-game")
    public String createOnlineGame(Model model){
        return "game/create-online-games";
    }


    /* errors handler */
    // TODO dont send an problem occur, instead mark sorry try again later,
    // else if status = 400 send bad request, else if 404 send not found mesage

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "game not found")
    private class GameNotFoundException extends RuntimeException {
        private static final long serialVersionUID = 41L;
    }

    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "socket server error")
    private class ServerErrorException extends RuntimeException {
        private static final long serialVersionUID = 42L;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "bad request")
    private class BadRequestException extends RuntimeException {
        private static final long serialVersionUID = 43L;
    }

    @ExceptionHandler({Exception.class})
    public String handleException(){
        return "error/error";
    }

}

