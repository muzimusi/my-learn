package me.arjenlee.sparklearn.scala

import org.apache.spark.{SparkConf, SparkContext}

object WordCountScala {
  def main(args: Array[String]): Unit = {
    // val inputPath = args(0)
    // val inputPath = "D:\\bigDatas\\inputs\\words.txt" // 路径是ok的
    val inputPath = "file:///d:/bigDatas/inputs/words.txt" // 路径是ok的
    val config = new SparkConf()
    config.setAppName("WordCountScala")
    // config.setMaster("yarn") // 提交到yarn
    config.setMaster("local") // 提交到本地
    val sc = new SparkContext(config)
    //val textFile = sc.textFile(inputPath)
    val textFile = sc.textFile(inputPath)
    textFile
      .flatMap(line => line.split("\t")).map(word => (word, 1))
      .reduceByKey((x, y) => x + y).collect().foreach(println)
  }
}
