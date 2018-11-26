package battleship;


import battleship.entities.games.Game;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Application {

    public static List<Game> gameList;
    public static void main(String[] args) throws Exception {
        gameList = new ArrayList<>();
        SpringApplication.run(Application.class, args);
    }

}

