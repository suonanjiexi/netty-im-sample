package com.example.nettyim.cluster;

import com.example.nettyim.entity.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 集群消息路由器
 * 负责在不同SocketIO节点之间路由消息
 */
@Component
public class ClusterMessageRouter {
    
    private static final Logger log = LoggerFactory.getLogger(ClusterMessageRouter.class);
    
    private static final String CLUSTER_MESSAGE_CHANNEL = "socketio:cluster:message";
    private static final String CLUSTER_EVENT_CHANNEL = "socketio:cluster:event";
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisMessageListenerContainer listenerContainer;
    private final ObjectMapper objectMapper;
    private final List<ClusterMessageListener> messageListeners = new CopyOnWriteArrayList<>();
    
    public ClusterMessageRouter(RedisTemplate<String, Object> redisTemplate,
                               RedisMessageListenerContainer listenerContainer,
                               ObjectMapper objectMapper) {
        this.redisTemplate = redisTemplate;
        this.listenerContainer = listenerContainer;
        this.objectMapper = objectMapper;
    }
    
    @PostConstruct
    public void init() {
        // 订阅集群消息频道
        MessageListenerAdapter messageAdapter = new MessageListenerAdapter(this, "handleClusterMessage");
        listenerContainer.addMessageListener(messageAdapter, new PatternTopic(CLUSTER_MESSAGE_CHANNEL));
        
        MessageListenerAdapter eventAdapter = new MessageListenerAdapter(this, "handleClusterEvent");
        listenerContainer.addMessageListener(eventAdapter, new PatternTopic(CLUSTER_EVENT_CHANNEL));
        
        log.info("集群消息路由器初始化完成");
    }
    
    @PreDestroy
    public void destroy() {
        listenerContainer.stop();
        log.info("集群消息路由器已停止");
    }
    
    /**
     * 广播消息到集群中的所有节点
     */
    public void broadcastMessage(ClusterMessage clusterMessage) {
        try {
            String messageJson = objectMapper.writeValueAsString(clusterMessage);
            redisTemplate.convertAndSend(CLUSTER_MESSAGE_CHANNEL, messageJson);
            log.debug("广播集群消息: {}", clusterMessage.getType());
        } catch (JsonProcessingException e) {
            log.error("序列化集群消息失败", e);
        }
    }
    
    /**
     * 发送事件到集群中的所有节点
     */
    public void broadcastEvent(ClusterEvent clusterEvent) {
        try {
            String eventJson = objectMapper.writeValueAsString(clusterEvent);
            redisTemplate.convertAndSend(CLUSTER_EVENT_CHANNEL, eventJson);
            log.debug("广播集群事件: {}", clusterEvent.getType());
        } catch (JsonProcessingException e) {
            log.error("序列化集群事件失败", e);
        }
    }
    
    /**
     * 添加消息监听器
     */
    public void addMessageListener(ClusterMessageListener listener) {
        messageListeners.add(listener);
    }
    
    /**
     * 移除消息监听器
     */
    public void removeMessageListener(ClusterMessageListener listener) {
        messageListeners.remove(listener);
    }
    
    /**
     * 处理接收到的集群消息
     */
    public void handleClusterMessage(String messageJson) {
        try {
            ClusterMessage clusterMessage = objectMapper.readValue(messageJson, ClusterMessage.class);
            log.debug("接收到集群消息: {}", clusterMessage.getType());
            
            // 通知所有监听器
            for (ClusterMessageListener listener : messageListeners) {
                try {
                    listener.onClusterMessage(clusterMessage);
                } catch (Exception e) {
                    log.error("处理集群消息失败", e);
                }
            }
        } catch (JsonProcessingException e) {
            log.error("反序列化集群消息失败", e);
        }
    }
    
    /**
     * 处理接收到的集群事件
     */
    public void handleClusterEvent(String eventJson) {
        try {
            ClusterEvent clusterEvent = objectMapper.readValue(eventJson, ClusterEvent.class);
            log.debug("接收到集群事件: {}", clusterEvent.getType());
            
            // 通知所有监听器
            for (ClusterMessageListener listener : messageListeners) {
                try {
                    listener.onClusterEvent(clusterEvent);
                } catch (Exception e) {
                    log.error("处理集群事件失败", e);
                }
            }
        } catch (JsonProcessingException e) {
            log.error("反序列化集群事件失败", e);
        }
    }
    
    /**
     * 集群消息监听器接口
     */
    public interface ClusterMessageListener {
        void onClusterMessage(ClusterMessage message);
        void onClusterEvent(ClusterEvent event);
    }
}