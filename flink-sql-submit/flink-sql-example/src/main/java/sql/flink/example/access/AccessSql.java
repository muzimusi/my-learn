package sql.flink.example.access;

import sql.flink.example.function.udf.Ip2HostName;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;

/**
 * ./bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 3 --partitions 3 --topic flink_test_user_access
 * ./bin/kafka-console-producer.sh --broker-list localhost:9092 --topic flink_test_user_access
 * ./bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic flink_test_user_access --from-beginning
 */
public class AccessSql {
    public static void main(String[] args) throws Exception {
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        TableEnvironment tEnv = TableEnvironment.create(settings);

        tEnv.registerFunction("Ip2HostName", new Ip2HostName());

        //src table
        String ddlSource = "CREATE TABLE access_log (\n" +
                "    source_ip VARCHAR,\n" +
                "    target_ip VARCHAR,\n" +
                "    behavior VARCHAR,\n" +
                "    ts TIMESTAMP\n" +
                ") WITH (\n" +
                "    'connector.type' = 'kafka',\n" +
                "    'connector.version' = 'universal',\n" +
                "    'connector.topic' = 'flink_test_user_access',\n" +
                "    'connector.startup-mode' = 'earliest-offset',\n" +
                "    'connector.properties.0.key' = 'zookeeper.connect',\n" +
                "    'connector.properties.0.value' = 'hadoop-01:2181,hadoop-02:2181,hadoop-03:2181',\n" +
                "    'connector.properties.1.key' = 'bootstrap.servers',\n" +
                "    'connector.properties.1.value' = 'hadoop-01:9092,hadoop-02:9092,hadoop-03:9092',\n" +
                "    'update-mode' = 'append',\n" +
                "    'format.type' = 'json',\n" +
                "    'format.derive-schema' = 'true'\n" +
                ")";
        //sink
        String ddlSink = "CREATE TABLE access_sink (\n" +
                "    ip VARCHAR,\n" +
                "    host VARCHAR,\n" +
                "    dt VARCHAR,\n" +
                "    pv BIGINT,\n" +
                "    uv BIGINT\n" +
                ") WITH (\n" +
                "    'connector.type' = 'jdbc',\n" +
                "    'connector.url' = 'jdbc:mysql://hadoop-01:3306/flink_test',\n" +
                "    'connector.table' = 'access_sink',\n" +
                "    'connector.username' = 'root',\n" +
                "    'connector.password' = 'root',\n" +
                "    'connector.write.flush.max-rows' = '1'\n" +
                ")";

        String dmlInsert = "INSERT INTO access_sink\n" +
                "SELECT\n" +
                "  target_ip AS ip,\n" +
                "  Ip2HostName(target_ip) AS host,\n" +
                "  DATE_FORMAT(ts, 'yyyy-MM-dd HH:mm') AS dt,\n" +
                "  COUNT(*) AS pv,\n" +
                "  COUNT(DISTINCT source_ip) AS uv\n" +
                "FROM access_log\n" +
                "GROUP BY target_ip, DATE_FORMAT(ts, 'yyyy-MM-dd HH:mm')";

        tEnv.sqlUpdate(ddlSource);

        tEnv.sqlUpdate(ddlSink);

        tEnv.sqlUpdate(dmlInsert);

        tEnv.execute("Access SQL Job");
    }
}
