package com.example.nettyim.cluster;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 集群消息
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClusterMessage {
    
    @JsonProperty("type")
    private String type;
    
    @JsonProperty("targetUserId")
    private Long targetUserId;
    
    @JsonProperty("targetUserIds")
    private java.util.List<Long> targetUserIds;
    
    @JsonProperty("event")
    private String event;
    
    @JsonProperty("data")
    private Object data;
    
    @JsonProperty("nodeId")
    private String nodeId;
    
    @JsonProperty("timestamp")
    private Long timestamp;
    
    public ClusterMessage() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public ClusterMessage(String type, String event, Object data) {
        this();
        this.type = type;
        this.event = event;
        this.data = data;
    }
    
    // Getter and Setter methods
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public Long getTargetUserId() {
        return targetUserId;
    }
    
    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }
    
    public java.util.List<Long> getTargetUserIds() {
        return targetUserIds;
    }
    
    public void setTargetUserIds(java.util.List<Long> targetUserIds) {
        this.targetUserIds = targetUserIds;
    }
    
    public String getEvent() {
        return event;
    }
    
    public void setEvent(String event) {
        this.event = event;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
    
    public String getNodeId() {
        return nodeId;
    }
    
    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
    @Override
    public String toString() {
        return "ClusterMessage{" +
                "type='" + type + '\'' +
                ", targetUserId=" + targetUserId +
                ", event='" + event + '\'' +
                ", nodeId='" + nodeId + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}