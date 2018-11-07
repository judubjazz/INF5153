package battleship.converters;

import org.springframework.core.convert.converter.Converter;

public class StringTo2DArrayConverter implements Converter<String, int [][]> {

    @Override
    public int [][] convert(String source) {
        String[] data = source.split(",");
        int[][] map = new int[10][10];
        for(int i=0; i < 10; ++i){
            for(int j = 0; j < 10; ++j){
                map[i][j] = Integer.parseInt(data[(j) + (i*10)]);
            }
        }
        return map;
    }
}