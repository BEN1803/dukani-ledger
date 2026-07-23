package com.dukaniledger.config;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {


    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(){
        return builder -> builder
                .withCacheConfiguration("products", cacheConfig(Duration.ofMinutes(10)))
                .withCacheConfiguration("categories", cacheConfig(Duration.ofMinutes(30)))
                .withCacheConfiguration("stock", cacheConfig(Duration.ofMinutes(2)));
    }

    private RedisCacheConfiguration cacheConfig(Duration ttl){
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .serializeKeysWith(
                        RedisSerializationContext.SerializationPair
                                .fromSerializer(new StringRedisSerializer())
                )
                .disableCachingNullValues();
    }
}