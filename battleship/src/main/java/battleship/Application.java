package battleship;


import battleship.entities.games.BattleshipGame;
import battleship.entities.games.Game;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Application {

    public static List<Game> gameListVsHuman;
    public static List<BattleshipGame> gameListVsCpu;

    public static void main(String[] args) throws Exception {
        gameListVsHuman = new ArrayList<>();
        gameListVsCpu = new ArrayList<>();
        SpringApplication.run(Application.class, args);
        try {
            SocketCS.startServer();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}

