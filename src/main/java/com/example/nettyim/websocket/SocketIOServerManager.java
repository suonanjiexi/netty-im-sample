package com.example.nettyim.websocket;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.example.nettyim.cluster.ClusterEvent;
import com.example.nettyim.cluster.ClusterMessage;
import com.example.nettyim.cluster.ClusterMessageRouter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SocketIO服务器管理器 - 支持集群部署
 */
@Component
public class SocketIOServerManager implements ClusterMessageRouter.ClusterMessageListener {
    
    private static final Logger log = LoggerFactory.getLogger(SocketIOServerManager.class);
    
    private final SocketIOServer socketIOServer;
    private final ClusterMessageRouter clusterMessageRouter;
    
    @Value("${socketio.cluster.enabled:false}")
    private Boolean clusterEnabled;
    
    @Value("${socketio.port:8081}")
    private Integer port;
    
    // 存储用户ID与客户端的映射关系
    private final ConcurrentHashMap<Long, SocketIOClient> userClients = new ConcurrentHashMap<>();
    
    // 存储客户端与用户ID的映射关系
    private final ConcurrentHashMap<String, Long> clientUsers = new ConcurrentHashMap<>();
    
    // 当前节点ID
    private String nodeId;
    
    public SocketIOServerManager(SocketIOServer socketIOServer, 
                                 @Autowired(required = false) ClusterMessageRouter clusterMessageRouter) {
        this.socketIOServer = socketIOServer;
        this.clusterMessageRouter = clusterMessageRouter;
        this.nodeId = generateNodeId();
    }
    
    @PostConstruct
    public void startServer() {
        // 注册集群消息监听器
        if (clusterEnabled) {
            clusterMessageRouter.addMessageListener(this);
        }
        
        // 设置连接监听器
        socketIOServer.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                log.info("客户端连接: {}", client.getSessionId());
                
                // 从请求参数中获取用户ID
                String userIdStr = client.getHandshakeData().getSingleUrlParam("userId");
                if (userIdStr != null && !userIdStr.isEmpty()) {
                    try {
                        Long userId = Long.parseLong(userIdStr);
                        
                        // 移除旧连接（如果存在）
                        SocketIOClient oldClient = userClients.get(userId);
                        if (oldClient != null && oldClient.isChannelOpen()) {
                            clientUsers.remove(oldClient.getSessionId().toString());
                            oldClient.disconnect();
                        }
                        
                        // 存储新连接
                        userClients.put(userId, client);
                        clientUsers.put(client.getSessionId().toString(), userId);
                        
                        log.info("用户 {} 已连接到节点 {}", userId, nodeId);
                        
                        // 广播用户上线事件到集群
                        if (clusterEnabled) {
                            ClusterEvent event = new ClusterEvent("USER_ONLINE", userId, nodeId);
                            event.setSessionId(client.getSessionId().toString());
                            clusterMessageRouter.broadcastEvent(event);
                        }
                        
                    } catch (NumberFormatException e) {
                        log.error("无效的用户ID: {}", userIdStr);
                        client.disconnect();
                    }
                } else {
                    log.warn("客户端连接时未提供用户ID");
                    client.disconnect();
                }
            }
        });
        
        // 设置断开连接监听器
        socketIOServer.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {
                String sessionId = client.getSessionId().toString();
                Long userId = clientUsers.remove(sessionId);
                
                if (userId != null) {
                    userClients.remove(userId);
                    log.info("用户 {} 从节点 {} 断开连接", userId, nodeId);
                    
                    // 广播用户下线事件到集群
                    if (clusterEnabled) {
                        ClusterEvent event = new ClusterEvent("USER_OFFLINE", userId, nodeId);
                        event.setSessionId(sessionId);
                        clusterMessageRouter.broadcastEvent(event);
                    }
                }
                
                log.info("客户端断开连接: {}", sessionId);
            }
        });
        
        // 启动服务器
        socketIOServer.start();
        log.info("SocketIO服务器启动成功，节点ID: {}, 端口: {}, 集群模式: {}", nodeId, socketIOServer.getConfiguration().getPort(), clusterEnabled);
    }
    
    @PreDestroy
    public void stopServer() {
        socketIOServer.stop();
        log.info("SocketIO服务器已停止，节点ID: {}", nodeId);
    }
    
    /**
     * 生成节点ID
     */
    private String generateNodeId() {
        try {
            String hostName = InetAddress.getLocalHost().getHostName();
            return hostName + ":" + port + ":" + System.currentTimeMillis();
        } catch (UnknownHostException e) {
            return "unknown:" + port + ":" + System.currentTimeMillis();
        }
    }
    
    /**
     * 获取用户的客户端连接
     */
    public SocketIOClient getUserClient(Long userId) {
        return userClients.get(userId);
    }
    
    /**
     * 检查用户是否在线
     */
    public boolean isUserOnline(Long userId) {
        SocketIOClient client = userClients.get(userId);
        return client != null && client.isChannelOpen();
    }
    
    /**
     * 向指定用户发送消息
     */
    public void sendToUser(Long userId, String event, Object data) {
        SocketIOClient client = getUserClient(userId);
        if (client != null && client.isChannelOpen()) {
            client.sendEvent(event, data);
        } else if (clusterEnabled) {
            // 如果本地没有连接，尝试通过集群路由
            ClusterMessage clusterMessage = new ClusterMessage("SEND_TO_USER", event, data);
            clusterMessage.setTargetUserId(userId);
            clusterMessage.setNodeId(nodeId);
            clusterMessageRouter.broadcastMessage(clusterMessage);
        }
    }
    
    /**
     * 向群组中的所有在线用户发送消息
     */
    public void sendToGroup(java.util.List<Long> userIds, String event, Object data) {
        for (Long userId : userIds) {
            SocketIOClient client = getUserClient(userId);
            if (client != null && client.isChannelOpen()) {
                client.sendEvent(event, data);
            }
        }
        
        // 在集群模式下，也需要广播到其他节点
        if (clusterEnabled) {
            ClusterMessage clusterMessage = new ClusterMessage("SEND_TO_GROUP", event, data);
            clusterMessage.setTargetUserIds(userIds);
            clusterMessage.setNodeId(nodeId);
            clusterMessageRouter.broadcastMessage(clusterMessage);
        }
    }
    
    /**
     * 广播消息给所有在线用户
     */
    public void broadcast(String event, Object data) {
        socketIOServer.getBroadcastOperations().sendEvent(event, data);
        
        // 在集群模式下，也需要广播到其他节点
        if (clusterEnabled) {
            ClusterMessage clusterMessage = new ClusterMessage("BROADCAST", event, data);
            clusterMessage.setNodeId(nodeId);
            clusterMessageRouter.broadcastMessage(clusterMessage);
        }
    }
    
    /**
     * 获取在线用户数量
     */
    public int getOnlineUserCount() {
        return userClients.size();
    }
    
    /**
     * 获取所有在线用户ID
     */
    public java.util.Set<Long> getOnlineUserIds() {
        return userClients.keySet();
    }
    
    /**
     * 获取节点ID
     */
    public String getNodeId() {
        return nodeId;
    }
    
    // 实现集群消息监听器接口
    @Override
    public void onClusterMessage(ClusterMessage message) {
        // 忽略本节点发送的消息
        if (nodeId.equals(message.getNodeId())) {
            return;
        }
        
        log.debug("接收到集群消息: {} from {}", message.getType(), message.getNodeId());
        
        switch (message.getType()) {
            case "SEND_TO_USER":
                if (message.getTargetUserId() != null) {
                    SocketIOClient client = getUserClient(message.getTargetUserId());
                    if (client != null && client.isChannelOpen()) {
                        client.sendEvent(message.getEvent(), message.getData());
                    }
                }
                break;
                
            case "SEND_TO_GROUP":
                if (message.getTargetUserIds() != null) {
                    for (Long userId : message.getTargetUserIds()) {
                        SocketIOClient client = getUserClient(userId);
                        if (client != null && client.isChannelOpen()) {
                            client.sendEvent(message.getEvent(), message.getData());
                        }
                    }
                }
                break;
                
            case "BROADCAST":
                socketIOServer.getBroadcastOperations().sendEvent(message.getEvent(), message.getData());
                break;
        }
    }
    
    @Override
    public void onClusterEvent(ClusterEvent event) {
        // 忽略本节点发送的事件
        if (nodeId.equals(event.getNodeId())) {
            return;
        }
        
        log.debug("接收到集群事件: {} from {}", event.getType(), event.getNodeId());
        
        switch (event.getType()) {
            case "USER_ONLINE":
                log.info("用户 {} 在节点 {} 上线", event.getUserId(), event.getNodeId());
                break;
                
            case "USER_OFFLINE":
                log.info("用户 {} 从节点 {} 下线", event.getUserId(), event.getNodeId());
                break;
        }
    }
}