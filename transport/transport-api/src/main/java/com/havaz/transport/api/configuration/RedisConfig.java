package com.havaz.transport.api.configuration;

import com.havaz.transport.api.service.impl.RedisMessageSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

@Configuration
public class RedisConfig {

    @Value("${havaz.redis.pubsub.channel}")
    private String channel;

    @Autowired
    private RedisMessageSubscriber redisMessageSubscriber;

    @Autowired
    private LettuceConnectionFactory lettuceConnectionFactory;

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic(channel);
    }

    @Bean
    public MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(redisMessageSubscriber);
    }

    @Bean
    public RedisMessageListenerContainer redisContainer() {
        RedisMessageListenerContainer container
                = new RedisMessageListenerContainer();
        container.setConnectionFactory(lettuceConnectionFactory);
        container.addMessageListener(messageListener(), topic());
        return container;
    }
}
