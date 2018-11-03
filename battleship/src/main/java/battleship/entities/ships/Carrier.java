package battleship.entities.ships;

public class Carrier extends Ship {
    public String name;

    public Carrier() {
        super(5, 5, 0, 0, 0, 0);
        this.name = "Carrier";
    }

    public Carrier(int stemX, int stemY, int bowX, int bowY) {
        super(5, 5, stemX, stemY, bowX, bowY);
        this.name = "Carrier";
    }

    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }

}
