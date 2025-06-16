package com.yicj.web.config;

import com.yicj.web.service._02TestSubscriber;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName("127.0.0.1");
        config.setPort(6379);
        //config.setPassword("your_password"); // 如果有密码的话
        return new LettuceConnectionFactory(config);
    }


    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        // 使用 String 序列化键
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        // 默认序列化值
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new StringRedisSerializer());

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }


    // 自定义监听容器，设置listenerAdapterA监听的频道_02TestChannel。
    @Bean
    public RedisMessageListenerContainer container(
        RedisConnectionFactory connectionFactory,
        MessageListenerAdapter listenerAdapterA //使用函数名称来区分监听器
        //,MessageListenerAdapter listenerAdapterB
        //,MessageListenerAdapter listenerAdapterC
        ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapterA,new ChannelTopic("_02TestChannel"));  //绑定监听器和频道
        return container;
    }

    /**
     * Redis 频道名称
     */
    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic("_02TestChannel");
    }


    // 为_02TestSubscriber自定义监听器，指定监听器调用的_02TestSubscriber的处理函数_02TestHandleMessage
    @Bean
    public MessageListenerAdapter listenerAdapterA(_02TestSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber,"_02TestHandleMessage");  //前面指定类，后面指定函数名称
    }

}
