package com.nailcankucuk.example.redispubsub.config;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;


/**
 * @author nailcankucuk@gmail.com
 * @date 04.11.2018
 */
@EnableRedisRepositories
@Configuration
public class RedisConfiguration {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Value("${spring.redis.password}")
    private String password;

    /*
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
        JedisConnectionFactory factory = new JedisConnectionFactory(configuration);
        return factory;
    }
    */

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(30);
        jedisPoolConfig.setMinIdle(10);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        return jedisPoolConfig;
    }

    @SuppressWarnings("deprecation")
    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig());
        jedisConnectionFactory.setHostName(redisHost);
        jedisConnectionFactory.setPort(redisPort);
        jedisConnectionFactory.setPassword(password);
        jedisConnectionFactory.setUsePool(true);
        return jedisConnectionFactory;
    }
    

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        final RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }

    @Bean
    HashOperations hashOperations() {
        return redisTemplate().opsForHash();
    }

    // StringBuilder stringBuilder = new StringBuilder();
    // Map<String, String> intro = hashOperations.entries("msh:keyvalue");
    // stringBuilder.append("\n******자기소개********");
    // stringBuilder.append("\n이름은 ");
    // stringBuilder.append(intro.get("name"));
    // stringBuilder.append("\n나이는 ");
    // stringBuilder.append(intro.get("age"));
    // System.out.println(stringBuilder.toString());


    @Bean
    Jedis jedis() {
        return new Jedis(redisHost, redisPort);
    }
}