package battleship.routes;

import battleship.Application;
import battleship.controllers.*;
import battleship.entities.games.BattleshipGame;
import battleship.entities.games.Game;
import battleship.entities.players.BattleshipPlayer;
import battleship.factories.GameFactory;
import battleship.middlewares.Validation;
import db.XMLDb;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import static battleship.controllers.GameController.getController;

@Controller
public class Routes{

    @GetMapping("/")
    public String main(){
        return "menu/mainMenu";
    }

    @GetMapping("/select")
    public String select() {
        return "menu/select";
    }

    @RequestMapping(value= "/home/{gameName}", method = RequestMethod.GET)
    public String home(@PathVariable("gameName") String gameName, Model model){
        if(gameName.equals("battleship") || gameName.equals("tictactoe")){
            model.addAttribute("gameName", gameName);
            return "menu/home";
        } else {
            throw new GameNotFoundException();
        }
    }

    @RequestMapping(value= "/start/{gameName}", method = RequestMethod.GET)
    public String getStart(@PathVariable("gameName") String gameName, Model model){
        if(gameName.equals("battleship") || gameName.equals("tictactoe")){
            model.addAttribute("gameName", gameName);
            model.addAttribute("formController", new JsonRequestController());
            String s = "game/" + gameName + "/start";
            return s;
        } else {
            throw new GameNotFoundException();
        }
    }

    @PostMapping("/start")
    public String postStart(@ModelAttribute JsonRequestController formController, Model model) {
        JSONObject data = formController.data;
        String gameName = data.getString("name");
        GameController controller = getController(gameName);
        Game game = controller.start(data);
        model.addAttribute("formController", formController);
        model.addAttribute("battleshipGame", game);

        return "game/" + gameName + "/play";
    }

    @PostMapping("/play")
    public String postPlay(@ModelAttribute JsonRequestController formController, Model model){
        JSONObject data = formController.data;
        String gameName = data.getString("name");
        GameFactory factory = GameFactory.getFactory(gameName);
        GameController controller = GameController.getController(gameName);

        Game game = factory.createGame(data);
        game = controller.play(game);

        model.addAttribute("formController", formController);
        model.addAttribute("battleshipGame", game);

        if(game.playerOne.winner){
            model.addAttribute("winner", "You Won");
            return "game/" + gameName + "/end-of-game";
        }
        if(game.playerTwo.winner){
            model.addAttribute("winner", "You Lost");
            return "game/" + gameName + "/end-of-game";
        }
        return "game/" + gameName + "/play";
    }

    @PostMapping("/save")
    public String postSave(@ModelAttribute BattleshipGame battleshipGame, Model model){
        GameController<BattleshipGame, BattleshipPlayer> controller = new BattleShipGameController();
        Game game = controller.save(battleshipGame);
        model.addAttribute("battleshipGame", game);
        return "menu/save";
    }
    
    @GetMapping("/loadFiles")
    public String getLoadFiles(Model model){
    	model.addAttribute("listOfFiles", XMLDb.getXMLDb().getIDs());
        return "menu/loadFiles";
    } 
    
    @RequestMapping(value="/load/{file}", method = RequestMethod.GET)
    public String getLoad(@PathVariable("file") String file, Model model){
    	try {
	        GameController<BattleshipGame, BattleshipPlayer> controller = new BattleShipGameController();
	        Game game = controller.load(Integer.parseInt(file));
	        model.addAttribute("formController", new JsonRequestController());
	        model.addAttribute("battleshipGame", game);
	        return "game/battleship/play";
    	}catch (Exception ex) {
        	model.addAttribute("listOfFiles", XMLDb.getXMLDb().getIDs());
            return "menu/mainMenu";
    	}
    }

    @RequestMapping(value= "/delete/{gameID}", method = RequestMethod.GET)
    public String getDelete(@PathVariable("gameID") int gameID, Model model){
        GameController<BattleshipGame, BattleshipPlayer> controller = new BattleShipGameController();
        if(!controller.delete(gameID)){
            throw new GameNotFoundException();
        }
        model.addAttribute("listOfFiles", XMLDb.getXMLDb().getIDs());
        return "menu/loadFiles";
    }


    @PostMapping("/replay")
    public String replayGame(@ModelAttribute JsonRequestController formController, Model model){
        JSONObject data = formController.data;
        String gameName = data.getString("name");
        GameFactory factory = GameFactory.getFactory(gameName);
        GameController controller = GameController.getController(gameName);

        Game game = factory.createGame(data);
        game = controller.replay(game);
        model.addAttribute("battleshipGame", game);
        model.addAttribute("formController", new JsonRequestController());

        if(game.playerOne.winner){
            model.addAttribute("winner", "You Won");
            return "game/" + gameName + "/end-of-game";
        }
        if(game.playerTwo.winner){
            model.addAttribute("winner", "You Lost");
            return "game/" + gameName + "/end-of-game";
        }
        return "game/" + gameName + "/replay";
    }

    @PostMapping("/restart")
    public String postRestartGame(@ModelAttribute JsonRequestController formController, Model model){
        JSONObject data = formController.data;
        String gameName = data.getString("name");
        GameFactory factory = GameFactory.getFactory(gameName);
        GameController controller = GameController.getController(gameName);
        Game game;
        if (factory != null && controller != null) {
            game = factory.createGame(data);
            game = controller.restart(game);
            model.addAttribute("formController", formController);
            model.addAttribute("battleshipGame", game);
            return "game/" + gameName + "/replay";
        } else {
            throw new BadRequestException();
        }

    }

    @GetMapping("/join")
    public String join(Model model){
        model.addAttribute("gameList", Application.gameListVsHuman);
        return "game/battleship/join-online-games";
    }

    @RequestMapping(value="/join/{gameID}", method = RequestMethod.GET)
    public String joinGame(@PathVariable("gameID") int gameID){
        if (Validation.gameIDisInTheList(gameID)) return "game/battleship/join-this-game";
        throw new GameNotFoundException();
    }

    @GetMapping("/create-online-game")
    public String createOnlineGame(Model model){
        return "game/battleship/create-online-games";
    }




    /* errors handler */
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
    public String handleAnyException(Model model) {
        System.out.println(HttpStatus.BAD_REQUEST);
    	if(HttpStatus.BAD_REQUEST != null){
        	model.addAttribute("errorType", "Bad request");
            return "error/error";
        }else if(HttpStatus.NOT_FOUND != null){
        	model.addAttribute("errorType", "Message not found");
            return "error/error";
        }else if(HttpStatus.INTERNAL_SERVER_ERROR != null){
        	model.addAttribute("errorType", "A problem has occurred");
            return "error/error";
        }else{
        	model.addAttribute("errorType", "A problem has occurred");
            return "error/error";
        }
    }
}

