# Netty-IM 即时通讯系统

基于 Spring Boot 3 + Netty-SocketIO + MyBatis Plus 的即时通讯系统，支持集群部署和多种社交功能。

## 技术栈

- **Java**: JDK 21
- **框架**: Spring Boot 3.x
- **数据库**: MySQL 8.0+
- **ORM**: MyBatis Plus
- **即时通讯**: Netty-SocketIO
- **缓存**: Redis
- **集群**: Redisson
- **构建工具**: Maven

## 核心功能

### 1. 用户系统
- ✅ **用户注册/登录**
  - 支持用户名注册登录
  - 支持手机号注册登录
  - JWT Token认证
  - 密码加密存储
- ✅ **身份证实名认证**
  - 身份证号码验证
  - 身份证照片上传
  - 实名认证状态管理
- ✅ **用户资料管理**
  - 个人信息修改
  - 头像上传
  - 在线状态管理

### 2. 即时通讯
- ✅ **实时消息**
  - 私聊消息
  - 群聊消息
  - 消息状态（已读/未读）
  - 消息持久化
- ✅ **好友系统**
  - 好友申请/同意/拒绝
  - 好友列表管理
  - 好友状态查看
- ✅ **群组功能**
  - 创建群组
  - 群成员管理
  - 群消息广播

### 3. 朋友圈功能
- ✅ **动态发布**
  - 文字动态
  - 图片动态（支持多图）
  - 位置信息
  - 可见性设置（公开/好友/私密）
- ✅ **互动功能**
  - 点赞/取消点赞
  - 评论/回复评论
  - 删除评论
- ✅ **动态管理**
  - 分页查询动态
  - 删除自己的动态
  - 查看动态详情

### 4. 贴吧功能
- ✅ **贴吧管理**
  - 创建贴吧
  - 加入/退出贴吧
  - 贴吧成员管理
  - 角色权限管理（吧主/管理员/普通成员）
- ✅ **帖子功能**
  - 发布帖子（支持文字、图片）
  - 删除帖子
  - 置顶/取消置顶帖子
  - 精华帖设置
  - 帖子分类管理
- ✅ **回复系统**
  - 回复帖子
  - 楼层显示
  - 回复点赞
  - 删除回复
- ✅ **搜索功能**
  - 贴吧搜索
  - 帖子搜索
  - 分类筛选
  - 多种排序方式
- ✅ **互动功能**
  - 帖子点赞
  - 回复点赞
  - 浏览数统计
  - 互动数据展示

### 5. 集群部署
- ✅ **分布式支持**
  - Redisson分布式会话存储
  - Redis消息发布/订阅
  - 跨节点消息路由
  - 故障转移支持
- ✅ **集群管理**
  - 一键启动集群脚本
  - 集群状态监控
  - 负载均衡

## API 接口文档

### 用户相关接口

#### 用户注册
```http
POST /user/register
Content-Type: application/json

{
  "username": "testuser",
  "phone": "13800138000",
  "email": "test@example.com",
  "password": "password123",
  "nickname": "测试用户"
}
```

#### 用户登录
```http
POST /user/login
Content-Type: application/json

{
  "account": "testuser",  // 用户名或手机号
  "password": "password123"
}
```

#### 身份证实名认证
```http
POST /user/{userId}/identity-verify
Content-Type: application/json

{
  "realName": "张三",
  "idCardNumber": "110101199001011234",
  "idCardFrontUrl": "https://example.com/front.jpg",
  "idCardBackUrl": "https://example.com/back.jpg"
}
```

### 朋友圈相关接口

#### 发布动态
```http
POST /moment/publish?userId={userId}
Content-Type: application/json

{
  "content": "今天天气真不错！",
  "images": ["https://example.com/image1.jpg", "https://example.com/image2.jpg"],
  "location": "北京市朝阳区",
  "visibility": 0  // 0-公开，1-仅好友，2-仅自己
}
```

#### 查询动态列表
```http
GET /moment/list?page=1&size=10&userId={userId}&visibility=0
```

#### 点赞动态
```http
POST /moment/{momentId}/like?userId={userId}
```

#### 评论动态
```http
POST /moment/comment?userId={userId}
Content-Type: application/json

{
  "momentId": 1,
  "content": "很棒的分享！",
  "replyToUserId": null,
  "replyToCommentId": null
}
```

### 贴吧相关接口

#### 创建贴吧
```http
POST /forum/create?userId={userId}
Content-Type: application/json

{
  "name": "Java技术交流吧",
  "description": "Java技术学习与交流平台",
  "category": "技本",
  "isPublic": 1
}
```

#### 加入贴吧
```http
POST /forum/{forumId}/join?userId={userId}
```

#### 查询贴吧列表
```http
GET /forum/list?page=1&size=10&keyword=Java&category=技术
```

#### 发布帖子
```http
POST /forum/post/create?userId={userId}
Content-Type: application/json

{
  "forumId": 1,
  "title": "Java 21新特性介绍",
  "content": "Java 21是LTS版本，带来了许多新特性...",
  "category": "技术分享",
  "images": ["https://example.com/java21.jpg"]
}
```

#### 查询帖子列表
```http
GET /forum/post/list?forumId=1&page=1&size=10&orderBy=last_reply_time&orderDirection=desc
```

#### 回复帖子
```http
POST /forum/reply/create?userId={userId}
Content-Type: application/json

{
  "postId": 1,
  "content": "谢谢分享，很有用！",
  "replyToUserId": null,
  "replyToReplyId": null
}
```

#### 点赞帖子
```http
POST /forum/post/{postId}/like?userId={userId}
```

#### 置顶帖子
```http
POST /forum/post/{postId}/pin?operatorId={operatorId}&pin=true
```

## 快速开始

### 环境要求
- JDK 21+
- MySQL 8.0+
- Redis 6.0+
- Maven 3.6+

### 安装步骤

1. **克隆项目**
```bash
git clone <repository-url>
cd netty-im-sample
```

2. **配置数据库**
```bash
# 创建数据库
mysql -u root -p
CREATE DATABASE netty_im DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 导入数据库结构
mysql -u root -p netty_im < src/main/resources/schema.sql
```

3. **配置Redis**
```bash
# 启动Redis服务
redis-server
```

4. **修改配置文件**
```yaml
# src/main/resources/application.yml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/netty_im
    username: your_username
    password: your_password
  data:
    redis:
      host: localhost
      port: 6379
```

5. **编译运行**
```bash
# 设置Java环境
export JAVA_HOME=$(/usr/libexec/java_home -v 21)

# 编译项目
mvn clean compile

# 运行项目
mvn spring-boot:run
```

### 集群部署

1. **启动集群**
```bash
# 启动三节点集群
./start-cluster.sh
```

2. **停止集群**
```bash
# 停止集群
./stop-cluster.sh
```

## 项目结构

```
src/main/java/com/example/nettyim/
├── config/                 # 配置类
│   ├── MybatisPlusConfig.java
│   ├── PasswordConfig.java
│   ├── RedisConfig.java
│   └── SocketIOConfig.java
├── controller/             # 控制器层
│   ├── FriendshipController.java
│   ├── GroupController.java
│   ├── MessageController.java
│   ├── MomentController.java
│   ├── ForumController.java
│   └── UserController.java
├── dto/                    # 数据传输对象
│   ├── IdentityVerifyDTO.java
│   ├── MomentCommentDTO.java
│   ├── MomentQueryDTO.java
│   ├── PublishMomentDTO.java
│   ├── CreateForumDTO.java
│   ├── CreateForumPostDTO.java
│   ├── ForumQueryDTO.java
│   ├── ForumPostQueryDTO.java
│   ├── ForumReplyDTO.java
│   ├── UserLoginDTO.java
│   └── UserRegisterDTO.java
├── entity/                 # 实体类
│   ├── User.java
│   ├── Moment.java
│   ├── MomentComment.java
│   ├── MomentLike.java
│   ├── Forum.java
│   ├── ForumMember.java
│   ├── ForumPost.java
│   ├── ForumReply.java
│   ├── ForumPostLike.java
│   ├── ForumReplyLike.java
│   └── ...
├── service/               # 服务层
│   ├── UserService.java
│   ├── MomentService.java
│   ├── ForumService.java
│   └── impl/
├── mapper/                # 数据访问层
├── websocket/             # WebSocket处理
├── cluster/               # 集群相关
└── utils/                 # 工具类
```

## 开发计划

### 即将实现的功能
- 🔄 **文件上传**
  - 图片上传服务
  - 文件存储管理
- 🔄 **消息推送**
  - 站内消息通知
  - 邮件通知
  - 短信通知

### 已实现功能
- ✅ **运营管理后台**
  - 管理员登录认证
  - 用户管理
  - 内容审核管理
  - 敏感词管理
  - 会员管理
  - 支付订单管理
  - 数据统计分析
  - 系统日志管理
  - 详细API文档请查看 [ADMIN_API_DOCUMENT.md](ADMIN_API_DOCUMENT.md)

### 未来计划
- 📋 **移动端适配**

## 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 许可证

本项目采用 MIT 许可证。详情请见 [LICENSE](LICENSE) 文件。

## 联系方式

如有问题或建议，请通过以下方式联系：

- 提交 Issue
- 发送邮件
- 微信群讨论