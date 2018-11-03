package battleship.entities.ships;

public class Battleship extends Ship {
    public String name;

    public Battleship() {
        super(4, 4, 0, 0, 0, 0);
        this.name = "Battleship";
    }

    public Battleship(int stemX, int stemY, int bowX, int bowY) {
        super(4, 4, stemX, stemY, bowX, bowY);
        this.name = "Battleship";
    }

    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }

}
