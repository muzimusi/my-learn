package sql.flink.example.function.udf;

import org.apache.flink.table.functions.ScalarFunction;

import java.util.HashMap;

public class Ip2HostName extends ScalarFunction {

    private HashMap<String, String> map;

    public Ip2HostName() {
        map = new HashMap<String, String>();
        map.put("192.168.199.200", "hadoop-01");
        map.put("192.168.199.201", "hadoop-02");
        map.put("192.168.199.202", "hadoop-03");
    }

    public Ip2HostName(HashMap<String, String> map) {
        this.map = map;
    }

    public String eval(String ip) {
        return map.get(ip) == null ? "unknow" : map.get(ip);
    }
}
