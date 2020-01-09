package sql;

import org.apache.flink.table.functions.ScalarFunction;

import java.util.HashMap;

public class Ip2HostName extends ScalarFunction {
    private HashMap<String, String> map;

    public Ip2HostName() {
        map = new HashMap<>();
        map.put("192.168.199.200", "hadoop01");
        map.put("192.168.199.201", "hadoop02");
        map.put("192.168.199.202", "hadoop03");
    }

    public String eval(String ip) {
        return map.get(ip) != null ? map.get(ip) : "unknow";
    }
}
