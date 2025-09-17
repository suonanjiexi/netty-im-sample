package com.example.nettyim.cluster;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 集群事件
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClusterEvent {
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("userId")
    private Long userId;
    
    @JsonProperty("sessionId") 
    private String sessionId;
    
    @JsonProperty("nodeId")
    private String nodeId;
    
    @JsonProperty("data")
    private Object data;
    
    @JsonProperty("timestamp")
    private Long timestamp;
    
    public ClusterEvent() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public ClusterEvent(String type, Long userId, String nodeId) {
        this();
        this.type = type;
        this.userId = userId;
        this.nodeId = nodeId;
    }
    
    // Getter and Setter methods
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    
    public String getNodeId() {
        return nodeId;
    }
    
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "ClusterEvent{" +
                "type='" + type + '\'' +
                ", userId=" + userId +
                ", sessionId='" + sessionId + '\'' +
                ", nodeId='" + nodeId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}