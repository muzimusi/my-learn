package me.arjenlee.shirolearn.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

@Configuration
public class RedisConfig extends CachingConfigurerSupport {

    @Value("${spring.redis.cluster.nodes}")
    private String clusterNodes;

    @Bean
    @ConfigurationProperties(prefix = "spring.redis")
    public JedisPoolConfig getRedisConfig() {
        JedisPoolConfig config = new JedisPoolConfig();
        return config;
    }

    @Bean
    public RedisClusterConfiguration getClusterConfig() {
        Assert.notNull(clusterNodes, "Cannot set cluster nodes to \'null\'.");
        String[] serverArray = clusterNodes.split(",");
        Set<RedisNode> nodes = new HashSet<>();

        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new RedisNode(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
        }
        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration();
        clusterConfig.setClusterNodes(nodes);
        return clusterConfig;
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.redis")
    public JedisConnectionFactory getConnectionFactory() {
        RedisClusterConfiguration clusterConfig = getClusterConfig();
        JedisConnectionFactory factory = new JedisConnectionFactory(clusterConfig);
        JedisPoolConfig config = getRedisConfig();
        factory.setDatabase(2);
        factory.setPoolConfig(config);
        return factory;
    }

    @Bean
    public RedisTemplate<?, ?> getRedisTemplate() {
        RedisTemplate<?, ?> template = new StringRedisTemplate(getConnectionFactory());
        return template;
    }

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
//        cacheManager.setDefaultExpiration(3600);
        return cacheManager;
    }

}
