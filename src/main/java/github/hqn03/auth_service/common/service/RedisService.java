package github.hqn03.auth_service.common.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, Object value, long timeoutInMinutes) {
        redisTemplate.opsForValue().set(key, value, timeoutInMinutes, TimeUnit.MINUTES);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public void expire(String key, long timeoutInMinutes) {
        redisTemplate.expire(key, timeoutInMinutes, TimeUnit.MINUTES);
    }

    public void hIncr(String key, String field, long delta, long timeoutInMinutes) {
        redisTemplate.opsForHash().increment(key, field, delta);
        redisTemplate.expire(key, timeoutInMinutes, TimeUnit.MINUTES);
    }

    public void hSet(String key, String field, Object value, long timeoutInMinutes) {
        redisTemplate.opsForHash().put(key, field, value);
        redisTemplate.expire(key, timeoutInMinutes, TimeUnit.MINUTES);
    }

    public void hDelete(String key, String field){
        redisTemplate.opsForHash().delete(key, field);
    }

    public Map<Object, Object> hGetAll(String key) {
        return redisTemplate.opsForHash().entries(key);
    }
}
