package battleship.middlewares.converters;

import java.util.Arrays;

public class toStringConverters {
    public static String mapToString(int [][] map){
        String s = Arrays.deepToString(map);
        s = s.replaceAll("\\[|\\]", "");
        s = s.replaceAll(" ", "");
        return s;
    }
}
