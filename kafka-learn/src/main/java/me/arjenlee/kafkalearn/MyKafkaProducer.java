package me.arjenlee.kafkalearn;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * 消息被封装成ProducerRecord对象由KafkaProducer实例的send()方法发送。
 * <p>
 * 创建KafkaProducer时，需要指定Properties配置类，下面是配置项说明：
 * <p>
 * bootstrap.servers 用于配置集群地址，多个地址使用逗号分隔
 * acks 指定客户端发送消息的确认方式，0表示不需要服务端确认，1表示当消息写入到主分区时确认，all表示消息写入到所有分区时确认
 * retries 重试次数，设置客户端在发送数据错误时重试的次数
 * batch.size 当多个消息需要被发送到同一个分区时，生产者会把他们放在同一个批次里。该参数指定了一个批次可以使用的内存大小。
 * linger.ms 设置生产者在发送批次之前等待更多消息加入批次的时间
 * buffer.memory 设置生产者内存缓冲区大小，生产者用它缓冲要发送到服务器的消息。
 * key.serializer 消息中"键"的序列化方式
 * value.serializer 消息中"值"的序列化方式
 */
public class MyKafkaProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "hadoop-01:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Producer<String, String> producer = new KafkaProducer(props);
        for (int i = 100; i < 150; i++)
            producer.send(new ProducerRecord<String, String>("test", Integer.toString(i), Integer.toString(i)));

        producer.close();
    }
}
