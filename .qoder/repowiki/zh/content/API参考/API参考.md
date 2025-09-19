# API参考

<cite>
**本文档中引用的文件**  
- [UserController.java](file://src/main/java/com/example/nettyim/controller/UserController.java)
- [FriendshipController.java](file://src/main/java/com/example/nettyim/controller/FriendshipController.java)
- [GroupController.java](file://src/main/java/com/example/nettyim/controller/GroupController.java)
- [MessageController.java](file://src/main/java/com/example/nettyim/controller/MessageController.java)
- [SocketIOEventHandler.java](file://src/main/java/com/example/nettyim/websocket/SocketIOEventHandler.java)
- [Result.java](file://src/main/java/com/example/nettyim/dto/Result.java)
- [UserLoginDTO.java](file://src/main/java/com/example/nettyim/dto/UserLoginDTO.java)
- [SendMessageDTO.java](file://src/main/java/com/example/nettyim/dto/SendMessageDTO.java)
- [JwtUtils.java](file://src/main/java/com/example/nettyim/utils/JwtUtils.java)
</cite>

## 目录
1. [简介](#简介)
2. [REST API](#rest-api)
   - [用户管理](#用户管理)
   - [好友管理](#好友管理)
   - [群组管理](#群组管理)
   - [消息管理](#消息管理)
3. [WebSocket API](#websocket-api)
   - [事件列表](#事件列表)
   - [数据结构](#数据结构)
   - [响应格式](#响应格式)
4. [认证与安全](#认证与安全)
5. [通用响应格式](#通用响应格式)

## 简介
本API参考文档详细描述了即时通讯系统提供的RESTful API和WebSocket实时通信接口。REST API用于执行初始化操作，如用户登录、获取历史消息等；WebSocket事件用于实时交互，如发送消息、状态通知等。所有REST端点均返回统一的`Result`格式响应，认证方式为JWT Bearer Token。

## REST API

### 用户管理

#### 用户注册
- **HTTP方法**: `POST`
- **URL路径**: `/user/register`
- **请求头**: `Authorization: Bearer <token>`（可选）
- **请求体**: `UserRegisterDTO`
- **成功响应**: `200 OK`
- **错误响应**: `400 Bad Request`（参数验证失败）

**Section sources**
- [UserController.java](file://src/main/java/com/example/nettyim/controller/UserController.java#L25-L32)

#### 用户登录
- **HTTP方法**: `POST`
- **URL路径**: `/user/login`
- **请求头**: 无
- **请求体**: `UserLoginDTO`
- **成功响应**: `200 OK`
- **错误响应**: `401 Unauthorized`（认证失败）

**请求体示例**
```json
{
  "account": "zhangsan",
  "password": "123456"
}
```

**响应示例**
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIs...",
    "user": {
      "id": 1,
      "username": "zhangsan",
      "email": "zhangsan@example.com"
    }
  },
  "timestamp": 1700000000000
}
```

**Section sources**
- [UserController.java](file://src/main/java/com/example/nettyim/controller/UserController.java#L34-L44)
- [UserLoginDTO.java](file://src/main/java/com/example/nettyim/dto/UserLoginDTO.java#L1-L35)

#### 获取用户信息
- **HTTP方法**: `GET`
- **URL路径**: `/user/{userId}`
- **请求头**: `Authorization: Bearer <token>`
- **成功响应**: `200 OK`
- **错误响应**: `404 Not Found`（用户不存在）

**Section sources**
- [UserController.java](file://src/main/java/com/example/nettyim/controller/UserController.java#L46-L51)

#### 更新用户信息
- **HTTP方法**: `PUT`
- **URL路径**: `/user/{userId}`
- **请求头**: `Authorization: Bearer <token>`
- **请求体**: `UserUpdateDTO`
- **成功响应**: `200 OK`

**Section sources**
- [UserController.java](file://src/main/java/com/example/nettyim/controller/UserController.java#L53-L62)

#### 实名认证
- **HTTP方法**: `POST`
- **URL路径**: `/user/{userId}/identity-verify`
- **请求头**: `Authorization: Bearer <token>`
- **请求体**: `IdentityVerifyDTO`
- **成功响应**: `200 OK`

**Section sources**
- [UserController.java](file://src/main/java/com/example/nettyim/controller/UserController.java#L64-L73)

#### 更新在线状态
- **HTTP方法**: `PUT`
- **URL路径**: `/user/{userId}/status`
- **请求头**: `Authorization: Bearer <token>`
- **查询参数**: `status` (1-在线, 2-离线)
- **成功响应**: `200 OK`

**Section sources**
- [UserController.java](file://src/main/java/com/example/nettyim/controller/UserController.java#L75-L82)

#### 检查用户名是否存在
- **HTTP方法**: `GET`
- **URL路径**: `/user/check/username`
- **查询参数**: `username`
- **成功响应**: `200 OK`

**Section sources**
- [UserController.java](file://src/main/java/com/example/nettyim/controller/UserController.java#L84-L90)

### 好友管理

#### 添加好友
- **HTTP方法**: `POST`
- **URL路径**: `/friendship/add`
- **请求头**: `Authorization: Bearer <token>`
- **查询参数**: `userId`
- **请求体**: `AddFriendDTO`
- **成功响应**: `200 OK`

**Section sources**
- [FriendshipController.java](file://src/main/java/com/example/nettyim/controller/FriendshipController.java#L25-L33)

#### 处理好友申请
- **HTTP方法**: `POST`
- **URL路径**: `/friendship/handle`
- **请求头**: `Authorization: Bearer <token}`
- **查询参数**: `userId`, `requestId`, `action` (1-同意, 2-拒绝), `remark`（可选）
- **成功响应**: `200 OK`

**Section sources**
- [FriendshipController.java](file://src/main/java/com/example/nettyim/controller/FriendshipController.java#L35-L47)

#### 获取好友列表
- **HTTP方法**: `GET`
- **URL路径**: `/friendship/list`
- **请求头**: `Authorization: Bearer <token}`
- **查询参数**: `userId`
- **成功响应**: `200 OK`

**Section sources**
- [FriendshipController.java](file://src/main/java/com/example/nettyim/controller/FriendshipController.java#L49-L55)

#### 获取好友申请列表
- **HTTP方法**: `GET`
- **URL路径**: `/friendship/requests`
- **请求头**: `Authorization: Bearer <token}`
- **查询参数**: `userId`
- **成功响应**: `200 OK`

**Section sources**
- [FriendshipController.java](file://src/main/java/com/example/nettyim/controller/FriendshipController.java#L57-L63)

#### 删除好友
- **HTTP方法**: `DELETE`
- **URL路径**: `/friendship/delete`
- **请求头**: `Authorization: Bearer <token}`
- **查询参数**: `userId`, `friendId`
- **成功响应**: `200 OK`

**Section sources**
- [FriendshipController.java](file://src/main/java/com/example/nettyim/controller/FriendshipController.java#L65-L73)

#### 更新好友备注
- **HTTP方法**: `PUT`
- **URL路径**: `/friendship/remark`
- **请求头**: `Authorization: Bearer <token}`
- **查询参数**: `userId`, `friendId`, `remark`
- **成功响应**: `200 OK`

**Section sources**
- [FriendshipController.java](file://src/main/java/com/example/nettyim/controller/FriendshipController.java#L75-L83)

#### 检查是否为好友
- **HTTP方法**: `GET`
- **URL路径**: `/friendship/check`
- **请求头**: `Authorization: Bearer <token}`
- **查询参数**: `userId`, `friendId`
- **成功响应**: `200 OK`

**Section sources**
- [FriendshipController.java](file://src/main/java/com/example/nettyim/controller/FriendshipController.java#L85-L92)

### 群组管理

#### 创建群组
- **HTTP方法**: `POST`
- **URL路径**: `/group/create`
- **请求头**: `Authorization: Bearer <token}`
- **查询参数**: `ownerId`
- **请求体**: `CreateGroupDTO`
- **成功响应**: `200 OK`

**Section sources**
- [GroupController.java](file://src/main/java/com/example/nettyim/controller/GroupController.java#L25-L33)

#### 加入群组
- **HTTP方法**: `POST`
- **URL路径**: `/group/join`
- **请求头**: `Authorization: Bearer <token}`
- **查询参数**: `userId`, `groupId`
- **成功响应**: `200 OK`

**Section sources**
- [GroupController.java](file://src/main/java/com/example/nettyim/controller/GroupController.java#L35-L42)

#### 邀请用户加入群组
- **HTTP方法**: `POST`
- **URL路径**: `/group/invite`
- **请求头**: `Authorization: Bearer <token}`
- **查询参数**: `operatorId`, `groupId`
- **请求体**: `List<Long>`（用户ID列表）
- **成功响应**: `200 OK`

**Section sources**
- [GroupController.java](file://src/main/java/com/example/nettyim/controller/GroupController.java#L44-L53)

#### 退出群组
- **HTTP方法**: `POST`
- **URL路径**: `/group/leave`
- **请求头**: `Authorization: Bearer <token}`
- **查询参数**: `userId`, `groupId`
- **成功响应**: `200 OK`

**Section sources**
- [GroupController.java](file://src/main/java/com/example/nettyim/controller/GroupController.java#L55-L62)

#### 踢出群成员
- **HTTP方法**: `POST`
- **URL路径**: `/group/kick`
- **请求头**: `Authorization: Bearer <token}`
- **查询参数**: `operatorId`, `groupId`, `userId`
- **成功响应**: `200 OK`

**Section sources**
- [GroupController.java](file://src/main/java/com/example/nettyim/controller/GroupController.java#L64-L73)

#### 设置/取消管理员
- **HTTP方法**: `POST`
- **URL路径**: `/group/admin`
- **请求头**: `Authorization: Bearer <token}`
- **查询参数**: `operatorId`, `groupId`, `userId`, `isAdmin` (true/false)
- **成功响应**: `200 OK`

**Section sources**
- [GroupController.java](file://src/main/java/com/example/nettyim/controller/GroupController.java#L75-L86)

#### 禁言群成员
- **HTTP方法**: `POST`
- **URL路径**: `/group/mute`
- **请求头**: `Authorization: Bearer <token}`
- **查询参数**: `operatorId`, `groupId`, `userId`, `muteDuration`（禁言时长，秒）
- **成功响应**: `200 OK`

**Section sources**
- [GroupController.java](file://src/main/java/com/example/nettyim/controller/GroupController.java#L88-L97)

#### 解除禁言
- **HTTP方法**: `POST`
- **URL路径**: `/group/unmute`
- **请求头**: `Authorization: Bearer <token}`
- **查询参数**: `operatorId`, `groupId`, `userId`
- **成功响应**: `200 OK`

**Section sources**
- [GroupController.java](file://src/main/java/com/example/nettyim/controller/GroupController.java#L99-L108)

#### 更新群组信息
- **HTTP方法**: `PUT`
- **URL路径**: `/group/{groupId}`
- **请求头**: `Authorization: Bearer <token}`
- **路径参数**: `groupId`
- **查询参数**: `operatorId`, `name`（可选）, `description`（可选）, `avatar`（可选）
- **成功响应**: `200 OK`

**Section sources**
- [GroupController.java](file://src/main/java/com/example/nettyim/controller/GroupController.java#L110-L123)

#### 解散群组
- **HTTP方法**: `DELETE`
- **URL路径**: `/group/{groupId}`
- **请求头**: `Authorization: Bearer <token}`
- **路径参数**: `groupId`
- **查询参数**: `ownerId`
- **成功响应**: `200 OK`

**Section sources**
- [GroupController.java](file://src/main/java/com/example/nettyim/controller/GroupController.java#L125-L133)

#### 获取群组信息
- **HTTP方法**: `GET`
- **URL路径**: `/group/{groupId}`
- **请求头**: `Authorization: Bearer <token}`
- **路径参数**: `groupId`
- **成功响应**: `200 OK`

**Section sources**
- [GroupController.java](file://src/main/java/com/example/nettyim/controller/GroupController.java#L135-L141)

#### 获取群组成员列表
- **HTTP方法**: `GET`
- **URL路径**: `/group/{groupId}/members`
- **请求头**: `Authorization: Bearer <token}`
- **路径参数**: `groupId`
- **成功响应**: `200 OK`

**Section sources**
- [GroupController.java](file://src/main/java/com/example/nettyim/controller/GroupController.java#L143-L150)

#### 获取用户加入的群组列表
- **HTTP方法**: `GET`
- **URL路径**: `/group/user/{userId}`
- **请求头**: `Authorization: Bearer <token}`
- **路径参数**: `userId`
- **成功响应**: `200 OK`

**Section sources**
- [GroupController.java](file://src/main/java/com/example/nettyim/controller/GroupController.java#L152-L159)

#### 检查是否为群成员
- **HTTP方法**: `GET`
- **URL路径**: `/group/{groupId}/member/{userId}`
- **请求头**: `Authorization: Bearer <token}`
- **路径参数**: `groupId`, `userId`
- **成功响应**: `200 OK`

**Section sources**
- [GroupController.java](file://src/main/java/com/example/nettyim/controller/GroupController.java#L161-L168)

### 消息管理

#### 发送消息
- **HTTP方法**: `POST`
- **URL路径**: `/message/send`
- **请求头**: `Authorization: Bearer <token}`
- **查询参数**: `fromUserId`
- **请求体**: `SendMessageDTO`
- **成功响应**: `200 OK`

**请求体示例**
```json
{
  "toUserId": 2,
  "messageType": 1,
  "content": "你好，这是测试消息"
}
```

**响应示例**
```json
{
  "code": 200,
  "message": "消息发送成功",
  "data": {
    "messageId": "msg_123456",
    "fromUserId": 1,
    "toUserId": 2,
    "content": "你好，这是测试消息",
    "timestamp": 1700000000000
  },
  "timestamp": 1700000000000
}
```

**Section sources**
- [MessageController.java](file://src/main/java/com/example/nettyim/controller/MessageController.java#L25-L33)
- [SendMessageDTO.java](file://src/main/java/com/example/nettyim/dto/SendMessageDTO.java#L1-L122)

#### 获取私聊消息历史
- **HTTP方法**: `POST`
- **URL路径**: `/message/private/history`
- **请求头**: `Authorization: Bearer <token}`
- **请求体**: `MessageQueryDTO`
- **成功响应**: `200 OK`

**Section sources**
- [MessageController.java](file://src/main/java/com/example/nettyim/controller/MessageController.java#L35-L41)

#### 获取群聊消息历史
- **HTTP方法**: `POST`
- **URL路径**: `/message/group/history`
- **请求头**: `Authorization: Bearer <token}`
- **请求体**: `MessageQueryDTO`
- **成功响应**: `200 OK`

**Section sources**
- [MessageController.java](file://src/main/java/com/example/nettyim/controller/MessageController.java#L43-L49)

#### 标记消息为已读
- **HTTP方法**: `POST`
- **URL路径**: `/message/read`
- **请求头**: `Authorization: Bearer <token}`
- **查询参数**: `userId`, `messageId`
- **成功响应**: `200 OK`

**Section sources**
- [MessageController.java](file://src/main/java/com/example/nettyim/controller/MessageController.java#L51-L58)

#### 批量标记消息为已读
- **HTTP方法**: `POST`
- **URL路径**: `/message/read/batch`
- **请求头**: `Authorization: Bearer <token}`
- **查询参数**: `userId`
- **请求体**: `List<Long>`（消息ID列表）
- **成功响应**: `200 OK`

**Section sources**
- [MessageController.java](file://src/main/java/com/example/nettyim/controller/MessageController.java#L60-L68)

#### 删除消息
- **HTTP方法**: `DELETE`
- **URL路径**: `/message/{messageId}`
- **请求头**: `Authorization: Bearer <token}`
- **路径参数**: `messageId`
- **查询参数**: `userId`
- **成功响应**: `200 OK`

**Section sources**
- [MessageController.java](file://src/main/java/com/example/nettyim/controller/MessageController.java#L70-L78)

#### 获取未读消息数量
- **HTTP方法**: `GET`
- **URL路径**: `/message/unread/count`
- **请求头**: `Authorization: Bearer <token}`
- **查询参数**: `userId`, `targetId`, `conversationType` (1-私聊, 2-群聊)
- **成功响应**: `200 OK`

**Section sources**
- [MessageController.java](file://src/main/java/com/example/nettyim/controller/MessageController.java#L80-L88)

#### 获取消息详情
- **HTTP方法**: `GET`
- **URL路径**: `/message/{messageId}`
- **请求头**: `Authorization: Bearer <token}`
- **路径参数**: `messageId`
- **成功响应**: `200 OK`

**Section sources**
- [MessageController.java](file://src/main/java/com/example/nettyim/controller/MessageController.java#L90-L97)

#### 撤回消息
- **HTTP方法**: `POST`
- **URL路径**: `/message/{messageId}/recall`
- **请求头**: `Authorization: Bearer <token}`
- **路径参数**: `messageId`
- **查询参数**: `userId`
- **成功响应**: `200 OK`

**Section sources**
- [MessageController.java](file://src/main/java/com/example/nettyim/controller/MessageController.java#L99-L107)

## WebSocket API

### 事件列表

#### send_message
- **事件名称**: `send_message`
- **接收数据格式**: `SendMessageData`
- **触发条件**: 客户端发送消息
- **服务器响应**: `MessageResponse`
- **错误处理**: 返回错误信息

**Section sources**
- [SocketIOEventHandler.java](file://src/main/java/com/example/nettyim/websocket/SocketIOEventHandler.java#L100-L140)

#### join_room
- **事件名称**: `join_room`
- **接收数据格式**: `JoinRoomData`
- **触发条件**: 客户端加入房间
- **服务器响应**: `BaseResponse`
- **错误处理**: 返回错误信息

**Section sources**
- [SocketIOEventHandler.java](file://src/main/java/com/example/nettyim/websocket/SocketIOEventHandler.java#L142-L158)

#### leave_room
- **事件名称**: `leave_room`
- **接收数据格式**: `LeaveRoomData`
- **触发条件**: 客户端离开房间
- **服务器响应**: `BaseResponse`
- **错误处理**: 返回错误信息

**Section sources**
- [SocketIOEventHandler.java](file://src/main/java/com/example/nettyim/websocket/SocketIOEventHandler.java#L160-L176)

#### typing
- **事件名称**: `typing`
- **接收数据格式**: `TypingData`
- **触发条件**: 用户开始输入
- **服务器响应**: 向目标用户发送`user_typing`事件
- **错误处理**: 记录错误日志

**Section sources**
- [SocketIOEventHandler.java](file://src/main/java/com/example/nettyim/websocket/SocketIOEventHandler.java#L178-L198)

#### stop_typing
- **事件名称**: `stop_typing`
- **接收数据格式**: `TypingData`
- **触发条件**: 用户停止输入
- **服务器响应**: 向目标用户发送`user_stop_typing`事件
- **错误处理**: 记录错误日志

**Section sources**
- [SocketIOEventHandler.java](file://src/main/java/com/example/nettyim/websocket/SocketIOEventHandler.java#L200-L220)

#### mark_read
- **事件名称**: `mark_read`
- **接收数据格式**: `MarkReadData`
- **触发条件**: 客户端标记消息为已读
- **服务器响应**: `BaseResponse`
- **错误处理**: 返回错误信息

**Section sources**
- [SocketIOEventHandler.java](file://src/main/java/com/example/nettyim/websocket/SocketIOEventHandler.java#L222-L242)

#### get_online_users
- **事件名称**: `get_online_users`
- **接收数据格式**: 任意对象
- **触发条件**: 客户端请求获取在线用户列表
- **服务器响应**: `OnlineUsersResponse`
- **错误处理**: 返回错误信息

**Section sources**
- [SocketIOEventHandler.java](file://src/main/java/com/example/nettyim/websocket/SocketIOEventHandler.java#L244-L260)

### 数据结构

#### SendMessageData
```json
{
  "toUserId": 123,
  "groupId": 456,
  "messageType": 1,
  "content": "消息内容",
  "fileUrl": "https://example.com/file.pdf",
  "fileSize": 1024,
  "fileName": "file.pdf",
  "replyToMessageId": "msg_789"
}
```

#### JoinRoomData
```json
{
  "targetId": 123,
  "roomType": 1
}
```

#### LeaveRoomData
```json
{
  "targetId": 123,
  "roomType": 1
}
```

#### TypingData
```json
{
  "targetId": 123,
  "targetType": 1
}
```

#### MarkReadData
```json
{
  "messageIds": [1, 2, 3]
}
```

**Section sources**
- [SocketIOEventHandler.java](file://src/main/java/com/example/nettyim/websocket/SocketIOEventHandler.java#L262-L356)

### 响应格式

#### BaseResponse
```json
{
  "success": true,
  "message": "操作成功"
}
```

#### MessageResponse
```json
{
  "success": true,
  "message": "消息发送成功",
  "data": {
    "messageId": "msg_123",
    "fromUserId": 1,
    "toUserId": 2,
    "content": "测试消息",
    "timestamp": 1700000000000
  }
}
```

#### OnlineUsersResponse
```json
{
  "success": true,
  "message": "获取成功",
  "data": [1, 2, 3, 4, 5]
}
```

#### TypingNotification
```json
{
  "userId": 1,
  "targetId": 2,
  "isTyping": true
}
```

**Section sources**
- [SocketIOEventHandler.java](file://src/main/java/com/example/nettyim/websocket/SocketIOEventHandler.java#L262-L356)

## 认证与安全
系统采用JWT（JSON Web Token）进行认证，所有需要认证的REST API端点和WebSocket连接都需要提供有效的JWT Token。

### 认证方式
- **类型**: Bearer Token
- **请求头**: `Authorization: Bearer <token>`
- **Token获取**: 通过`/user/login`端点登录后获取

### Token有效期
- 默认有效期：24小时（86400000毫秒）
- 可通过配置`jwt.expiration`修改

### 参数验证
所有API端点均使用Jakarta Validation进行参数验证，常见验证规则：
- `@NotBlank`: 字符串不能为空或空白
- `@NotNull`: 对象不能为空
- `@Size`: 字符串长度限制

**Section sources**
- [JwtUtils.java](file://src/main/java/com/example/nettyim/utils/JwtUtils.java#L1-L123)
- [UserLoginDTO.java](file://src/main/java/com/example/nettyim/dto/UserLoginDTO.java#L1-L35)
- [SendMessageDTO.java](file://src/main/java/com/example/nettyim/dto/SendMessageDTO.java#L1-L122)

## 通用响应格式
所有REST API响应均采用统一的`Result`格式：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {},
  "timestamp": 1700000000000
}
```

### 字段说明
- **code**: HTTP状态码或业务状态码
- **message**: 响应消息
- **data**: 响应数据（可为空）
- **timestamp**: 响应时间戳

### 常见状态码
- `200`: 操作成功
- `400`: 请求参数错误
- `401`: 未授权
- `403`: 禁止访问
- `404`: 资源未找到
- `500`: 服务器内部错误

**Section sources**
- [Result.java](file://src/main/java/com/example/nettyim/dto/Result.java#L1-L57)