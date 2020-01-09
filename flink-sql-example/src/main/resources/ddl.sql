# 数据库
CREATE DATABASE flink_test DEFAULT CHARACTER SET utf8 DEFAULT COLLATE utf8_general_ci;
GRANT ALL PRIVILEGES ON flink_test.* TO 'root'@'%' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON flink_test.* TO 'root'@'localhost' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON flink_test.* TO 'root'@'mini' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON flink_test.* TO 'root'@'hadoop01' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON flink_test.* TO 'root'@'hadoop02' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON flink_test.* TO 'root'@'hadoop03' IDENTIFIED BY 'root';
flush privileges;

# 数据表
DROP TABLE IF EXISTS `pvuv_sink`;
CREATE TABLE `pvuv_sink` (
  `dt` varchar(20) NOT NULL,
  `pv` BIGINT ,
  `uv` BIGINT ,
  PRIMARY KEY (`dt`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `access_sink`;
CREATE TABLE `access_sink` (
  `ip` varchar(20) NOT NULL,
  `hostname` varchar(20) ,
  `dt` varchar(20) NOT NULL,
  `pv` BIGINT ,
  `uv` BIGINT ,
  PRIMARY KEY (`ip`, `dt`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;