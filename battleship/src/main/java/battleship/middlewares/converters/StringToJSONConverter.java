package battleship.middlewares.converters;

import net.sf.json.JSONObject;
import org.springframework.core.convert.converter.Converter;

public class StringToJSONConverter implements Converter<String, JSONObject> {

    @Override
    public JSONObject convert(String source) {
        return JSONObject.fromObject(source);
    }
}