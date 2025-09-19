# RESTful API

<cite>
**本文档引用的文件**
- [UserController.java](file://src/main/java/com/example/nettyim/controller/UserController.java)
- [FriendshipController.java](file://src/main/java/com/example/nettyim/controller/FriendshipController.java)
- [GroupController.java](file://src/main/java/com/example/nettyim/controller/GroupController.java)
- [MessageController.java](file://src/main/java/com/example/nettyim/controller/MessageController.java)
- [UserLoginDTO.java](file://src/main/java/com/example/nettyim/dto/UserLoginDTO.java)
- [SendMessageDTO.java](file://src/main/java/com/example/nettyim/dto/SendMessageDTO.java)
- [MessageQueryDTO.java](file://src/main/java/com/example/nettyim/dto/MessageQueryDTO.java)
- [UserRegisterDTO.java](file://src/main/java/com/example/nettyim/dto/UserRegisterDTO.java)
- [Result.java](file://src/main/java/com/example/nettyim/dto/Result.java)
- [GlobalExceptionHandler.java](file://src/main/java/com/example/nettyim/exception/GlobalExceptionHandler.java)
</cite>

## 目录
1. [简介](#简介)
2. [通用规范](#通用规范)
3. [用户管理接口](#用户管理接口)
4. [好友管理接口](#好友管理接口)
5. [群组管理接口](#群组管理接口)
6. [消息管理接口](#消息管理接口)
7. [全局异常处理](#全局异常处理)

## 简介
本API文档详细描述了基于Netty的即时通讯系统提供的RESTful接口，涵盖用户身份认证、数据初始化和历史数据获取三大核心功能。系统通过JWT进行身份验证，所有接口均返回统一的Result泛型结构，确保客户端能够一致地处理响应。

## 通用规范

### 响应结构
所有接口响应均封装在`Result<T>`泛型类中，包含以下字段：
- `code`: 状态码（200、400、401、403、404、500等）
- `message`: 响应消息
- `data`: 返回数据（泛型T）
- `timestamp`: 时间戳

### 认证机制
除注册、登录及部分检查接口外，其余接口均需在请求头中携带JWT Bearer Token：
```
Authorization: Bearer <token>
```

### 参数验证
使用`@Valid`注解对请求体进行参数校验，若验证失败，返回400状态码及错误信息。

**Section sources**
- [Result.java](file://src/main/java/com/example/nettyim/dto/Result.java#L7-L56)
- [GlobalExceptionHandler.java](file://src/main/java/com/example/nettyim/exception/GlobalExceptionHandler.java#L20-L81)

## 用户管理接口

### 用户注册
- **HTTP方法**: POST
- **URL路径**: `/user/register`
- **请求头**: 无特殊要求
- **请求体**: `UserRegisterDTO`
  - `username`: 用户名（3-50字符，必填）
  - `email`: 邮箱（格式校验）
  - `phone`: 手机号（11位，必填）
  - `password`: 密码（6-20字符，必填）
  - `nickname`: 昵称（1-50字符，必填）
- **响应体**: `Result<User>`
- **状态码**:
  - 200: 注册成功
  - 400: 参数校验失败
  - 500: 系统异常
- **请求示例**:
```json
{
  "username": "zhangsan",
  "email": "zhangsan@example.com",
  "phone": "13812345678",
  "password": "123456",
  "nickname": "张三"
}
```
- **响应示例**:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": {
    "id": 1,
    "username": "zhangsan",
    "nickname": "张三",
    "email": "zhangsan@example.com",
    "phone": "13812345678",
    "status": 1
  },
  "timestamp": 1730000000000
}
```

**Section sources**
- [UserController.java](file://src/main/java/com/example/nettyim/controller/UserController.java#L19-L118)
- [UserRegisterDTO.java](file://src/main/java/com/example/nettyim/dto/UserRegisterDTO.java#L0-L74)

### 用户登录
- **HTTP方法**: POST
- **URL路径**: `/user/login`
- **请求头**: 无特殊要求
- **请求体**: `UserLoginDTO`
  - `account`: 登录账号（用户名或手机号，必填）
  - `password`: 密码（必填）
- **响应体**: `Result<Map<String, Object>>`
  - `token`: JWT令牌
  - `user`: 用户信息
- **状态码**:
  - 200: 登录成功
  - 401: 用户名或密码错误
  - 400: 参数校验失败
- **请求示例**:
```json
{
  "account": "zhangsan",
  "password": "123456"
}
```
- **响应示例**:
```json
{
  "code": 200,
  "message": "登录成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.xxxxx",
    "user": {
      "id": 1,
      "username": "zhangsan",
      "nickname": "张三",
      "status": 1
    }
  },
  "timestamp": 1730000000000
}
```

**Section sources**
- [UserController.java](file://src/main/java/com/example/nettyim/controller/UserController.java#L19-L118)
- [UserLoginDTO.java](file://src/main/java/com/example/nettyim/dto/UserLoginDTO.java#L0-L34)

### 获取用户信息
- **HTTP方法**: GET
- **URL路径**: `/user/{userId}`
- **请求头**: 需要JWT Bearer Token
- **路径参数**: `userId`（用户ID）
- **响应体**: `Result<User>`
- **状态码**:
  - 200: 获取成功
  - 401: 未授权
  - 404: 用户不存在
- **响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "zhangsan",
    "nickname": "张三",
    "email": "zhangsan@example.com",
    "status": 1
  },
  "timestamp": 1730000000000
}
```

**Section sources**
- [UserController.java](file://src/main/java/com/example/nettyim/controller/UserController.java#L19-L118)

## 好友管理接口

### 添加好友
- **HTTP方法**: POST
- **URL路径**: `/friendship/add`
- **请求头**: 需要JWT Bearer Token
- **请求参数**: `userId`（当前用户ID）
- **请求体**: `AddFriendDTO`
  - `targetId`: 目标用户ID（必填）
  - `remark`: 备注（可选）
  - `verifyMessage`: 验证消息（可选）
- **响应体**: `Result<String>`
- **状态码**:
  - 200: 好友申请已发送
  - 400: 参数校验失败
  - 401: 未授权
- **响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": "好友申请已发送",
  "timestamp": 1730000000000
}
```

**Section sources**
- [FriendshipController.java](file://src/main/java/com/example/nettyim/controller/FriendshipController.java#L16-L97)

### 获取好友列表
- **HTTP方法**: GET
- **URL路径**: `/friendship/list`
- **请求头**: 需要JWT Bearer Token
- **请求参数**: `userId`（用户ID）
- **响应体**: `Result<List<User>>`
- **状态码**:
  - 200: 获取成功
  - 401: 未授权
- **响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 2,
      "username": "lisi",
      "nickname": "李四",
      "status": 1
    }
  ],
  "timestamp": 1730000000000
}
```

**Section sources**
- [FriendshipController.java](file://src/main/java/com/example/nettyim/controller/FriendshipController.java#L16-L97)

## 群组管理接口

### 创建群组
- **HTTP方法**: POST
- **URL路径**: `/group/create`
- **请求头**: 需要JWT Bearer Token
- **请求参数**: `ownerId`（群主ID）
- **请求体**: `CreateGroupDTO`
  - `name`: 群名称（必填）
  - `description`: 群描述（可选）
  - `avatar`: 群头像（可选）
- **响应体**: `Result<Group>`
- **状态码**:
  - 200: 群组创建成功
  - 400: 参数校验失败
  - 401: 未授权
- **响应示例**:
```json
{
  "code": 200,
  "message": "群组创建成功",
  "data": {
    "id": 1,
    "name": "技术交流群",
    "ownerId": 1,
    "memberCount": 1
  },
  "timestamp": 1730000000000
}
```

**Section sources**
- [GroupController.java](file://src/main/java/com/example/nettyim/controller/GroupController.java#L16-L173)

### 获取群组成员列表
- **HTTP方法**: GET
- **URL路径**: `/group/{groupId}/members`
- **请求头**: 需要JWT Bearer Token
- **路径参数**: `groupId`（群组ID）
- **响应体**: `Result<List<User>>`
- **状态码**:
  - 200: 获取成功
  - 401: 未授权
  - 404: 群组不存在
- **响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "username": "zhangsan",
      "nickname": "张三"
    },
    {
      "id": 2,
      "username": "lisi",
      "nickname": "李四"
    }
  ],
  "timestamp": 1730000000000
}
```

**Section sources**
- [GroupController.java](file://src/main/java/com/example/nettyim/controller/GroupController.java#L16-L173)

## 消息管理接口

### 发送消息
- **HTTP方法**: POST
- **URL路径**: `/message/send`
- **请求头**: 需要JWT Bearer Token
- **请求参数**: `fromUserId`（发送者ID）
- **请求体**: `SendMessageDTO`
  - `toUserId`: 接收者ID（私聊）
  - `groupId`: 群组ID（群聊）
  - `messageType`: 消息类型（1-文本，2-图片等，必填）
  - `content`: 消息内容（必填，≤5000字符）
  - `fileUrl`: 文件URL（非文本消息）
  - `fileSize`: 文件大小
  - `fileName`: 文件名
  - `replyToMessageId`: 回复的消息ID
- **响应体**: `Result<Message>`
- **状态码**:
  - 200: 消息发送成功
  - 400: 参数校验失败
  - 401: 未授权
- **请求示例**:
```json
{
  "toUserId": 2,
  "messageType": 1,
  "content": "你好，这是测试消息"
}
```
- **响应示例**:
```json
{
  "code": 200,
  "message": "消息发送成功",
  "data": {
    "id": 1,
    "fromUserId": 1,
    "toUserId": 2,
    "content": "你好，这是测试消息",
    "messageType": 1,
    "status": 1,
    "createTime": "2023-10-01T10:00:00"
  },
  "timestamp": 1730000000000
}
```

**Section sources**
- [MessageController.java](file://src/main/java/com/example/nettyim/controller/MessageController.java#L17-L114)
- [SendMessageDTO.java](file://src/main/java/com/example/nettyim/dto/SendMessageDTO.java#L0-L121)

### 获取消息历史
- **HTTP方法**: POST
- **URL路径**: `/message/private/history`（私聊）或 `/message/group/history`（群聊）
- **请求头**: 需要JWT Bearer Token
- **请求体**: `MessageQueryDTO`
  - `targetId`: 会话对象ID（用户ID或群组ID，必填）
  - `conversationType`: 会话类型（1-私聊，2-群聊，必填）
  - `page`: 页码（默认1）
  - `size`: 每页大小（默认20）
  - `lastMessageId`: 最后一条消息ID（用于分页）
- **响应体**: `Result<Page<Message>>`
- **状态码**:
  - 200: 获取成功
  - 400: 参数校验失败
  - 401: 未授权
- **请求示例**:
```json
{
  "targetId": 2,
  "conversationType": 1,
  "page": 1,
  "size": 10
}
```

**Section sources**
- [MessageController.java](file://src/main/java/com/example/nettyim/controller/MessageController.java#L17-L114)
- [MessageQueryDTO.java](file://src/main/java/com/example/nettyim/dto/MessageQueryDTO.java#L0-L79)

## 全局异常处理

系统通过`GlobalExceptionHandler`统一处理各类异常，返回标准化的错误响应：

| 异常类型 | HTTP状态码 | 响应码 | 说明 |
|---------|----------|-------|------|
| BusinessException | 400 | 自定义 | 业务逻辑异常 |
| MethodArgumentNotValidException | 400 | 400 | 参数校验失败 |
| BadCredentialsException | 401 | 401 | 认证失败（用户名/密码错误） |
| AccessDeniedException | 403 | 403 | 权限不足 |
| Exception | 500 | 500 | 系统内部异常 |

**Section sources**
- [GlobalExceptionHandler.java](file://src/main/java/com/example/nettyim/exception/GlobalExceptionHandler.java#L20-L81)