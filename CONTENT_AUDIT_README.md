# 内容审核系统和敏感词过滤系统

本项目已集成完整的内容审核系统和敏感词过滤系统，用于自动检测和过滤用户发布的不当内容。

## 系统特性

### 1. 敏感词过滤系统
- **高效算法**: 使用AC自动机算法，支持大规模敏感词库的快速匹配
- **灵活分类**: 支持政治敏感、色情低俗、暴力血腥、赌博诈骗、毒品违法等多种分类
- **分级管理**: 敏感词分为低、中、高三个等级，支持不同的处理策略
- **多种处理方式**: 支持替换、拒绝、人工审核三种处理方式
- **实时缓存更新**: 敏感词库更新后自动刷新缓存

### 2. 内容审核系统
- **自动审核**: 基于敏感词检测和评分算法的自动审核
- **人工审核**: 支持管理员进行人工审核和批量审核
- **审核记录**: 完整记录所有审核过程和结果
- **统计分析**: 提供详细的审核数据统计和分析

### 3. 集成模块
- **消息系统**: 私聊和群聊消息的内容审核
- **朋友圈系统**: 动态发布和评论的内容审核
- **贴吧系统**: 帖子和回复的内容审核

## 数据库表结构

### 敏感词表 (sensitive_words)
```sql
CREATE TABLE `sensitive_words` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `word` VARCHAR(100) NOT NULL COMMENT '敏感词内容',
    `category` TINYINT NOT NULL COMMENT '分类：1-政治敏感，2-色情低俗，3-暴力血腥，4-赌博诈骗，5-毒品违法，6-其他',
    `level` TINYINT NOT NULL COMMENT '等级：1-低，2-中，3-高',
    `action` TINYINT NOT NULL COMMENT '处理方式：1-替换，2-拒绝，3-人工审核',
    `replacement` VARCHAR(100) DEFAULT NULL COMMENT '替换词',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    ...
);
```

### 内容审核记录表 (content_audits)
```sql
CREATE TABLE `content_audits` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `content_type` TINYINT NOT NULL COMMENT '内容类型：1-消息，2-朋友圈动态，3-朋友圈评论，4-贴吧帖子，5-贴吧回复，6-用户资料',
    `content_id` BIGINT NOT NULL COMMENT '内容ID',
    `user_id` BIGINT NOT NULL COMMENT '发布者ID',
    `content` TEXT NOT NULL COMMENT '审核内容',
    `audit_status` TINYINT DEFAULT 0 COMMENT '审核状态：0-待审核，1-审核通过，2-审核拒绝，3-自动通过，4-自动拒绝',
    `audit_result` TEXT DEFAULT NULL COMMENT '审核结果',
    `auditor_id` BIGINT DEFAULT NULL COMMENT '审核员ID',
    `audit_type` TINYINT DEFAULT 0 COMMENT '审核方式：0-自动审核，1-人工审核',
    `sensitive_words` TEXT DEFAULT NULL COMMENT '敏感词匹配结果',
    `audit_score` TINYINT DEFAULT NULL COMMENT '审核分数(0-100)',
    ...
);
```

## API接口

### 敏感词管理接口

#### 1. 分页查询敏感词
```http
GET /admin/sensitive-words?page=1&size=20&category=1&level=2&status=1
```

#### 2. 添加敏感词
```http
POST /admin/sensitive-words
Content-Type: application/json

{
    "word": "测试敏感词",
    "category": 1,
    "level": 2,
    "action": 1,
    "replacement": "***",
    "status": 1
}
```

#### 3. 批量添加敏感词
```http
POST /admin/sensitive-words/batch
Content-Type: application/json

[
    {
        "word": "敏感词1",
        "category": 1,
        "level": 2,
        "action": 1,
        "replacement": "***"
    },
    {
        "word": "敏感词2",
        "category": 2,
        "level": 3,
        "action": 2
    }
]
```

#### 4. 检测敏感词
```http
POST /admin/sensitive-words/check?text=这是一段包含敏感词的文本
```

#### 5. 过滤敏感词
```http
POST /admin/sensitive-words/filter?text=这是一段包含敏感词的文本
```

### 内容审核管理接口

#### 1. 分页查询审核记录
```http
GET /admin/audit/records?page=1&size=20&contentType=1&auditStatus=0
```

#### 2. 人工审核
```http
POST /admin/audit/manual/{auditId}
Content-Type: application/json

{
    "auditStatus": 1,
    "auditResult": "内容审核通过"
}
```

#### 3. 批量审核
```http
POST /admin/audit/batch?auditIds=1,2,3
Content-Type: application/json

{
    "auditStatus": 1,
    "auditResult": "批量审核通过"
}
```

#### 4. 获取审核统计数据
```http
GET /admin/audit/statistics?startTime=2024-01-01 00:00:00&endTime=2024-01-31 23:59:59
```

## 配置说明

### 审核阈值配置
在 `ContentAuditServiceImpl` 中可以调整以下阈值：
```java
// 审核阈值配置
private static final int AUTO_PASS_THRESHOLD = 80;  // 自动通过阈值
private static final int AUTO_REJECT_THRESHOLD = 30; // 自动拒绝阈值
```

### 评分规则
- 基础分数：100分
- 低级敏感词：扣5分
- 中级敏感词：扣15分  
- 高级敏感词：扣30分
- 内容过短（<5字符）：扣10分
- 内容过长（>5000字符）：扣5分

### 审核流程
1. **自动审核**：
   - 分数 >= 80：自动通过
   - 分数 < 30：自动拒绝
   - 30 <= 分数 < 80：待人工审核

2. **敏感词处理**：
   - action=1：替换为指定词汇或*号
   - action=2：直接拒绝内容
   - action=3：提交人工审核

## 使用示例

### 1. 发送消息时的审核
```java
// MessageServiceImpl.sendMessage() 中已集成审核功能
// 文本消息会自动进行敏感词检测和内容审核
```

### 2. 发布朋友圈时的审核
```java
// MomentServiceImpl.publishMoment() 中已集成审核功能
// 动态内容会自动进行敏感词检测和内容审核
```

### 3. 检测文本敏感词
```java
@Autowired
private SensitiveWordService sensitiveWordService;

// 检测敏感词
SensitiveWordFilter.SensitiveWordResult result = sensitiveWordService.checkSensitiveWords("待检测文本");
if (result.hasSensitiveWords()) {
    // 处理敏感词
}

// 过滤敏感词
String filteredText = sensitiveWordService.filterSensitiveWords("待过滤文本");
```

## 部署说明

1. **运行数据库脚本**：执行 `schema.sql` 创建相关表结构和初始数据
2. **启动应用**：敏感词过滤器会在应用启动时自动初始化
3. **管理敏感词库**：通过管理接口添加和维护敏感词
4. **监控审核状态**：通过审核管理接口查看和处理待审核内容

## 注意事项

1. **性能优化**：敏感词库较大时，建议使用Redis缓存提高检测效率
2. **定期维护**：定期更新敏感词库，删除过时的敏感词
3. **权限控制**：审核管理接口需要管理员权限
4. **数据备份**：定期备份审核记录，用于分析和改进
5. **扩展性**：系统设计支持扩展到更多内容类型的审核

## 技术栈

- **算法**: AC自动机（Aho-Corasick）
- **缓存**: Redis
- **数据库**: MySQL
- **框架**: Spring Boot + MyBatis Plus
- **注解**: Jakarta Validation

通过以上配置和使用，系统能够有效地检测和过滤不当内容，维护健康的社交环境。