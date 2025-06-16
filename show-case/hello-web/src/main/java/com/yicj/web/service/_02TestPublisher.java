package com.yicj.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class _02TestPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    // RedisTemplate 通过 Spring 上下文获取，自动注入
    // 该RedisTemple对象在 RedisConfig中注册
    // 不想定义可以使用SimpMessagingTemplate代替
    @Autowired
    public _02TestPublisher(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // SimpMessagingTemplate

    /**
     * 发布消息到 Redis 频道
     * 通过 Channel 来发布信息，这里用 String 简单表示消息
     */
    public void publish(String channel, String message) {
        redisTemplate.convertAndSend(channel, message);
    }
}
