package me.arjenlee.flinklearn.java;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.apache.flink.util.Collector;

public class FlatMapTest {
    private static String URL = null;

    private static String Dir = null;

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        testFlatMap(env);
        env.execute("FlatMapTest");
    }

    public static void testFlatMap(StreamExecutionEnvironment environment) {

        environment
                .addSource(new RichParallelSourceFunction<String>() {
                    boolean status = true;

                    @Override
                    public void run(SourceContext<String> ctx) throws Exception {
                        int i = 0;
                        while (status) {
                            ctx.collect("hello\t" + i++);
                            Thread.sleep(1000);
                        }
                    }

                    @Override
                    public void cancel() {
                        status = false;
                    }
                })
                .setParallelism(3)
                .flatMap(new RichFlatMapFunction<String, String>() {

                    @Override
                    public void open(Configuration parameters) throws Exception {
                        if (Dir == null) {
                            Dir = "dir";
                            System.out.println("open...");
                            Thread.sleep(1000);
                        }
                    }

                    @Override
                    public void flatMap(String line, Collector<String> out) throws Exception {
                        if (URL == null) {
                            URL = "ok";
                            System.out.println("init flatMap ...");
                        }
                        for (String word : line.split("\t")) {
                            out.collect(word);
                        }
                    }
                })
                .setParallelism(3)
                .map(new MapFunction<String, Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> map(String word) throws Exception {
                        return new Tuple2<>(word, 1);
                    }
                })
                .keyBy(0)
                .sum(1)
                .print();
    }
}
