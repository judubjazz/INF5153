package battleship.middlewares.converters;

import org.springframework.core.convert.converter.Converter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringToMapStringIntegerConverter implements Converter<String, Map<String, Integer>> {

    @Override
    public Map<String, Integer> convert(String source) {
        Map<String, Integer> map = new HashMap<>();
        String pattern = "\\b([^\\s]+)=([^\\s]+)\\b,\\s([^\\s]+)=([^\\s]+)\\b,\\s([^\\s]+)=([^\\s]+)\\b";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(source);
        boolean match = m.find();

        while (match) {
            map.put(m.group(1), Integer.parseInt(m.group(2)));
            map.put(m.group(3), Integer.parseInt(m.group(4)));
            map.put(m.group(5), Integer.parseInt(m.group(6)));
            match=m.find();
        }
        return map;
    }
}