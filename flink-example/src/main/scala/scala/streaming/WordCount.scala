package scala.streaming
import java.util.Properties

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.java.utils.ParameterTool
// 必须引入
import org.apache.flink.api.scala._
import org.apache.flink.streaming.api.CheckpointingMode
import org.apache.flink.streaming.api.environment.CheckpointConfig
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer
/**
  * ./bin/flink run -m yarn-cluster -ys 3 -p 3 -yjm 1024 -ytm 2048 -ynm FlinkStreamingWordCount -yqu root.work -c scala.streaming.WordCount ck_test/flink-example-1.0-SNAPSHOT.jar --input-topic words --bootstrap.servers localhost:9092 --zookeeper.connect localhost:2181 --group.id ck
  * */
object WordCount {
  def main(args: Array[String]):Unit = {
    val params = ParameterTool.fromArgs(args)
    val env = StreamExecutionEnvironment.getExecutionEnvironment
    //env.getConfig().disableSysoutLogging();
    env.enableCheckpointing(10000) // create a checkpoint every 5 seconds

    // set mode to exactly-once (this is the default)
    env.getCheckpointConfig.setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE)

    // make sure 500 ms of progress happen between checkpoints
    env.getCheckpointConfig.setMinPauseBetweenCheckpoints(500)

    // checkpoints have to complete within one minute, or are discarded
    env.getCheckpointConfig.setCheckpointTimeout(60000)

    // allow only one checkpoint to be in progress at the same time
    env.getCheckpointConfig.setMaxConcurrentCheckpoints(1)

    // enable externalized checkpoints which are retained after job cancellation
    //env.getCheckpointConfig.enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION)

    // make parameters available in the web interface
    env.getConfig.setGlobalJobParameters(params)
    val properties = new Properties()
    val topic = params.get("input-topic")
    val brokers = params.get("bootstrap.servers")
    val zkhosts = params.get("zookeeper.connect","localhost:2181")
    val group = params.get("group.id","ck-test-group")
    val startFromEarliest = params.getBoolean("startfromearliest",false)
    properties.setProperty("bootstrap.servers", brokers)
    properties.setProperty("zookeeper.connect", zkhosts)
    properties.setProperty("group.id", group)
    val kafkaConsumer = new FlinkKafkaConsumer(topic, new SimpleStringSchema, properties)
    if (startFromEarliest) {
      kafkaConsumer.setStartFromEarliest()
    }

    env.addSource(kafkaConsumer)
      .flatMap(_.toLowerCase.split("\\s+"))
      .filter(_.nonEmpty)
      .map((_, 1))
      .keyBy(0)
      .sum(1)
      .print()
    env.execute("wordCountTest")
  }
}
