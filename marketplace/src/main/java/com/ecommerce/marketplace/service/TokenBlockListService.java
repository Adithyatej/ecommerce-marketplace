package com.ecommerce.marketplace.service;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TokenBlockListService {

    private final StringRedisTemplate redis;

    public TokenBlockListService(StringRedisTemplate redis) {
        this.redis=redis;
}

    public void block(String jti, long remainingTtlSeconds) {
        redis.opsForValue().set("blocklist:" + jti, "1", Duration.ofSeconds(remainingTtlSeconds));
    }
    public boolean isBlocked(String jti) {
        return redis.hasKey("blocklist:" + jti);
    }
}
