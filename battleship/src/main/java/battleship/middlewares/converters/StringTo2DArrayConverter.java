package battleship.middlewares.converters;

import org.springframework.core.convert.converter.Converter;

public class StringTo2DArrayConverter implements Converter<String, int [][]> {
    public int arraySize = 10;

    @Override
    public int [][] convert(String source) {
        String[] data = source.split(",");
        int[][] map = new int[arraySize][arraySize];
        for(int i=0; i < arraySize; ++i){
            for(int j = 0; j < arraySize; ++j){
                map[i][j] = Integer.parseInt(data[(j) + (i*arraySize)]);
            }
        }
        return map;
    }
}