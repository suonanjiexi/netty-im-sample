#!/bin/bash

# Netty-SocketIO集群部署脚本

# 检查Redis是否运行
redis-cli ping > /dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "错误: Redis服务器未运行，请先启动Redis"
    exit 1
fi

# 检查MySQL是否运行
mysql -h localhost -u root -proot -e "SELECT 1" > /dev/null 2>&1
if [ $? -ne 0 ]; then
    echo "错误: MySQL服务器未运行或连接失败，请检查数据库配置"
    exit 1
fi

echo "=== Netty-SocketIO 集群部署 ==="

# 清理之前的进程
echo "清理之前的进程..."
pkill -f "netty-im-sample"

# 编译项目
echo "编译项目..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "错误: 项目编译失败"
    exit 1
fi

# 启动节点1
echo "启动节点1 (端口: 8080, SocketIO: 8081)..."
nohup java -jar target/netty-im-sample-0.0.1-SNAPSHOT.jar \
    --spring.profiles.active=cluster \
    --server.port=8080 \
    --socketio.port=8081 \
    --socketio.cluster.nodeId=node-1 \
    > logs/node1.log 2>&1 &

sleep 5

# 启动节点2
echo "启动节点2 (端口: 8082, SocketIO: 8083)..."
nohup java -jar target/netty-im-sample-0.0.1-SNAPSHOT.jar \
    --spring.profiles.active=cluster \
    --server.port=8082 \
    --socketio.port=8083 \
    --socketio.cluster.nodeId=node-2 \
    > logs/node2.log 2>&1 &

sleep 5

# 启动节点3
echo "启动节点3 (端口: 8084, SocketIO: 8085)..."
nohup java -jar target/netty-im-sample-0.0.1-SNAPSHOT.jar \
    --spring.profiles.active=cluster \
    --server.port=8084 \
    --socketio.port=8085 \
    --socketio.cluster.nodeId=node-3 \
    > logs/node3.log 2>&1 &

sleep 3

echo ""
echo "集群启动完成！"
echo ""
echo "节点信息:"
echo "  节点1: HTTP=8080, SocketIO=8081"
echo "  节点2: HTTP=8082, SocketIO=8083" 
echo "  节点3: HTTP=8084, SocketIO=8085"
echo ""
echo "查看日志:"
echo "  tail -f logs/node1.log"
echo "  tail -f logs/node2.log"
echo "  tail -f logs/node3.log"
echo ""
echo "停止集群:"
echo "  ./stop-cluster.sh"
echo ""

# 检查进程状态
echo "当前运行的节点:"
ps aux | grep "netty-im-sample" | grep -v grep