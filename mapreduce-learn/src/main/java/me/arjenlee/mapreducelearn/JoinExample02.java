package me.arjenlee.mapreducelearn;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
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
import java.util.List;
import java.util.Scanner;

/**
 * 小米5	1998.00	1001	2018-03-10	15：30：11
 * 茅台王子酒	366.00	1001	2018-03-10	15：30：11
 * 法国进口拉菲	708.00	1002	2018-03-10	15：30：12
 * 东北大辣皮	15.50	1002	2018-03-10	15：30：12
 * <p>
 * 1001	张三丰	男	1990-10-11
 * 1002	张无忌	男	1992-10-10
 * 1003	小龙女	女	1993-10-13
 * <p>
 * 小米5	1998.00	张三丰	男	2018-03-10	15：30：11
 * 茅台王子酒	366.00	张三丰	男	2018-03-10	15：30：11
 * 法国进口拉菲	708.00	张无忌	男	2018-03-10	15：30：12
 * 东北大辣皮	15.50	张无忌	男	2018-03-10	15：30：12
 */

//map端做join
public class JoinExample02 {
    public static class JoinMapper extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {
            // 1001	张三丰	男	1990-10-11
            Scanner scanner = new Scanner(new File("D:\\bigDatas\\inputs\\join\\user.txt"));
            String[] splits = null;
            String next = null;
            StringBuilder sb;
            while (scanner.hasNextLine()) {
                sb = new StringBuilder();
                next = scanner.nextLine();
                splits = next.split("\t");
                sb.append(splits[1]).append("\t")
                        .append(splits[2]).append("\t");
                context.write(new Text(splits[0]),
                        new Text(sb.toString()));
                System.out.println(splits[0] + "\t" + sb.toString());
            }

        }

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // 小米5	1998.00	1001	2018-03-10	15：30：11
            String line = value.toString();
            String[] split = line.split("\t");
            StringBuffer sb = new StringBuffer();

            System.out.println(context.getCacheFiles().toString());
            for (int i = 0; i < split.length; i++) {
                if (i == 2) {
                    sb.append(context.getCurrentValue().toString()).append("\t");
                }
                sb.append(split[i]).append("\t");
            }
            context.getCurrentValue().set(new Text(sb.toString()));
            System.out.println(split[2] + "\t" + sb.toString());
        }
    }

    /**
     * 1001 茅台王子酒	366.00	2018-03-10	15：30：11
     * 1001 小米5	1998.00	2018-03-10	15：30：11
     * 1001	    张三丰	男
     */

    public static class JoinRecuce extends Reducer<Text, Text, Text, Text> {
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            for (Text value : values) {
                context.write(null, value);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // conf
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "joinJob");
        job.setJarByClass(JoinExample02.class);
        // input
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job, new Path("D:\\bigDatas\\inputs\\join\\order.txt"));
        // map
        job.setMapperClass(JoinMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        // reduce
        job.setReducerClass(JoinRecuce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        // output
        job.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(job, new Path("D:\\bigDatas\\outputs\\mapreduce\\orderUserJoin"));
        // submit
        boolean success = job.waitForCompletion(true);
        System.exit(success ? 0 : 1);

    }
}
