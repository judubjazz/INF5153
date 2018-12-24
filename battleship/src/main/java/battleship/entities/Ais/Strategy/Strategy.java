package battleship.entities.Ais.Strategy;

public class Strategy {
    public enum Corner {UP_LEFT, UP_RIGHT, DOWN_LEFT, DOWN_RIGHT}

    public Corner getCorner(int x, int y) {
        if (x == 0 && y == 0) return Corner.UP_LEFT;
        if (x == 0 && y == 2) return Corner.DOWN_LEFT;
        if (x == 2 && y == 0) return Corner.UP_RIGHT;
        if (x == 2 && y == 2) return Corner.DOWN_RIGHT;
        return null;
    }
}
