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
import java.util.List;

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

//reduce端做join
public class JoinExampleMR01 {
    public static class JoinMapper extends Mapper<LongWritable, Text, Text, Text> {
        @Override
        protected void map(LongWritable key, Text input, Context context) throws IOException, InterruptedException {
            // 小米5	1998.00	1001	2018-03-10	15：30：11
            // 1001	张三丰	男	1990-10-11
            String[] splits = input.toString().split("\t");
            int tag = -1;
            Long id = 0l;
            for (int i = 0; i < splits.length; i++) {
                try {
                    id = Long.valueOf(splits[i]);
                    tag = i;
                } catch (Exception e) {
                }
            }
            StringBuilder sb = new StringBuilder();
            for (int i = 0; tag != -1 && i < splits.length; i++) {
                if (i == tag)
                    continue;
                sb.append(splits[i]).append("\t");
            }
            context.write(new Text(id.toString()), new Text(sb.toString()));
            System.out.println(id.toString() + "\t" + sb.toString());
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
            List<String> orderList = new ArrayList<>();
            String userString = "";
            for (Text value : values) {
                String line = value.toString();
                String[] split = line.split("\t");
                // order
                if (split.length > 2) {
                    orderList.add(line);
                } else {
                    // user
                    userString = line;
                }
            }
            String[] userSplit = userString.split("\t");
            orderList.stream().map(x -> {
                String[] split = x.split("\t");
                StringBuffer sb = new StringBuffer();
                sb.append(split[0]).append("\t")
                        .append(split[1]).append("\t")
                        .append(userSplit[0]).append("\t")
                        .append(userSplit[1]).append("\t")
                        .append(split[2]).append("\t")
                        .append(split[3]);
                return sb.toString();
            });
            for (String o : orderList) {
                context.write(null, new Text(o));
                System.out.println(key.toString() + "\t" + o);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        // conf
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "joinJob");
        job.setJarByClass(JoinExampleMR01.class);
        // input
        job.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(job, new Path("D:\\bigDatas\\inputs\\join\\order.txt"));
        TextInputFormat.addInputPath(job, new Path("D:\\bigDatas\\inputs\\join\\user.txt"));
        // map
        job.setMapperClass(JoinMapper.class);
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);
        // reduce
        job.setReducerClass(JoinRecuce.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);
        // output
        String path = "D:\\bigDatas\\outputs\\mapreduce\\orderUserJoin";
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
