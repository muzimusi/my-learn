package me.arjenlee.flinklearn.scala.stream

import org.apache.flink.streaming.api.scala.{StreamExecutionEnvironment, _}

object WordCount {
  def main(args: Array[String]): Unit = {
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    env.readTextFile("D:\\bigDatas\\inputs\\words.txt")
      .setParallelism(1)
      .flatMap(line => line.split("\t"))
      .map(word => (word, 1))
      .keyBy(0)
      .sum(1)
      .print()
      .setParallelism(1)
    env.execute("wordCount")
  }
}
