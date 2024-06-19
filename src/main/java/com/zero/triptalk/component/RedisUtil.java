package com.zero.triptalk.component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zero.triptalk.planner.entity.PlannerDocument;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;
    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper;
    private static final String MAP_CACHE_KEY = "planner";

    public String getData(String key) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        return valueOperations.get(key);
    }


    public void setData(String key, String value) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        valueOperations.set(key, value);
    }

    public void setDataExpire(String key, String value, long duration) {
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        Duration expireDuration = Duration.ofSeconds(duration);
        valueOperations.set(key, value, expireDuration);
    }

    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }

    // 조회수

    public Boolean isMember(String key, String plannerId) {
        SetOperations<String, String> setOps = stringRedisTemplate.opsForSet();
        return Boolean.TRUE.equals(setOps.isMember(key, plannerId));
    }

    public Boolean hasKey(String key) {
        return stringRedisTemplate.hasKey(key);
    }

    public void addUserSet(String key, Long plannerId) {
        SetOperations<String, String> setOps = stringRedisTemplate.opsForSet();
        setOps.add(key, String.valueOf(plannerId));
    }

    public void increaseViews(String plannerKey) {
        ValueOperations<String, String> valueOps = stringRedisTemplate.opsForValue();
        valueOps.increment(plannerKey);
    }

    public void setExpireMidnight(String key) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = LocalDateTime.of(now.toLocalDate().plusDays(1), LocalTime.MIDNIGHT);
        long secondsUntilMidnight = Duration.between(now, midnight).toSeconds();
        stringRedisTemplate.expire(key, secondsUntilMidnight, TimeUnit.SECONDS);
    }

    public void setPlannerTop6(String key, List<PlannerDocument> planners) {
        RMapCache<String, String> cache = redissonClient.getMapCache(MAP_CACHE_KEY, StringCodec.INSTANCE);

        try {
            String json = objectMapper.writeValueAsString(planners);
            cache.put(key, json, 2, TimeUnit.MINUTES);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error serializing PlannerDocument list", e);
        }
    }

    public List<PlannerDocument> getPlannerTop6(String key) {
        RMapCache<String, String> cache = redissonClient.getMapCache(MAP_CACHE_KEY, StringCodec.INSTANCE);
        String json = cache.get(key);

        if (json == null) {
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, PlannerDocument.class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}