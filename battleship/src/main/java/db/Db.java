package db;

import battleship.entities.games.Game;

public abstract class Db <G extends Game> {
    abstract public void load(G game);
    abstract public void save(G game);
}
