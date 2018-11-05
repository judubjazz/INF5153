package battleship.converters;

import battleship.entities.Grid;
import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StringToMapStringIntegerConverter implements Converter<String, Map<String, Integer>> {

    @Override
    public Map<String, Integer> convert(String source) {
        checkArg(source);
        Map<String, Integer> map = new HashMap<>();
        String[] keyValuePairs = source.split(",");
        String key = null;
        int value;
        String[] keyValueArr = null;
        if(source.contains("x")) {
            for (String keyValuePair : keyValuePairs) {
                keyValueArr = keyValuePair.split(":");
                key = keyValueArr[0];
                value = Integer.parseInt(keyValueArr[1]);
                map.put(key, value);
            }
        }
        return map;
    }

    private void checkArg(String from) {
        // In the spec, null input is not allowed
        if (from == null) {
            throw new IllegalArgumentException("null source is in allowed");
        }
    }
}