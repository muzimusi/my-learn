package sql.flink.example.userbehavior;

import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.TableEnvironment;

/**
 * ./bin/flink run -m yarn-cluster -d -yjm 1024 -ytm 1024 -p 1 -s 1 -c sql.SqlExample lj_test/flink-sql-example.jar
 */
public class UserBehaviorSql {
    public static void main(String[] args) throws Exception {
        EnvironmentSettings settings = EnvironmentSettings.newInstance()
                .useBlinkPlanner()
                .inStreamingMode()
                .build();
        TableEnvironment tEnv = TableEnvironment.create(settings);

        //数据源表
        String ddlSource = "CREATE TABLE user_log (\n" +
                "    user_id VARCHAR,\n" +
                "    item_id VARCHAR,\n" +
                "    category_id VARCHAR,\n" +
                "    behavior VARCHAR,\n" +
                "    ts TIMESTAMP\n" +
                ") WITH (\n" +
                "    'connector.type' = 'kafka',\n" +
                "    'connector.version' = 'universal',\n" +
                "    'connector.topic' = 'user_behavior',\n" +
                "    'connector.startup-mode' = 'earliest-offset',\n" +
                "    'connector.properties.0.key' = 'zookeeper.connect',\n" +
                "    'connector.properties.0.value' = 'hadoop-01:2181',\n" +
                "    'connector.properties.1.key' = 'bootstrap.servers',\n" +
                "    'connector.properties.1.value' = 'hadoop-01:9092',\n" +
                "    'update-mode' = 'append',\n" +
                "    'format.type' = 'json',\n" +
                "    'format.derive-schema' = 'true'\n" +
                ")";

        //数据结果表
        String ddlSink = "CREATE TABLE pvuv_sink (\n" +
                "    dt VARCHAR,\n" +
                "    pv BIGINT,\n" +
                "    uv BIGINT\n" +
                ") WITH (\n" +
                "    'connector.type' = 'jdbc',\n" +
                "    'connector.url' = 'jdbc:mysql://hadoop-01:3306/flink_test',\n" +
                "    'connector.table' = 'pvuv_sink',\n" +
                "    'connector.username' = 'root',\n" +
                "    'connector.password' = 'root',\n" +
                "    'connector.write.flush.max-rows' = '1'\n" +
                ")";


        String dmlInsert = "INSERT INTO pvuv_sink\n" +
                "SELECT\n" +
                "  DATE_FORMAT(ts, 'yyyy-MM-dd HH:00') dt,\n" +
                "  COUNT(*) AS pv,\n" +
                "  COUNT(DISTINCT user_id) AS uv\n" +
                "FROM user_log\n" +
                "GROUP BY DATE_FORMAT(ts, 'yyyy-MM-dd HH:00')";

        tEnv.sqlUpdate(ddlSource);

        tEnv.sqlUpdate(ddlSink);

        tEnv.sqlUpdate(dmlInsert);

        /**
         * execute
         */
        tEnv.execute("SQL Job");
    }
}
