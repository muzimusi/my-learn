package me.arjenlee.hivelearn;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class MyLower extends UDF {
    public Text evaluate(Text str) {
        String src = str.toString();
        return new Text(src.toLowerCase());
    }

}
