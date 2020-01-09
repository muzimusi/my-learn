package me.arjenlee.sparklearn.scala

import org.apache.spark.{SparkConf, SparkContext}

object TopNScala {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setMaster("local")
    conf.setAppName("TopNScala")
    val context = new SparkContext(conf)
    context.textFile("file:///d:/bigDatas/inputs/words.txt")
      .flatMap(line => line.split("\t"))
      .map(word => (word, 1))
      .reduceByKey((a, b) => a + b)
      .sortBy(_._2, false)
      .take(3)
      .foreach(println)
  }
}
