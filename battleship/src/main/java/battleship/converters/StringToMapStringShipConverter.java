//package battleship.converters;
//
//import battleship.entities.ships.Ship;
//import org.springframework.core.convert.converter.Converter;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class StringToMapStringShipConverter implements Converter<String, Map<String, Ship>> {
//    @Override
//    public Map<String, Ship> convert(String source) {
//        checkArg(source);
//        Map<String, Ship> map = new HashMap<>();
//        String[] keyValuePairs = source.split(",");
//        String key = null;
//        int value;
//        String[] keyValueArr = null;
////        if(from.contains("x")) {
////            key = "x";
////            int index = from.indexOf("x");
////            value = from.charAt(index+2);
////        }
////        if(from.contains("y")) {
////            key = "y";
////            int index = from.indexOf("y");
////            value = from.charAt(index+2);
////            map.put(key, value);
////        }
////
//        if(source.contains("x")) {
//            for (String keyValuePair : keyValuePairs) {
//                keyValueArr = keyValuePair.split(":");
//                key = keyValueArr[0];
//                value = Integer.parseInt(keyValueArr[1]);
//                Ship s = new Ship();
//                map.put(key, s);
//            }
//        }
//        return map;
//    }
//
//    private void checkArg(String from) {
//        // In the spec, null input is not allowed
//        if (from == null) {
//            throw new IllegalArgumentException("null source is in allowed");
//        }
//    }
//}
