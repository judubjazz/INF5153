package battleship.entities.ships;

import javafx.util.Pair;

public class Cruiser extends Ship {
    public String name;

    public Cruiser() {
        super(3, 3, 0, 0, 0, 0);
        this.name = "Cruiser";
    }

    public Cruiser(int stemX, int stemY, int bowX, int bowY) {
        super(3, 3, stemX, stemY, bowX, bowY);
        this.name = "Cruiser";
    }

    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }

}
