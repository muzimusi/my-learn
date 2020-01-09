package me.arjenlee.flinklearn.scala.stream.window

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}
import org.apache.flink.streaming.api.windowing.time.Time

object WindowSocketWordCount {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.socketTextStream("hadoop-01", 9999)
      .setParallelism(1)
      .flatMap(line => line.split("\t"))
      .map(word => (word, 1))
      .keyBy(0)
//      .window(TumblingEventTimeWindows.of(Time.seconds(5))) //event需要有timestamp，指定watermark
      .timeWindow(Time.seconds(10))
      .sum(1)
      .print()
      .setParallelism(1)
    env.execute("WindowSocketWordCount")
  }

}
