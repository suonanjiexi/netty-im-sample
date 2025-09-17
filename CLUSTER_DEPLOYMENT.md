# Netty-SocketIO 集群部署指南

## 概述

本项目已成功实现了Netty-SocketIO的集群部署功能，支持多节点横向扩展，确保高可用性和负载分担。

## 架构特点

### 1. **集群存储**
- 使用 **Redis** 作为会话存储和消息队列
- 通过 **Redisson** 实现分布式会话管理
- 支持跨节点的用户连接信息共享

### 2. **消息路由**
- 实现跨节点消息路由机制
- 支持私聊、群聊消息的集群广播
- 用户在线状态的集群同步

### 3. **负载均衡**
- 支持多个SocketIO服务器实例
- 每个节点独立处理客户端连接
- 通过Redis实现节点间通信

## 部署配置

### 1. **环境要求**
```bash
- JDK 21
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+
```

### 2. **集群配置**
在 `application-cluster.yml` 中启用集群模式：
```yaml
socketio:
  cluster:
    enabled: true  # 启用集群模式
    nodeId: ${NODE_ID:node-1}  # 节点ID
```

### 3. **启动集群**
```bash
# 启动三节点集群
./start-cluster.sh

# 停止集群
./stop-cluster.sh
```

### 4. **节点配置**
- **节点1**: HTTP=8080, SocketIO=8081
- **节点2**: HTTP=8082, SocketIO=8083  
- **节点3**: HTTP=8084, SocketIO=8085

## 功能特性

### 1. **会话共享**
- 用户连接信息存储在Redis中
- 支持跨节点的会话访问
- 自动处理节点故障转移

### 2. **消息路由**
- 智能消息路由到目标用户所在节点
- 群聊消息自动广播到所有相关节点
- 支持实时消息推送

### 3. **在线状态同步**
- 用户上线/下线事件集群广播
- 实时同步在线用户列表
- 支持跨节点在线状态查询

## 测试指南

### 1. **连接测试**
```javascript
// 连接不同节点
const socket1 = io('http://localhost:8081?userId=1');
const socket2 = io('http://localhost:8083?userId=2');
const socket3 = io('http://localhost:8085?userId=3');
```

### 2. **跨节点消息测试**
```javascript
// 用户1(节点1)向用户2(节点2)发送消息
socket1.emit('send_message', {
    toUserId: 2,
    messageType: 1,
    content: '跨节点消息测试'
});

// 用户2应该能收到消息
socket2.on('private_message', (data) => {
    console.log('收到跨节点消息:', data);
});
```

### 3. **群聊集群测试**
```javascript
// 创建群聊，成员分布在不同节点
socket1.emit('send_message', {
    groupId: 1,
    messageType: 1,
    content: '群聊集群测试'
});
```

## 监控和调试

### 1. **日志查看**
```bash
# 查看各节点日志
tail -f logs/node1.log
tail -f logs/node2.log  
tail -f logs/node3.log
```

### 2. **Redis监控**
```bash
# 监控Redis键空间
redis-cli monitor

# 查看SocketIO相关键
redis-cli keys "redisson*"
redis-cli keys "socketio*"
```

### 3. **集群状态检查**
```bash
# 检查运行中的节点
ps aux | grep netty-im-sample

# 检查端口占用
netstat -tlnp | grep -E "8080|8081|8082|8083|8084|8085"
```

## 扩展和优化

### 1. **增加节点**
修改 `start-cluster.sh` 添加更多节点：
```bash
# 启动节点4
nohup java -jar target/netty-im-sample-0.0.1-SNAPSHOT.jar \
    --spring.profiles.active=cluster \
    --server.port=8086 \
    --socketio.port=8087 \
    --socketio.cluster.nodeId=node-4 \
    > logs/node4.log 2>&1 &
```

### 2. **负载均衡配置**
使用Nginx进行HTTP负载均衡：
```nginx
upstream netty_im_backend {
    server localhost:8080;
    server localhost:8082;
    server localhost:8084;
}

upstream netty_im_socketio {
    ip_hash;  # 保持会话亲和性
    server localhost:8081;
    server localhost:8083;
    server localhost:8085;
}
```

### 3. **性能优化**
- 调整Redis连接池大小
- 优化SocketIO worker线程数
- 配置适当的心跳间隔

## 故障排除

### 1. **常见问题**
- Redis连接失败：检查Redis服务状态
- 端口冲突：修改端口配置
- 消息路由失败：检查节点ID配置

### 2. **集群恢复**
```bash
# 重启整个集群
./stop-cluster.sh
./start-cluster.sh

# 单节点重启
kill -15 <pid>
# 然后手动启动对应节点
```

## 总结

通过以上配置，Netty-SocketIO已具备完整的集群部署能力：

✅ **Redis集群存储** - 会话和消息的分布式存储  
✅ **跨节点消息路由** - 智能消息转发机制  
✅ **负载均衡支持** - 多节点横向扩展  
✅ **故障转移** - 节点故障自动恢复  
✅ **实时同步** - 在线状态集群同步  

系统现在可以支持大规模并发用户和高可用性要求的生产环境部署。