package battleship.middlewares.converters;

import java.util.Arrays;

/**
 * util class to render a 2d map into 1d array to string
 */
public class toStringConverters {
    public static String mapToString(int [][] map){
        String s = Arrays.deepToString(map);
        s = s.replaceAll("\\[|\\]", "");
        s = s.replaceAll(" ", "");
        return s;
    }
}
