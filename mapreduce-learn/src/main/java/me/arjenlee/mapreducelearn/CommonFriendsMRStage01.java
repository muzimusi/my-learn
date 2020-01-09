package me.arjenlee.mapreducelearn;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A:B,C,D,F,E,O
 * B:A,C,E,K
 * C:F,A,D,I
 * D:A,E,F,L
 * E:B,C,D,M,L
 * F:A,B,C,D,E,O,M
 * G:A,C,D,E,F
 * H:A,C,D,E,O
 * I:A,O
 * J:B,O
 * K:A,C,D
 * L:D,E,F
 * M:E,F,G
 * O:A,H,I,J
 */
public class CommonFriendsMRStage01 {

    public static class MyMapper extends Mapper<LongWritable, Text, Text, Text> {
        // map 阶段
        // 读入 A:B,C,D,F,E,O 拆分：B-A C-A D-A F-A E-A 0-A
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] split = line.split(":");
            String people = split[0];
            String[] friendArray = split[1].split(",");
            for (String friend : friendArray) {
                //System.out.println(friend + ":" + people);
                context.write(new Text(friend), new Text(people));
            }
        }
    }

    // reduce 阶段
    // 聚合相同的key
    // A	B, C, D, F, G, H, I, K, O,
    // B	A, E, F, J,
    public static class MyReduce extends Reducer<Text, Text, Text, Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            List<String> list = new ArrayList<>();
            for (Text value : values) {
                list.add(value.toString());
            }
            Collections.sort(list);
            StringBuilder sb = new StringBuilder();
            for (String s : list) {
                sb.append(s).append(", ");
            }
            context.write(key, new Text(sb.toString()));
        }
    }

    public static void main(String[] args) throws Exception {
        // job
        Job job = Job.getInstance(new Configuration(), "CommonFriendsJob01");
        job.setJarByClass(CommonFriendsMRStage01.class);
        // input
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job, new Path("D:\\bigDatas\\inputs\\commonfriends\\friends.txt"));
        // map
        job.setMapperClass(MyMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        // reduce
        job.setReducerClass(MyReduce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        // output
        String path = "D:\\bigDatas\\outputs\\mapreduce\\commonFriends\\stage01";
        File dir = new File(path);
        if (dir.exists()) {
            for (File f : dir.listFiles()) {
                if (f.exists()) {
                    f.delete();
                }
            }
            dir.delete();
        }
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job, new Path(path));
        // submit
        boolean success = job.waitForCompletion(true);
        System.exit(success ? 0 : 1);
    }
}
