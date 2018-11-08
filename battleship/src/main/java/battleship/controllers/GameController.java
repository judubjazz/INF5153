package battleship.controllers;

import battleship.entities.BattleshipGame;


public interface GameController {
    public BattleshipGame play();
    public BattleshipGame load();
    public BattleshipGame save(BattleshipGame battleshipGame);
    public BattleshipGame playTurn(BattleshipGame battleshipGame);
    public BattleshipGame replay(BattleshipGame battleshipGame);
    public BattleshipGame start(String data);
    public BattleshipGame restart(BattleshipGame battleshipGame);
}
