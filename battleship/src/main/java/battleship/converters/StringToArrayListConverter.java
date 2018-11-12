package battleship.converters;

import org.springframework.core.convert.converter.Converter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringToArrayListConverter implements Converter<String, ArrayList> {

    @Override
    public ArrayList<Map<String, Integer>> convert(String source) {

        ArrayList<Map<String, Integer>> arrayList = new ArrayList<>();
        String pattern = "\\b([^\\s]+)=([^\\s]+)\\b,\\s([^\\s]+)=([^\\s]+)\\b,\\s([^\\s]+)=([^\\s]+)\\b";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(source);
        boolean match = m.find();

        while (match) {
            Map<String, Integer> map = new HashMap<>();
            map.put(m.group(1), Integer.parseInt(m.group(2)));
            map.put(m.group(3), Integer.parseInt(m.group(4)));
            map.put(m.group(5), Integer.parseInt(m.group(6)));
            arrayList.add(map);
            match=m.find();
        }
        return arrayList;
    }
}