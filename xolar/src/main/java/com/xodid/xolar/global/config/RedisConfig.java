package com.xodid.xolar.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    /**
     * RedisConnectionFactory 빈 설정
     *
     * Redis와의 연결을 생성하고 관리함
     * LettuceConnectionFactory를 사용하여 Redis 서버와 통신할 수 있도록 설정
     *
     * @return RedisConnectionFactory 객체
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        // Redis 서버의 host와 port 설정
        return new LettuceConnectionFactory(host, port);
    }

    /**
     * RedisTemplate 빈 설정
     *
     * Redis에 데이터를 저장하고 조회하기 위한 RedisTemplate을 설정
     * Redis에서 데이터를 읽고 쓸 때 키는 문자열 형식, 값은 JSON 형식으로 직렬화하여 저장
     *
     * @param redisConnectionFactory
     * @return 설정된 RedisTemplate 객체
     */
    @Bean
    public RedisTemplate<String , Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String , Object> template = new RedisTemplate<>();
        // Redis 연결 설정
        template.setConnectionFactory(redisConnectionFactory);
        // 키를 문자열 형식으로 직렬화
        template.setKeySerializer(new StringRedisSerializer());
        // 값을 JSON 형식으로 직렬화
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template;
    }
}