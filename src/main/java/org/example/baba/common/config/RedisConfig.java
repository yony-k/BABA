package org.example.baba.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Value("${spring.data.redis.host}")
  private String host;

  @Value("${spring.data.redis.port}")
  private int port;

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    return new LettuceConnectionFactory(host, port);
  }

  // Redis 데이터의 직렬화 및 역직렬화와 CRUD 작업을 관리
  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    // RedisConnectionFactory 와 연결
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory());

    // 키 직렬화
    redisTemplate.setKeySerializer(new StringRedisSerializer());

    // RegiterDTO 저장을 위해 json 형식으로 값 직렬화
    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));
    return redisTemplate;
  }
}
