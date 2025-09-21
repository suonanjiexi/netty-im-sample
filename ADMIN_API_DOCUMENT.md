# 运营管理后台API文档

## 1. 管理员管理

### 1.1 管理员登录
```
POST /admin/api/auth/login
请求参数：
{
  "username": "admin",
  "password": "admin123"
}
```

### 1.2 获取当前管理员信息
```
GET /admin/api/users/current
```

## 2. 用户管理

### 2.1 分页查询用户列表
```
GET /admin/api/users?page=1&size=10&keyword=张三&status=1
```

### 2.2 根据ID获取用户信息
```
GET /admin/api/users/{id}
```

### 2.3 更新用户状态
```
PUT /admin/api/users/{id}/status?status=1
```

### 2.4 删除用户
```
DELETE /admin/api/users/{id}
```

## 3. 内容审核管理

### 3.1 分页查询审核记录列表
```
GET /admin/api/content-audits?page=1&size=10&contentType=1&auditStatus=0&keyword=敏感词
```

### 3.2 根据ID获取审核记录
```
GET /admin/api/content-audits/{id}
```

### 3.3 人工审核内容
```
POST /admin/api/content-audits/{id}/review
请求参数：
{
  "auditStatus": 1,
  "auditResult": "审核通过",
  "auditorId": 1
}
```

### 3.4 批量审核内容
```
POST /admin/api/content-audits/batch-review
请求参数：
{
  "auditIds": "1,2,3",
  "auditStatus": 1,
  "auditResult": "审核通过",
  "auditorId": 1
}
```

### 3.5 获取审核统计数据
```
GET /admin/api/content-audits/statistics?startTime=2024-01-01 00:00:00&endTime=2024-01-31 23:59:59
```

## 4. 敏感词管理

### 4.1 分页查询敏感词列表
```
GET /admin/api/sensitive-words?page=1&size=10&category=政治&keyword=敏感词
```

### 4.2 添加敏感词
```
POST /admin/api/sensitive-words
请求参数：
{
  "word": "敏感词",
  "category": "政治",
  "level": 1,
  "description": "敏感词描述"
}
```

### 4.3 更新敏感词
```
PUT /admin/api/sensitive-words/{id}
请求参数：
{
  "word": "敏感词",
  "category": "政治",
  "level": 1,
  "description": "敏感词描述"
}
```

### 4.4 删除敏感词
```
DELETE /admin/api/sensitive-words/{id}
```

### 4.5 批量删除敏感词
```
DELETE /admin/api/sensitive-words/batch?ids=1,2,3
```

### 4.6 获取敏感词分类统计
```
GET /admin/api/sensitive-words/statistics
```

## 5. 会员管理

### 5.1 分页查询会员等级列表
```
GET /admin/api/memberships/levels?page=1&size=10
```

### 5.2 添加会员等级
```
POST /admin/api/memberships/levels
请求参数：
{
  "levelName": "黄金会员",
  "level": 2,
  "monthlyPrice": 29.9,
  "annualPrice": 299.0,
  "description": "黄金会员特权",
  "benefits": "特权1,特权2,特权3",
  "isActive": true
}
```

### 5.3 更新会员等级
```
PUT /admin/api/memberships/levels/{id}
请求参数：
{
  "levelName": "黄金会员",
  "level": 2,
  "monthlyPrice": 29.9,
  "annualPrice": 299.0,
  "description": "黄金会员特权",
  "benefits": "特权1,特权2,特权3",
  "isActive": true
}
```

### 5.4 删除会员等级
```
DELETE /admin/api/memberships/levels/{id}
```

### 5.5 分页查询用户会员信息
```
GET /admin/api/memberships/users?page=1&size=10&userId=1&levelId=1
```

### 5.6 更新用户会员状态
```
PUT /admin/api/memberships/users/{id}/status?status=1
```

## 6. 支付管理

### 6.1 分页查询支付订单列表
```
GET /admin/api/payments/orders?page=1&size=10&orderNo=ORDER123&userId=1&paymentStatus=1
```

### 6.2 根据ID获取支付订单详情
```
GET /admin/api/payments/orders/{id}
```

### 6.3 处理退款申请
```
POST /admin/api/payments/orders/{id}/refund
请求参数：
{
  "refundReason": "用户申请退款"
}
```

### 6.4 分页查询支付方式列表
```
GET /admin/api/payments/methods?page=1&size=10
```

### 6.5 添加支付方式
```
POST /admin/api/payments/methods
请求参数：
{
  "methodName": "微信支付",
  "methodType": "WECHAT",
  "description": "微信支付方式",
  "isActive": true
}
```

### 6.6 更新支付方式
```
PUT /admin/api/payments/methods/{id}
请求参数：
{
  "methodName": "微信支付",
  "methodType": "WECHAT",
  "description": "微信支付方式",
  "isActive": true
}
```

### 6.7 删除支付方式
```
DELETE /admin/api/payments/methods/{id}
```

### 6.8 更新支付方式状态
```
PUT /admin/api/payments/methods/{id}/status?status=1
```

## 7. 数据统计

### 7.1 分页查询数据统计列表
```
GET /admin/api/statistics?page=1&size=10&statType=daily&startTime=2024-01-01 00:00:00&endTime=2024-01-31 23:59:59
```

### 7.2 获取综合统计数据
```
GET /admin/api/statistics/overview?startTime=2024-01-01 00:00:00&endTime=2024-01-31 23:59:59
```

### 7.3 获取用户统计数据
```
GET /admin/api/statistics/users?startTime=2024-01-01 00:00:00&endTime=2024-01-31 23:59:59
```

### 7.4 获取内容统计数据
```
GET /admin/api/statistics/contents?startTime=2024-01-01 00:00:00&endTime=2024-01-31 23:59:59
```

### 7.5 获取支付统计数据
```
GET /admin/api/statistics/payments?startTime=2024-01-01 00:00:00&endTime=2024-01-31 23:59:59
```

### 7.6 获取会员统计数据
```
GET /admin/api/statistics/memberships?startTime=2024-01-01 00:00:00&endTime=2024-01-31 23:59:59
```

### 7.7 获取图表数据
```
GET /admin/api/statistics/chart?chartType=activeUsers&startTime=2024-01-01 00:00:00&endTime=2024-01-31 23:59:59
```

### 7.8 生成每日统计数据
```
POST /admin/api/statistics/generate?date=2024-01-01
```

### 7.9 根据ID获取统计数据
```
GET /admin/api/statistics/{id}
```

### 7.10 删除统计数据
```
DELETE /admin/api/statistics/{id}
```

## 8. 系统日志管理

### 8.1 分页查询操作日志列表
```
GET /admin/api/logs?page=1&size=10&adminUserId=1&module=用户管理&operation=删除
```

### 8.2 删除操作日志
```
DELETE /admin/api/logs/{id}
```

### 8.3 分页查询安全日志列表
```
GET /admin/api/security-logs?page=1&size=10&userId=1&actionType=LOGIN&riskLevel=1&startTime=2024-01-01 00:00:00&endTime=2024-01-31 23:59:59
```

### 8.4 获取安全日志统计数据
```
GET /admin/api/security-logs/statistics?startTime=2024-01-01 00:00:00&endTime=2024-01-31 23:59:59
```

### 8.5 根据ID获取安全日志
```
GET /admin/api/security-logs/{id}
```

### 8.6 删除安全日志
```
DELETE /admin/api/security-logs/{id}
```

### 8.7 批量删除安全日志
```
DELETE /admin/api/security-logs/batch?ids=1,2,3
```