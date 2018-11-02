package battleship.converters;

import battleship.entities.ships.Ship;
import org.springframework.core.convert.converter.Converter;

public class StringToShipConverter implements Converter<String, Ship> {

    @Override
    public Ship convert(String source) {
        String[] data = source.split(",");
        return new Ship();
    }
}