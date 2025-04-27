package malla.dipesh.journalApp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

    private final RedisTemplate redisTemplate;

    public RedisService(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public <T> T get(String key, Class<T> entityClass) {

        try{
            Object o = redisTemplate.opsForValue().get(key);
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.readValue(o.toString(),entityClass);
        }catch (Exception e){
            log.error("exception while getting data from redis",e);
            return null;
        }
    }

    public void set(String key, Object o, Long ttl ) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key,json,ttl,TimeUnit.SECONDS);

        }catch (Exception e){
            log.error("exception while setting data to redis",e);
        }
    }
}
