package battleship.entities.ships;

import javafx.util.Pair;

public class Submarine extends Ship {
    public String name;

    public Submarine() {
        super(1, 3, 0, 0, 0, 0);
        this.name = "Submarine";
    }

    public Submarine(int stemX, int stemY, int bowX, int bowY) {
        super(1, 3, stemX, stemY, bowX, bowY);
        this.name = "Submarine";
    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }

}
