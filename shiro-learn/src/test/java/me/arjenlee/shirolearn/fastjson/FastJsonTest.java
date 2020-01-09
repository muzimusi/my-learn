package me.arjenlee.shirolearn.fastjson;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

public class FastJsonTest {
    @Test
    public void testParseObject(){
        String value1 = null;
        String value2 = "";
        String value3 = " ";
        Object parse1 = JSON.parse(value1);
        Object parse2 = JSON.parse(value2);
        Object parse3 = JSON.parse(value3);
    }
}
