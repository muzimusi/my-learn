package me.arjenlee.shirolearn.config;

import org.crazycake.shiro.RedisManager;
import redis.clients.jedis.JedisCluster;

public class RedisClusterManager extends RedisManager {

    private JedisCluster jedisCluster;

    private int expire = 0;

    @Override
    public byte[] get(byte[] key) {
        byte[] value;
        try {
            value = jedisCluster.get(key);
        } catch (Exception e) {
            throw new RuntimeException("redis operation error:", e);
        } finally {
        }

        return value;
    }

    @Override
    public byte[] set(byte[] key, byte[] value) {
        try {
            jedisCluster.set(key, value);
            if (this.expire != 0) {
                jedisCluster.expire(key, this.expire);
            }
        } finally {
        }

        return value;
    }

    @Override
    public byte[] set(byte[] key, byte[] value, int expire) {
        try {
            jedisCluster.set(key, value);
            if (expire != 0) {
                jedisCluster.expire(key, expire);
            }
        } finally {
        }

        return value;
    }

    @Override
    public void del(byte[] key) {
        try {
            jedisCluster.del(key);
        } finally {
        }

    }

    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public void setJedisCluster(JedisCluster jedisCluster) {
        this.jedisCluster = jedisCluster;
    }

    @Override
    public int getExpire() {
        return expire;
    }

    @Override
    public void setExpire(int expire) {
        this.expire = expire;
    }
}
