package testbase;

import java.util.HashMap;
import java.util.Map;

public class TestTryMap {
    public static void main(String[] args){
        System.out.println(getMap().get("KEY").toString());

    }
    public static Map<String, String> getMap() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("KEY", "INIT");

        try {
            map.put("KEY", "TRY");
            return map;
        }
        catch (Exception e) {
            map.put("KEY", "CATCH");
        }
        finally {
            map.put("KEY", "FINALLY");
        }

        return map;
    }
}
