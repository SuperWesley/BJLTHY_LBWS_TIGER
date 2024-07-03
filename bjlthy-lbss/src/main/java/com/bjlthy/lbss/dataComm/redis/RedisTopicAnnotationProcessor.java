package com.bjlthy.lbss.dataComm.redis;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class RedisTopicAnnotationProcessor implements BeanPostProcessor, ApplicationContextAware {

    private final Map<String, MessageListener> topicListeners = new HashMap<>();
    private ApplicationContext applicationContext;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RedisTopic.class)) {
            RedisTopic redisTopic = bean.getClass().getAnnotation(RedisTopic.class);
            String topic = redisTopic.value();
            if (bean instanceof MessageListener) {
                topicListeners.put(topic, (MessageListener) bean);
            }
        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public Map<String, MessageListener> getTopicListeners() {
        return topicListeners;
    }
}


