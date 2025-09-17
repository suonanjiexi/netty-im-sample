#!/bin/bash

# 停止Netty-SocketIO集群

echo "=== 停止 Netty-SocketIO 集群 ==="

# 查找并停止所有相关进程
PIDS=$(ps aux | grep "netty-im-sample" | grep -v grep | awk '{print $2}')

if [ -z "$PIDS" ]; then
    echo "没有找到运行中的节点"
else
    echo "停止进程: $PIDS"
    kill -15 $PIDS
    
    # 等待进程优雅关闭
    sleep 5
    
    # 检查是否还有进程在运行
    REMAINING=$(ps aux | grep "netty-im-sample" | grep -v grep | awk '{print $2}')
    if [ ! -z "$REMAINING" ]; then
        echo "强制停止剩余进程: $REMAINING"
        kill -9 $REMAINING
    fi
fi

echo "集群已停止"

# 显示结果
RUNNING=$(ps aux | grep "netty-im-sample" | grep -v grep)
if [ -z "$RUNNING" ]; then
    echo "✓ 所有节点已成功停止"
else
    echo "⚠ 仍有进程在运行:"
    echo "$RUNNING"
fi