package scala.batch

import org.apache.flink.api.java.utils.ParameterTool
// 必须引入
import org.apache.flink.api.scala._

/**
  * ./bin/flink run -m yarn-cluster -ys 3 -p 3 -yjm 1024 -ytm 2048 -ynm FlinkBatchWordCount -yqu root.work -c scala.batch.WordCount ck_test/flink-example-1.0-SNAPSHOT.jar --input hdfs:///sparkwordcount.data --out
  * */
object WordCount {
  def main(args : Array[String]): Unit = {
    val params = ParameterTool.fromArgs(args)
    // set up the batch execution environment
    val env = ExecutionEnvironment.getExecutionEnvironment
    // make parameters available in the web interface
    env.getConfig.setGlobalJobParameters(params)
    // get input data
    val text = env.readTextFile(params.get("input"))
    val sum = text
      .flatMap(line => line.toLowerCase().split("\\W+"))
      .map(word =>(word,1))
      .groupBy(0)
      .sum(1)
      .setParallelism(1)
    //sum.writeAsCsv(params.get("output"), "\n", " ")
    if (params.has("output")) {
      sum.writeAsCsv(params.get("output"), "\n", " ")
      env.execute("Scala WordCount Example")
    } else {
      println("Printing result to stdout. Use --output to specify output path.")
      sum.print()
    }
  }
}
