package me.arjenlee.flinklearn.scala.batch

import org.apache.flink.api.scala.ExecutionEnvironment
import org.apache.flink.api.scala._

object WordCount {
  def main(args: Array[String]): Unit = {
    var env: ExecutionEnvironment = ExecutionEnvironment.getExecutionEnvironment
    wordCountScala(env)
  }

  def wordCountScala(env: ExecutionEnvironment): Unit = {
    env.readTextFile("D:\\bigDatas\\inputs\\words.txt")
      .flatMap(x => x.split("\t"))
      .map(x => (x, 1))
      .groupBy(0)
      .sum(1)
      .print()
  }
}
