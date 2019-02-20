package testbase;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Base64Test {

    public static void main(String[] args) throws UnsupportedEncodingException {

        Map<String,Object> map = new HashMap<>();

        String data = "{\"idCard\":\"339005197808254010\",\"userMobile\":\"15866666666\",\"appType\":\"AT5\",\"name\":\"盛建国\",\"platformType\":\"PT2\",\"device\":\"PC\"}";
        byte[] encodeData =Base64.getEncoder().encode(data.getBytes("UTF-8"));

        System.out.println(new String(encodeData));
        map.put("data",new String(encodeData));

        String origStr = new String(Base64.getDecoder().decode((String) map.get("data")), "UTF-8");

        System.out.println(origStr);
    }
}
