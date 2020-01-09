package me.arjenlee.flinklearn.java;

import org.apache.flink.api.common.ExecutionMode;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.util.Collector;

/**
 * hello arjenlee
 * hello word
 * hello spark
 * hello flink
 * hello hdfs
 * hello mapreduce
 * hello hadoop
 * hello storm
 */
public class WordCount {

    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().setExecutionMode(ExecutionMode.BATCH);
        //wordCountJava(env);
        wordCountJavaLambda(env);
    }

    public static void wordCountJava(ExecutionEnvironment env) throws Exception {
        DataSource<String> dataSource = env.readTextFile("D:\\bigDatas\\inputs\\words.txt");
        dataSource
                .flatMap(new FlatMapFunction<String, String>() {
                    @Override
                    public void flatMap(String input, Collector<String> out) throws Exception {
                        for (String value : input.split("\t")) {
                            out.collect(value);
                        }
                    }
                })
                .map(new MapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> map(String value) throws Exception {
                        return new Tuple2<>(value, 1);
                    }
                })
                .groupBy(0)
                .reduce(new ReduceFunction<Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2) throws Exception {
                        return new Tuple2<>(value1.f0, value1.f1 + value2.f1);
                    }
                }).print();
    }

    // java的 lambada 本质是匿名内部类，不好用
    public static void wordCountJavaLambda(ExecutionEnvironment env) throws Exception {
        DataSource<String> dataSource = env.readTextFile("D:\\bigDatas\\inputs\\words.txt");
        dataSource
                .flatMap((String input, Collector<String> out) -> {
                    for (String word : input.split("\t")) {
                        out.collect(word);
                    }
                }).returns(Types.STRING)
                .map(x -> new Tuple2(x, 1))
                .returns(Types.TUPLE(Types.STRING, Types.INT))
                .groupBy(0)
                .sum(1)
                .print();
    }
}
