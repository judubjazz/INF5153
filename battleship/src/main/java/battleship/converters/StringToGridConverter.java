package battleship.converters;

import battleship.entities.Board;
import org.springframework.core.convert.converter.Converter;

public class StringToGridConverter implements Converter<String, Board> {

    @Override
    public Board convert(String source) {
        String[] data = source.split(",");
        return new Board(
//                Integer.parseInt(data[0]),
//                Integer.parseInt(data[1]),
//                Integer.parseInt(data[2]),
//                Integer.parseInt(data[3]),
//                Integer.parseInt(data[4]),
//                Integer.parseInt(data[5])
        );
    }
}