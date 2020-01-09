package me.arjenlee.mapreducelearn;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.File;
import java.io.IOException;

public class WordCountMR {

    static class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // hadoop hello hello
            String line = value.toString();
            // [hadoop, hello, hello]
            String[] words = line.split("\t");
            for (String word : words) {
                // (hadoop, 1)
                // (hello, 1)
                // (hello, 1)
                context.write(new Text(word), new IntWritable(1));
            }
        }
    }

    static class MyReducer extends Reducer<Text, IntWritable, Text, LongWritable> {
        // (hadoop, 1)
        // (hello, 1) (hello, 1)
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            long sum = 0;
            for (IntWritable value : values) {
                sum += 1;
            }
            // (hello, 2)
            // (hadoop, 1)
            context.write(key, new LongWritable(sum));
        }
    }

    public static void main(String[] args) throws Exception {
        // 1.conf (a)conf (b)job (c)mainClass
        Configuration conf = new Configuration();
        Job wordCountJob = Job.getInstance(conf, "WordCountMR");
        wordCountJob.setJarByClass(WordCountMR.class);

        // 2.input
        wordCountJob.setInputFormatClass(TextInputFormat.class);
        TextInputFormat.addInputPath(wordCountJob, new Path("D:\\bigDatas\\inputs\\words.txt"));

        // 3.map
        wordCountJob.setMapperClass(MyMapper.class);
        wordCountJob.setMapOutputKeyClass(Text.class);
        wordCountJob.setMapOutputValueClass(IntWritable.class);

        // 4.reduce
        wordCountJob.setReducerClass(MyReducer.class);
        wordCountJob.setOutputKeyClass(Text.class);
        wordCountJob.setOutputValueClass(LongWritable.class);

        // 5.output
        String path = "D:\\bigDatas\\outputs\\mapreduce\\wordcount";
        File dir = new File(path);
        if (dir.exists()) {
            for (File file : dir.listFiles()) {
                if (file.exists()) {
                    file.delete();
                }
            }
            dir.delete();
        }
        wordCountJob.setOutputFormatClass(TextOutputFormat.class);
        TextOutputFormat.setOutputPath(wordCountJob, new Path(path));

        //6.submit
        boolean success = wordCountJob.waitForCompletion(true);
        System.exit(success ? 0 : 1);
    }

}
