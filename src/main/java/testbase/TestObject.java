package testbase;

import yanglas.Test;
import yanglas.base.Father;

import java.util.HashMap;
import java.util.Map;

public class TestObject {
    public static void main(String[] args){

        Father father = new Father(1);
        Map map = new HashMap<String,Object>();
        map.put("x","123");
        TestObject.modifyMap(map);
        System.out.println(map);
        TestObject.modifyObject(father);
        System.out.println(father.toString());
    }

    private static void modifyMap(Map map) {
        map.put("q","dsad");
    }

    /**
     * 传入的是对象的话，修改的指针对应的数据
     * @param father
     */
    private static void modifyObject(Father father) {

        father.setId(2);

        System.out.println(father.toString());

    }
}
