package battleship.entities.ships;

import javafx.util.Pair;

public class Destroyer extends Ship {
    public String name;

    public Destroyer() {
        super(2, 2, 0, 0, 0, 0);
        this.name = "Destroyer";
    }

    public Destroyer(int stemX, int stemY, int bowX, int bowY) {
        super(2, 2, stemX, stemY, bowX, bowY);
        this.name = "Destroyer";
    }

    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }

}
