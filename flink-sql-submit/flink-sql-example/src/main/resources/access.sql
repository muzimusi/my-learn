-- -- 开启 mini-batch
-- SET table.exec.mini-batch.enabled=true;
-- -- mini-batch的时间间隔，即作业需要额外忍受的延迟
-- SET table.exec.mini-batch.allow-latency=1s;
-- -- 一个 mini-batch 中允许最多缓存的数据
-- SET table.exec.mini-batch.size=1000;
-- -- 开启 local-global 优化
-- SET table.optimizer.agg-phase-strategy=TWO_PHASE;
--
-- -- 开启 distinct agg 切分
-- SET table.optimizer.distinct-agg.split.enabled=true;


-- source
CREATE TABLE access_log (
    source_ip VARCHAR,
    target_ip VARCHAR,
    behavior VARCHAR,
    ts TIMESTAMP
) WITH (
    'connector.type' = 'kafka',
    'connector.version' = 'universal',
    'connector.topic' = 'flink_test_user_access',
    'connector.startup-mode' = 'earliest-offset',
    'connector.properties.0.key' = 'zookeeper.connect',
    'connector.properties.0.value' = 'hadoop-01:2181,hadoop-02:2181,hadoop-03:2181',
    'connector.properties.1.key' = 'bootstrap.servers',
    'connector.properties.1.value' = 'hadoop-01:9092,hadoop-02:9092,hadoop-03:9092',
    'update-mode' = 'append',
    'format.type' = 'json',
    'format.derive-schema' = 'true'
);

-- sink
CREATE TABLE access_sink (
    ip VARCHAR,
    host VARCHAR,
    dt VARCHAR,
    pv BIGINT,
    uv BIGINT
) WITH (
    'connector.type' = 'jdbc',
    'connector.url' = 'jdbc:mysql://hadoop-01:3306/flink_test',
    'connector.table' = 'access_sink',
    'connector.username' = 'root',
    'connector.password' = 'root',
    'connector.write.flush.max-rows' = '1'
);

INSERT INTO access_sink
SELECT
  target_ip AS ip,
  Ip2HostName(target_ip) AS host,
  DATE_FORMAT(ts, 'yyyy-MM-dd HH:mm') AS dt,
  COUNT(*) AS pv,
  COUNT(DISTINCT source_ip) AS uv
FROM access_log
GROUP BY target_ip, DATE_FORMAT(ts, 'yyyy-MM-dd HH:mm');