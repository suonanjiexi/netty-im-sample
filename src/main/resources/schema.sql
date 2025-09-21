-- 即时通讯系统数据库设计
-- 创建数据库
CREATE DATABASE IF NOT EXISTS netty_im DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE netty_im;

-- 用户表
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(20) NOT NULL UNIQUE COMMENT '手机号',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    `nickname` VARCHAR(50) NOT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `online_status` TINYINT DEFAULT 0 COMMENT '在线状态：0-离线，1-在线，2-忙碌，3-离开',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `id_card_number` VARCHAR(18) DEFAULT NULL COMMENT '身份证号码',
    `real_name` VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    `identity_status` TINYINT DEFAULT 0 COMMENT '实名认证状态：0-未认证，1-已认证，2-认证失败',
    `id_card_front_url` VARCHAR(255) DEFAULT NULL COMMENT '身份证正面照片URL',
    `id_card_back_url` VARCHAR(255) DEFAULT NULL COMMENT '身份证反面照片URL',
    `identity_verify_time` DATETIME DEFAULT NULL COMMENT '实名认证时间',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_username` (`username`),
    INDEX `idx_email` (`email`),
    INDEX `idx_phone` (`phone`),
    INDEX `idx_status` (`status`),
    INDEX `idx_id_card_number` (`id_card_number`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 好友关系表
CREATE TABLE IF NOT EXISTS `friendships` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关系ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `friend_id` BIGINT NOT NULL COMMENT '好友ID',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0-待确认，1-已同意，2-已拒绝，3-已删除',
    `remark` VARCHAR(50) DEFAULT NULL COMMENT '备注名',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_user_friend` (`user_id`, `friend_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_friend_id` (`friend_id`),
    INDEX `idx_status` (`status`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`friend_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='好友关系表';

-- 群组表
CREATE TABLE IF NOT EXISTS `groups` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '群组ID',
    `name` VARCHAR(100) NOT NULL COMMENT '群组名称',
    `description` TEXT DEFAULT NULL COMMENT '群组描述',
    `creator_id` BIGINT NOT NULL COMMENT '创建者ID',
    `type` TINYINT DEFAULT 0 COMMENT '群组类型：0-普通群，1-超级群',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_name` (`name`),
    INDEX `idx_creator_id` (`creator_id`),
    INDEX `idx_type` (`type`),
    INDEX `idx_status` (`status`),
    FOREIGN KEY (`creator_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='群组表';

-- 群组成员表
CREATE TABLE IF NOT EXISTS `group_members` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '成员ID',
    `group_id` BIGINT NOT NULL COMMENT '群组ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role` TINYINT DEFAULT 0 COMMENT '角色：0-普通成员，1-管理员，2-群主',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `join_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_group_user` (`group_id`, `user_id`),
    INDEX `idx_group_id` (`group_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_role` (`role`),
    INDEX `idx_status` (`status`),
    FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='群组成员表';

-- 消息表
CREATE TABLE IF NOT EXISTS `messages` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '消息ID',
    `sender_id` BIGINT NOT NULL COMMENT '发送者ID',
    `receiver_id` BIGINT NOT NULL COMMENT '接收者ID',
    `content` TEXT NOT NULL COMMENT '消息内容',
    `type` TINYINT DEFAULT 0 COMMENT '消息类型：0-文本，1-图片，2-语音，3-视频，4-文件',
    `status` TINYINT DEFAULT 0 COMMENT '消息状态：0-未读，1-已读',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_sender_id` (`sender_id`),
    INDEX `idx_receiver_id` (`receiver_id`),
    INDEX `idx_type` (`type`),
    INDEX `idx_status` (`status`),
    FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';

-- 社交成就表
CREATE TABLE IF NOT EXISTS `social_achievements` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '成就ID',
    `name` VARCHAR(100) NOT NULL COMMENT '成就名称',
    `description` TEXT DEFAULT NULL COMMENT '成就描述',
    `target_value` INT NOT NULL COMMENT '目标值',
    `is_active` TINYINT DEFAULT 1 COMMENT '是否激活：0-未激活，1-已激活',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_name` (`name`),
    INDEX `idx_is_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社交成就表';

-- 用户成就表
CREATE TABLE IF NOT EXISTS `user_achievements` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户成就ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `achievement_id` BIGINT NOT NULL COMMENT '成就ID',
    `current_value` INT NOT NULL COMMENT '当前值',
    `target_value` INT NOT NULL COMMENT '目标值',
    `is_completed` TINYINT DEFAULT 0 COMMENT '是否完成：0-未完成，1-已完成',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_user_achievement` (`user_id`, `achievement_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_achievement_id` (`achievement_id`),
    INDEX `idx_is_completed` (`is_completed`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`achievement_id`) REFERENCES `social_achievements` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户成就表';

-- 插入默认社交成就
INSERT INTO `social_achievements` (`name`, `description`, `target_value`, `is_active`) VALUES
('First Message', 'Send your first message', 1, 1),
('Ten Messages', 'Send 10 messages', 10, 1),
('Fifty Messages', 'Send 50 messages', 50, 1),
('Hundred Messages', 'Send 100 messages', 100, 1),
('First Friend', 'Add your first friend', 1, 1),
('Ten Friends', 'Add 10 friends', 10, 1),
('Fifty Friends', 'Add 50 friends', 50, 1),
('Hundred Friends', 'Add 100 friends', 100, 1),
('First Group', 'Create your first group', 1, 1),
('Ten Groups', 'Create 10 groups', 10, 1),
('Fifty Groups', 'Create 50 groups', 50, 1),
('Hundred Groups', 'Create 100 groups', 100, 1);

-- 插入默认用户成就
INSERT INTO `user_achievements` (`user_id`, `achievement_id`, `current_value`, `target_value`, `is_completed`) 
SELECT u.id, sa.id, 0, sa.target_value, 0 
FROM `users` u 
CROSS JOIN `social_achievements` sa 
WHERE sa.is_active = 1 
AND NOT EXISTS (SELECT 1 FROM `user_achievements` ua WHERE ua.user_id = u.id AND ua.achievement_id = sa.id);

-- 运营管理后台管理员表
CREATE TABLE IF NOT EXISTS `admin_users` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '管理员ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    `nickname` VARCHAR(50) NOT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
    `role` VARCHAR(50) NOT NULL DEFAULT 'ADMIN' COMMENT '角色：ADMIN-超级管理员，OPERATOR-普通运营',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `last_login_ip` VARCHAR(45) DEFAULT NULL COMMENT '最后登录IP',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_username` (`username`),
    INDEX `idx_status` (`status`),
    INDEX `idx_role` (`role`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='运营管理后台管理员表';

-- 管理员操作日志表
CREATE TABLE IF NOT EXISTS `admin_operation_logs` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    `admin_user_id` BIGINT NOT NULL COMMENT '管理员ID',
    `admin_username` VARCHAR(50) NOT NULL COMMENT '管理员用户名',
    `operation` VARCHAR(100) NOT NULL COMMENT '操作类型',
    `module` VARCHAR(50) NOT NULL COMMENT '操作模块',
    `description` TEXT DEFAULT NULL COMMENT '操作描述',
    `request_method` VARCHAR(10) DEFAULT NULL COMMENT '请求方法',
    `request_url` VARCHAR(255) DEFAULT NULL COMMENT '请求URL',
    `request_params` JSON DEFAULT NULL COMMENT '请求参数',
    `response_result` JSON DEFAULT NULL COMMENT '响应结果',
    `ip_address` VARCHAR(45) DEFAULT NULL COMMENT 'IP地址',
    `user_agent` TEXT DEFAULT NULL COMMENT '用户代理',
    `execution_time` INT DEFAULT NULL COMMENT '执行时间（毫秒）',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_admin_user_id` (`admin_user_id`),
    INDEX `idx_operation` (`operation`),
    INDEX `idx_module` (`module`),
    INDEX `idx_created_at` (`created_at`),
    FOREIGN KEY (`admin_user_id`) REFERENCES `admin_users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员操作日志表';

-- 数据统计表
CREATE TABLE IF NOT EXISTS `data_statistics` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '统计ID',
    `stat_type` VARCHAR(50) NOT NULL COMMENT '统计类型：user-用户统计, content-内容统计, payment-支付统计, membership-会员统计',
    `stat_date` DATETIME NOT NULL COMMENT '统计日期',
    `new_users` INT DEFAULT 0 COMMENT '新增用户数',
    `active_users` INT DEFAULT 0 COMMENT '活跃用户数',
    `total_users` INT DEFAULT 0 COMMENT '总用户数',
    `new_contents` INT DEFAULT 0 COMMENT '新增内容数',
    `total_contents` INT DEFAULT 0 COMMENT '总内容数',
    `new_orders` INT DEFAULT 0 COMMENT '新增订单数',
    `total_orders` INT DEFAULT 0 COMMENT '总订单数',
    `order_amount` DECIMAL(15,2) DEFAULT 0.00 COMMENT '订单总金额',
    `new_members` INT DEFAULT 0 COMMENT '新增会员数',
    `total_members` INT DEFAULT 0 COMMENT '总会员数',
    `remark` TEXT DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_stat_type` (`stat_type`),
    INDEX `idx_stat_date` (`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='数据统计表';

-- 插入默认管理员账户
INSERT INTO `admin_users` (`username`, `password`, `nickname`, `role`, `status`) VALUES
('admin', '$2a$10$k9x8gNKvPEEwwsGhKCfNVOwP5WRE.QZuuNBXYkOHb1lHk8OV8qL3G', '超级管理员', 'ADMIN', 1);

    `name` VARCHAR(100) NOT NULL COMMENT '群组名称',
    `description` TEXT DEFAULT NULL COMMENT '群组描述',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '群组头像',
    `owner_id` BIGINT NOT NULL COMMENT '群主ID',
    `max_members` INT DEFAULT 500 COMMENT '最大成员数',
    `member_count` INT DEFAULT 0 COMMENT '当前成员数',
    `is_public` TINYINT DEFAULT 1 COMMENT '是否公开：0-私有，1-公开',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_owner_id` (`owner_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_is_public` (`is_public`),
    FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='群组表';

-- 群组成员表
CREATE TABLE IF NOT EXISTS `group_members` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '成员关系ID',
    `group_id` BIGINT NOT NULL COMMENT '群组ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role` TINYINT DEFAULT 0 COMMENT '角色：0-普通成员，1-管理员，2-群主',
    `join_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    `mute_until` DATETIME DEFAULT NULL COMMENT '禁言到期时间',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-已退出，1-正常',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_group_user` (`group_id`, `user_id`),
    INDEX `idx_group_id` (`group_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_role` (`role`),
    INDEX `idx_status` (`status`),
    FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='群组成员表';

-- 消息表
CREATE TABLE IF NOT EXISTS `messages` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '消息ID',
    `message_id` VARCHAR(64) NOT NULL UNIQUE COMMENT '消息唯一标识',
    `from_user_id` BIGINT NOT NULL COMMENT '发送者ID',
    `to_user_id` BIGINT DEFAULT NULL COMMENT '接收者ID（私聊）',
    `group_id` BIGINT DEFAULT NULL COMMENT '群组ID（群聊）',
    `message_type` TINYINT NOT NULL COMMENT '消息类型：1-文本，2-图片，3-文件，4-语音，5-视频，6-系统消息',
    `content` TEXT NOT NULL COMMENT '消息内容',
    `file_url` VARCHAR(500) DEFAULT NULL COMMENT '文件URL（非文本消息）',
    `file_size` BIGINT DEFAULT NULL COMMENT '文件大小',
    `file_name` VARCHAR(255) DEFAULT NULL COMMENT '文件名',
    `reply_to_message_id` VARCHAR(64) DEFAULT NULL COMMENT '回复的消息ID',
    `is_read` TINYINT DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-正常，1-已删除',
    `audit_status` TINYINT DEFAULT 3 COMMENT '审核状态：0-待审核，1-审核通过，2-审核拒绝，3-自动通过，4-自动拒绝',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_message_id` (`message_id`),
    INDEX `idx_from_user_id` (`from_user_id`),
    INDEX `idx_to_user_id` (`to_user_id`),
    INDEX `idx_group_id` (`group_id`),
    INDEX `idx_message_type` (`message_type`),
    INDEX `idx_created_at` (`created_at`),
    INDEX `idx_conversation` (`from_user_id`, `to_user_id`, `created_at`),
    INDEX `idx_group_conversation` (`group_id`, `created_at`),
    FOREIGN KEY (`from_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`to_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息表';

-- 会话表（用于优化消息列表查询）
CREATE TABLE IF NOT EXISTS `conversations` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '会话ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `target_id` BIGINT NOT NULL COMMENT '目标ID（好友ID或群组ID）',
    `conversation_type` TINYINT NOT NULL COMMENT '会话类型：1-私聊，2-群聊',
    `last_message_id` BIGINT DEFAULT NULL COMMENT '最后一条消息ID',
    `last_message_content` TEXT DEFAULT NULL COMMENT '最后一条消息内容',
    `last_message_time` DATETIME DEFAULT NULL COMMENT '最后一条消息时间',
    `unread_count` INT DEFAULT 0 COMMENT '未读消息数',
    `is_top` TINYINT DEFAULT 0 COMMENT '是否置顶：0-否，1-是',
    `is_muted` TINYINT DEFAULT 0 COMMENT '是否免打扰：0-否，1-是',
    `is_deleted` TINYINT DEFAULT 0 COMMENT '是否删除：0-否，1-是',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_user_target_type` (`user_id`, `target_id`, `conversation_type`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_target_id` (`target_id`),
    INDEX `idx_conversation_type` (`conversation_type`),
    INDEX `idx_last_message_time` (`last_message_time`),
    INDEX `idx_is_top` (`is_top`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会话表';

-- 用户会话状态表（记录用户在线状态和连接信息）
CREATE TABLE IF NOT EXISTS `user_sessions` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '会话ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `session_id` VARCHAR(255) NOT NULL COMMENT 'Socket会话ID',
    `device_type` VARCHAR(50) DEFAULT NULL COMMENT '设备类型',
    `ip_address` VARCHAR(45) DEFAULT NULL COMMENT 'IP地址',
    `user_agent` TEXT DEFAULT NULL COMMENT '用户代理',
    `login_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
    `last_active_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后活跃时间',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-离线，1-在线',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_session_id` (`session_id`),
    INDEX `idx_status` (`status`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户会话状态表';

-- 朋友圈动态表
CREATE TABLE IF NOT EXISTS `moments` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '动态ID',
    `user_id` BIGINT NOT NULL COMMENT '发布用户ID',
    `content` TEXT NOT NULL COMMENT '动态内容',
    `images` JSON DEFAULT NULL COMMENT '图片URL列表（JSON格式）',
    `location` VARCHAR(255) DEFAULT NULL COMMENT '位置信息',
    `visibility` TINYINT DEFAULT 0 COMMENT '可见性：0-公开，1-仅好友，2-仅自己',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `comment_count` INT DEFAULT 0 COMMENT '评论数',
    `audit_status` TINYINT DEFAULT 3 COMMENT '审核状态：0-待审核，1-审核通过，2-审核拒绝，3-自动通过，4-自动拒绝',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_created_at` (`created_at`),
    INDEX `idx_visibility` (`visibility`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='朋友圈动态表';

-- 朋友圈评论表
CREATE TABLE IF NOT EXISTS `moment_comments` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '评论ID',
    `moment_id` BIGINT NOT NULL COMMENT '动态ID',
    `user_id` BIGINT NOT NULL COMMENT '评论用户ID',
    `content` TEXT NOT NULL COMMENT '评论内容',
    `reply_to_user_id` BIGINT DEFAULT NULL COMMENT '回复的用户ID',
    `reply_to_comment_id` BIGINT DEFAULT NULL COMMENT '回复的评论ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_moment_id` (`moment_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_reply_to_user_id` (`reply_to_user_id`),
    INDEX `idx_reply_to_comment_id` (`reply_to_comment_id`),
    INDEX `idx_created_at` (`created_at`),
    FOREIGN KEY (`moment_id`) REFERENCES `moments` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`reply_to_user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL,
    FOREIGN KEY (`reply_to_comment_id`) REFERENCES `moment_comments` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='朋友圈评论表';

-- 朋友圈点赞表
CREATE TABLE IF NOT EXISTS `moment_likes` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '点赞ID',
    `moment_id` BIGINT NOT NULL COMMENT '动态ID',
    `user_id` BIGINT NOT NULL COMMENT '点赞用户ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_moment_user` (`moment_id`, `user_id`),
    INDEX `idx_moment_id` (`moment_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_created_at` (`created_at`),
    FOREIGN KEY (`moment_id`) REFERENCES `moments` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='朋友圈点赞表';

-- 贴吧表
CREATE TABLE IF NOT EXISTS `forums` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '贴吧ID',
    `name` VARCHAR(100) NOT NULL COMMENT '贴吧名称',
    `description` TEXT DEFAULT NULL COMMENT '贴吧描述',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '贴吧头像URL',
    `owner_id` BIGINT NOT NULL COMMENT '吧主ID',
    `category` VARCHAR(50) DEFAULT NULL COMMENT '分类',
    `member_count` INT DEFAULT 1 COMMENT '成员数',
    `post_count` INT DEFAULT 0 COMMENT '帖子数',
    `is_public` TINYINT DEFAULT 1 COMMENT '是否公开：0-私密，1-公开',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-正常',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_name` (`name`),
    INDEX `idx_owner_id` (`owner_id`),
    INDEX `idx_category` (`category`),
    INDEX `idx_is_public` (`is_public`),
    INDEX `idx_status` (`status`),
    INDEX `idx_created_at` (`created_at`),
    FOREIGN KEY (`owner_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='贴吧表';

-- 贴吧成员表
CREATE TABLE IF NOT EXISTS `forum_members` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '成员ID',
    `forum_id` BIGINT NOT NULL COMMENT '贴吧ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `role` TINYINT DEFAULT 0 COMMENT '角色：0-普通成员，1-管理员，2-吧主',
    `join_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '加入时间',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁言，1-正常',
    UNIQUE KEY `uk_forum_user` (`forum_id`, `user_id`),
    INDEX `idx_forum_id` (`forum_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_role` (`role`),
    INDEX `idx_join_time` (`join_time`),
    FOREIGN KEY (`forum_id`) REFERENCES `forums` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='贴吧成员表';

-- 贴吧帖子表
CREATE TABLE IF NOT EXISTS `forum_posts` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '帖子ID',
    `forum_id` BIGINT NOT NULL COMMENT '贴吧ID',
    `user_id` BIGINT NOT NULL COMMENT '发帖用户ID',
    `title` VARCHAR(200) NOT NULL COMMENT '帖子标题',
    `content` TEXT NOT NULL COMMENT '帖子内容',
    `images` JSON DEFAULT NULL COMMENT '图片URL列表（JSON格式）',
    `category` VARCHAR(50) DEFAULT NULL COMMENT '帖子分类',
    `view_count` INT DEFAULT 0 COMMENT '浏览数',
    `reply_count` INT DEFAULT 0 COMMENT '回复数',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `is_pinned` TINYINT DEFAULT 0 COMMENT '是否置顶：0-普通，1-置顶',
    `is_essence` TINYINT DEFAULT 0 COMMENT '是否精华：0-普通，1-精华',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-删除，1-正常',
    `last_reply_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '最后回复时间',
    `audit_status` TINYINT DEFAULT 3 COMMENT '审核状态：0-待审核，1-审核通过，2-审核拒绝，3-自动通过，4-自动拒绝',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_forum_id` (`forum_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_category` (`category`),
    INDEX `idx_is_pinned` (`is_pinned`),
    INDEX `idx_is_essence` (`is_essence`),
    INDEX `idx_status` (`status`),
    INDEX `idx_last_reply_time` (`last_reply_time`),
    INDEX `idx_created_at` (`created_at`),
    FULLTEXT KEY `ft_title_content` (`title`, `content`),
    FOREIGN KEY (`forum_id`) REFERENCES `forums` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='贴吧帖子表';

-- 贴吧回复表
CREATE TABLE IF NOT EXISTS `forum_replies` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '回复ID',
    `post_id` BIGINT NOT NULL COMMENT '帖子ID',
    `user_id` BIGINT NOT NULL COMMENT '回复用户ID',
    `content` TEXT NOT NULL COMMENT '回复内容',
    `images` JSON DEFAULT NULL COMMENT '图片URL列表（JSON格式）',
    `reply_to_user_id` BIGINT DEFAULT NULL COMMENT '回复的用户ID',
    `reply_to_reply_id` BIGINT DEFAULT NULL COMMENT '回复的回复ID',
    `floor_number` INT DEFAULT 0 COMMENT '楼层号',
    `like_count` INT DEFAULT 0 COMMENT '点赞数',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-删除，1-正常',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_post_id` (`post_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_reply_to_user_id` (`reply_to_user_id`),
    INDEX `idx_reply_to_reply_id` (`reply_to_reply_id`),
    INDEX `idx_floor_number` (`floor_number`),
    INDEX `idx_status` (`status`),
    INDEX `idx_created_at` (`created_at`),
    FOREIGN KEY (`post_id`) REFERENCES `forum_posts` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`reply_to_user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL,
    FOREIGN KEY (`reply_to_reply_id`) REFERENCES `forum_replies` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='贴吧回复表';

-- 贴吧帖子点赞表
CREATE TABLE IF NOT EXISTS `forum_post_likes` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '点赞ID',
    `post_id` BIGINT NOT NULL COMMENT '帖子ID',
    `user_id` BIGINT NOT NULL COMMENT '点赞用户ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_post_user` (`post_id`, `user_id`),
    INDEX `idx_post_id` (`post_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_created_at` (`created_at`),
    FOREIGN KEY (`post_id`) REFERENCES `forum_posts` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='贴吧帖子点赞表';

-- 贴吧回复点赞表
CREATE TABLE IF NOT EXISTS `forum_reply_likes` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '点赞ID',
    `reply_id` BIGINT NOT NULL COMMENT '回复ID',
    `user_id` BIGINT NOT NULL COMMENT '点赞用户ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_reply_user` (`reply_id`, `user_id`),
    INDEX `idx_reply_id` (`reply_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_created_at` (`created_at`),
    FOREIGN KEY (`reply_id`) REFERENCES `forum_replies` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='贴吧回复点赞表';

-- 会员等级表
CREATE TABLE IF NOT EXISTS `membership_levels` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '会员等级ID',
    `level_name` VARCHAR(50) NOT NULL COMMENT '会员等级名称',
    `level_code` VARCHAR(20) NOT NULL UNIQUE COMMENT '会员等级代码',
    `level_order` INT NOT NULL COMMENT '等级排序（数字越大等级越高）',
    `description` TEXT DEFAULT NULL COMMENT '等级描述',
    `icon_url` VARCHAR(255) DEFAULT NULL COMMENT '等级图标URL',
    `background_color` VARCHAR(10) DEFAULT NULL COMMENT '背景颜色',
    `text_color` VARCHAR(10) DEFAULT NULL COMMENT '文字颜色',
    `price_monthly` DECIMAL(10,2) DEFAULT 0.00 COMMENT '月费价格',
    `price_yearly` DECIMAL(10,2) DEFAULT 0.00 COMMENT '年费价格',
    `trial_days` INT DEFAULT 0 COMMENT '免费试用天数',
    `max_groups` INT DEFAULT 10 COMMENT '最大可加入群组数',
    `max_friends` INT DEFAULT 100 COMMENT '最大好友数',
    `max_file_size` BIGINT DEFAULT 10485760 COMMENT '最大文件上传大小（字节）',
    `max_storage_size` BIGINT DEFAULT 1073741824 COMMENT '最大存储空间（字节）',
    `can_create_group` TINYINT DEFAULT 1 COMMENT '是否可以创建群组',
    `can_upload_file` TINYINT DEFAULT 1 COMMENT '是否可以上传文件',
    `can_video_call` TINYINT DEFAULT 0 COMMENT '是否支持视频通话',
    `can_voice_call` TINYINT DEFAULT 1 COMMENT '是否支持语音通话',
    `can_live_stream` TINYINT DEFAULT 0 COMMENT '是否支持直播功能',
    `ad_free` TINYINT DEFAULT 0 COMMENT '是否免广告',
    `priority_support` TINYINT DEFAULT 0 COMMENT '是否享受优先客服',
    `custom_theme` TINYINT DEFAULT 0 COMMENT '是否支持自定义主题',
    `special_badge` TINYINT DEFAULT 0 COMMENT '是否有特殊徽章',
    `is_active` TINYINT DEFAULT 1 COMMENT '是否激活',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_level_code` (`level_code`),
    INDEX `idx_level_order` (`level_order`),
    INDEX `idx_is_active` (`is_active`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员等级表';

-- 用户会员信息表
CREATE TABLE IF NOT EXISTS `user_memberships` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '会员信息ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `level_id` BIGINT NOT NULL COMMENT '会员等级ID',
    `status` TINYINT DEFAULT 1 COMMENT '会员状态：0-停用，1-正常，2-过期，3-暂停',
    `start_time` DATETIME NOT NULL COMMENT '会员开始时间',
    `end_time` DATETIME DEFAULT NULL COMMENT '会员结束时间',
    `is_trial` TINYINT DEFAULT 0 COMMENT '是否试用：0-非试用，1-试用',
    `trial_end_time` DATETIME DEFAULT NULL COMMENT '试用结束时间',
    `auto_renew` TINYINT DEFAULT 0 COMMENT '是否自动续费：0-不自动，1-自动',
    `renew_type` TINYINT DEFAULT 1 COMMENT '续费类型：1-月费，2-年费',
    `total_paid` DECIMAL(10,2) DEFAULT 0.00 COMMENT '累计支付金额',
    `points_earned` INT DEFAULT 0 COMMENT '累计获得积分',
    `points_used` INT DEFAULT 0 COMMENT '累计使用积分',
    `referral_code` VARCHAR(20) DEFAULT NULL COMMENT '推荐码',
    `referrer_user_id` BIGINT DEFAULT NULL COMMENT '推荐人用户ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_user_id` (`user_id`),
    INDEX `idx_level_id` (`level_id`),
    INDEX `idx_status` (`status`),
    INDEX `idx_start_time` (`start_time`),
    INDEX `idx_end_time` (`end_time`),
    INDEX `idx_is_trial` (`is_trial`),
    INDEX `idx_referral_code` (`referral_code`),
    INDEX `idx_referrer_user_id` (`referrer_user_id`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`level_id`) REFERENCES `membership_levels` (`id`) ON DELETE RESTRICT,
    FOREIGN KEY (`referrer_user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户会员信息表';

-- 会员权益记录表
CREATE TABLE IF NOT EXISTS `membership_benefits` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '权益ID',
    `level_id` BIGINT NOT NULL COMMENT '会员等级ID',
    `benefit_type` VARCHAR(50) NOT NULL COMMENT '权益类型',
    `benefit_name` VARCHAR(100) NOT NULL COMMENT '权益名称',
    `benefit_description` TEXT DEFAULT NULL COMMENT '权益描述',
    `benefit_value` VARCHAR(255) DEFAULT NULL COMMENT '权益值',
    `is_active` TINYINT DEFAULT 1 COMMENT '是否激活',
    `sort_order` INT DEFAULT 0 COMMENT '排序顺序',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_level_id` (`level_id`),
    INDEX `idx_benefit_type` (`benefit_type`),
    INDEX `idx_is_active` (`is_active`),
    INDEX `idx_sort_order` (`sort_order`),
    FOREIGN KEY (`level_id`) REFERENCES `membership_levels` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员权益表';

-- 会员积分记录表
CREATE TABLE IF NOT EXISTS `membership_points` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '积分记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `points_type` TINYINT NOT NULL COMMENT '积分类型：1-获得，2-消费',
    `points_amount` INT NOT NULL COMMENT '积分数量',
    `source_type` VARCHAR(50) NOT NULL COMMENT '来源类型：登录、充值、消费、推荐等',
    `source_id` BIGINT DEFAULT NULL COMMENT '来源ID（如订单ID、推荐ID等）',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
    `balance_after` INT NOT NULL COMMENT '操作后余额',
    `expire_time` DATETIME DEFAULT NULL COMMENT '过期时间',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_points_type` (`points_type`),
    INDEX `idx_source_type` (`source_type`),
    INDEX `idx_source_id` (`source_id`),
    INDEX `idx_expire_time` (`expire_time`),
    INDEX `idx_created_at` (`created_at`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员积分记录表';

-- 支付方式表
CREATE TABLE IF NOT EXISTS `payment_methods` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '支付方式ID',
    `method_code` VARCHAR(20) NOT NULL UNIQUE COMMENT '支付方式代码',
    `method_name` VARCHAR(50) NOT NULL COMMENT '支付方式名称',
    `method_type` TINYINT NOT NULL COMMENT '支付类型：1-第三方支付，2-银行卡，3-余额支付',
    `icon_url` VARCHAR(255) DEFAULT NULL COMMENT '图标URL',
    `description` TEXT DEFAULT NULL COMMENT '描述',
    `config_json` JSON DEFAULT NULL COMMENT '配置信息（JSON格式）',
    `fee_rate` DECIMAL(5,4) DEFAULT 0.0000 COMMENT '手续费率',
    `min_amount` DECIMAL(10,2) DEFAULT 0.01 COMMENT '最小支付金额',
    `max_amount` DECIMAL(10,2) DEFAULT 999999.99 COMMENT '最大支付金额',
    `is_active` TINYINT DEFAULT 1 COMMENT '是否启用',
    `sort_order` INT DEFAULT 0 COMMENT '显示顺序',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_method_code` (`method_code`),
    INDEX `idx_method_type` (`method_type`),
    INDEX `idx_is_active` (`is_active`),
    INDEX `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='支付方式表';

-- 支付订单表
CREATE TABLE IF NOT EXISTS `payment_orders` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    `order_no` VARCHAR(64) NOT NULL UNIQUE COMMENT '订单号',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `product_type` TINYINT NOT NULL COMMENT '产品类型：1-会员升级，2-会员续费，3-积分充值，4-虚拟物品',
    `product_id` BIGINT DEFAULT NULL COMMENT '产品ID（如会员等级ID）',
    `product_name` VARCHAR(100) NOT NULL COMMENT '产品名称',
    `product_description` TEXT DEFAULT NULL COMMENT '产品描述',
    `original_amount` DECIMAL(10,2) NOT NULL COMMENT '原始金额',
    `discount_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '优惠金额',
    `final_amount` DECIMAL(10,2) NOT NULL COMMENT '实际支付金额',
    `payment_method_id` BIGINT DEFAULT NULL COMMENT '支付方式ID',
    `payment_status` TINYINT DEFAULT 0 COMMENT '支付状态：0-待支付，1-支付中，2-支付成功，3-支付失败，4-已退款，5-已取消',
    `trade_no` VARCHAR(255) DEFAULT NULL COMMENT '第三方交易号',
    `paid_time` DATETIME DEFAULT NULL COMMENT '支付时间',
    `callback_data` JSON DEFAULT NULL COMMENT '支付回调数据',
    `refund_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '退款金额',
    `refund_time` DATETIME DEFAULT NULL COMMENT '退款时间',
    `refund_reason` VARCHAR(255) DEFAULT NULL COMMENT '退款原因',
    `expire_time` DATETIME DEFAULT NULL COMMENT '订单过期时间',
    `remark` TEXT DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_order_no` (`order_no`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_product_type` (`product_type`),
    INDEX `idx_product_id` (`product_id`),
    INDEX `idx_payment_method_id` (`payment_method_id`),
    INDEX `idx_payment_status` (`payment_status`),
    INDEX `idx_trade_no` (`trade_no`),
    INDEX `idx_paid_time` (`paid_time`),
    INDEX `idx_expire_time` (`expire_time`),
    INDEX `idx_created_at` (`created_at`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`payment_method_id`) REFERENCES `payment_methods` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='支付订单表';

-- 用户钱包表
CREATE TABLE IF NOT EXISTS `user_wallets` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '钱包ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `balance` DECIMAL(10,2) DEFAULT 0.00 COMMENT '余额',
    `frozen_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '冻结金额',
    `total_recharge` DECIMAL(10,2) DEFAULT 0.00 COMMENT '累计充值',
    `total_consumption` DECIMAL(10,2) DEFAULT 0.00 COMMENT '累计消费',
    `payment_password` VARCHAR(255) DEFAULT NULL COMMENT '支付密码',
    `is_locked` TINYINT DEFAULT 0 COMMENT '是否锁定：0-正常，1-锁定',
    `lock_reason` VARCHAR(255) DEFAULT NULL COMMENT '锁定原因',
    `lock_time` DATETIME DEFAULT NULL COMMENT '锁定时间',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_user_id` (`user_id`),
    INDEX `idx_balance` (`balance`),
    INDEX `idx_is_locked` (`is_locked`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户钱包表';

-- 钱包交易记录表
CREATE TABLE IF NOT EXISTS `wallet_transactions` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '交易记录ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `transaction_no` VARCHAR(64) NOT NULL UNIQUE COMMENT '交易号',
    `transaction_type` TINYINT NOT NULL COMMENT '交易类型：1-充值，2-消费，3-退款，4-冻结，5-解冻，6-转入，7-转出',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '交易金额',
    `balance_before` DECIMAL(10,2) NOT NULL COMMENT '交易前余额',
    `balance_after` DECIMAL(10,2) NOT NULL COMMENT '交易后余额',
    `related_order_no` VARCHAR(64) DEFAULT NULL COMMENT '关联订单号',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '交易描述',
    `remark` TEXT DEFAULT NULL COMMENT '备注',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_transaction_no` (`transaction_no`),
    INDEX `idx_transaction_type` (`transaction_type`),
    INDEX `idx_related_order_no` (`related_order_no`),
    INDEX `idx_created_at` (`created_at`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='钱包交易记录表';

-- 敏感词表
CREATE TABLE IF NOT EXISTS `sensitive_words` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '敏感词ID',
    `word` VARCHAR(100) NOT NULL COMMENT '敏感词内容',
    `category` TINYINT NOT NULL COMMENT '敏感词分类：1-政治敏感，2-色情低俗，3-暴力血腥，4-赌博诈骗，5-毒品违法，6-其他',
    `level` TINYINT NOT NULL COMMENT '敏感等级：1-低，2-中，3-高',
    `action` TINYINT NOT NULL COMMENT '处理方式：1-替换，2-拒绝，3-人工审核',
    `replacement` VARCHAR(100) DEFAULT NULL COMMENT '替换词（当action=1时使用）',
    `status` TINYINT DEFAULT 1 COMMENT '是否启用：0-禁用，1-启用',
    `creator_id` BIGINT NOT NULL COMMENT '创建者ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_word` (`word`),
    INDEX `idx_category` (`category`),
    INDEX `idx_level` (`level`),
    INDEX `idx_status` (`status`),
    INDEX `idx_creator_id` (`creator_id`),
    INDEX `idx_created_at` (`created_at`),
    FOREIGN KEY (`creator_id`) REFERENCES `users` (`id`) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='敏感词表';

-- 内容审核记录表
CREATE TABLE IF NOT EXISTS `content_audits` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '审核记录ID',
    `content_type` TINYINT NOT NULL COMMENT '内容类型：1-消息，2-朋友圈动态，3-朋友圈评论，4-贴吧帖子，5-贴吧回复，6-用户资料',
    `content_id` BIGINT NOT NULL COMMENT '内容ID',
    `user_id` BIGINT NOT NULL COMMENT '内容发布者ID',
    `content` TEXT NOT NULL COMMENT '审核内容（原始内容）',
    `audit_status` TINYINT DEFAULT 0 COMMENT '审核状态：0-待审核，1-审核通过，2-审核拒绝，3-自动通过，4-自动拒绝',
    `audit_result` TEXT DEFAULT NULL COMMENT '审核结果：审核通过/拒绝的原因',
    `auditor_id` BIGINT DEFAULT NULL COMMENT '审核员ID（人工审核）',
    `audit_type` TINYINT DEFAULT 0 COMMENT '审核方式：0-自动审核，1-人工审核',
    `sensitive_words` TEXT DEFAULT NULL COMMENT '敏感词匹配结果',
    `audit_score` TINYINT DEFAULT NULL COMMENT '审核分数（0-100，分数越高越安全）',
    `audit_time` DATETIME DEFAULT NULL COMMENT '审核时间',
    `need_manual_review` TINYINT DEFAULT 0 COMMENT '是否需要人工复审：0-不需要，1-需要',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_content_type` (`content_type`),
    INDEX `idx_content_id` (`content_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_audit_status` (`audit_status`),
    INDEX `idx_auditor_id` (`auditor_id`),
    INDEX `idx_audit_type` (`audit_type`),
    INDEX `idx_need_manual_review` (`need_manual_review`),
    INDEX `idx_audit_time` (`audit_time`),
    INDEX `idx_created_at` (`created_at`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`auditor_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='内容审核记录表';

-- 插入测试数据
INSERT INTO `users` (`username`, `email`, `password`, `nickname`, `status`) VALUES
('admin', 'admin@example.com', '$2a$10$k9x8gNKvPEEwwsGhKCfNVOwP5WRE.QZuuNBXYkOHb1lHk8OV8qL3G', '管理员', 1),
('user1', 'user1@example.com', '$2a$10$k9x8gNKvPEEwwsGhKCfNVOwP5WRE.QZuuNBXYkOHb1lHk8OV8qL3G', '用户1', 1),
('user2', 'user2@example.com', '$2a$10$k9x8gNKvPEEwwsGhKCfNVOwP5WRE.QZuuNBXYkOHb1lHk8OV8qL3G', '用户2', 1);

-- 插入初始敏感词数据（扩容版）
INSERT INTO `sensitive_words` (`word`, `category`, `level`, `action`, `replacement`, `status`, `creator_id`) VALUES
-- 政治敏感类（category = 1）
('投毒', 1, 3, 2, NULL, 1, 1),
('毒草', 1, 3, 2, NULL, 1, 1),
('核弹', 1, 3, 2, NULL, 1, 1),
('革命', 1, 2, 3, NULL, 1, 1),
('推翻', 1, 3, 2, NULL, 1, 1),
('造反', 1, 3, 2, NULL, 1, 1),
('暴动', 1, 3, 2, NULL, 1, 1),
('分裂', 1, 2, 3, NULL, 1, 1),
('独立', 1, 2, 3, NULL, 1, 1),
('恐怖主义', 1, 3, 2, NULL, 1, 1),
('极端主义', 1, 3, 2, NULL, 1, 1),
('颠覆', 1, 3, 2, NULL, 1, 1),

-- 色情低俗类（category = 2）
('色情', 2, 2, 1, '***', 1, 1),
('黄色', 2, 2, 1, '***', 1, 1),
('成人', 2, 1, 1, '***', 1, 1),
('淫秽', 2, 3, 2, NULL, 1, 1),
('裸体', 2, 2, 1, '***', 1, 1),
('性交', 2, 3, 2, NULL, 1, 1),
('做爱', 2, 3, 2, NULL, 1, 1),
('性爱', 2, 3, 2, NULL, 1, 1),
('约炮', 2, 3, 2, NULL, 1, 1),
('援交', 2, 3, 2, NULL, 1, 1),
('卖淫', 2, 3, 2, NULL, 1, 1),
('嫖娼', 2, 3, 2, NULL, 1, 1),
('脱光', 2, 2, 1, '***', 1, 1),
('激情', 2, 2, 1, '***', 1, 1),
('三级片', 2, 2, 1, '***', 1, 1),
('一夜情', 2, 2, 1, '***', 1, 1),
('包养', 2, 2, 1, '***', 1, 1),
('情色', 2, 2, 1, '***', 1, 1),

-- 暴力血腥类（category = 3）
('杀手', 3, 2, 1, '***', 1, 1),
('血腥', 3, 2, 1, '***', 1, 1),
('暴力', 3, 2, 1, '***', 1, 1),
('杀人', 3, 3, 2, NULL, 1, 1),
('杀死', 3, 3, 2, NULL, 1, 1),
('谋杀', 3, 3, 2, NULL, 1, 1),
('屠杀', 3, 3, 2, NULL, 1, 1),
('砍死', 3, 3, 2, NULL, 1, 1),
('自杀', 3, 2, 3, NULL, 1, 1),
('自残', 3, 2, 3, NULL, 1, 1),
('仇杀', 3, 3, 2, NULL, 1, 1),
('械斗', 3, 2, 1, '***', 1, 1),
('斗殴', 3, 2, 1, '***', 1, 1),
('报复', 3, 2, 1, '***', 1, 1),
('恐吓', 3, 2, 1, '***', 1, 1),
('威胁', 3, 2, 1, '***', 1, 1),
('刺杀', 3, 3, 2, NULL, 1, 1),
('爆炸', 3, 3, 2, NULL, 1, 1),
('炸弹', 3, 3, 2, NULL, 1, 1),
('枪击', 3, 3, 2, NULL, 1, 1),
('砍杀', 3, 3, 2, NULL, 1, 1),

-- 赌博诈骗类（category = 4）
('赌博', 4, 3, 2, NULL, 1, 1),
('诈骗', 4, 3, 2, NULL, 1, 1),
('赌场', 4, 3, 2, NULL, 1, 1),
('博彩', 4, 3, 2, NULL, 1, 1),
('六合彩', 4, 3, 2, NULL, 1, 1),
('时时彩', 4, 3, 2, NULL, 1, 1),
('老虎机', 4, 3, 2, NULL, 1, 1),
('百家乐', 4, 3, 2, NULL, 1, 1),
('网络赌博', 4, 3, 2, NULL, 1, 1),
('地下赌场', 4, 3, 2, NULL, 1, 1),
('洗钱', 4, 3, 2, NULL, 1, 1),
('传销', 4, 3, 2, NULL, 1, 1),
('非法集资', 4, 3, 2, NULL, 1, 1),
('庞氏骗局', 4, 3, 2, NULL, 1, 1),
('网络诈骗', 4, 3, 2, NULL, 1, 1),
('电信诈骗', 4, 3, 2, NULL, 1, 1),
('虚假投资', 4, 3, 2, NULL, 1, 1),
('高利贷', 4, 3, 2, NULL, 1, 1),
('套路贷', 4, 3, 2, NULL, 1, 1),
('网贷', 4, 2, 3, NULL, 1, 1),
('校园贷', 4, 3, 2, NULL, 1, 1),

-- 毒品违法类（category = 5）
('毒品', 5, 3, 2, NULL, 1, 1),
('毒贩', 5, 3, 2, NULL, 1, 1),
('吸毒', 5, 3, 2, NULL, 1, 1),
('海洛因', 5, 3, 2, NULL, 1, 1),
('冰毒', 5, 3, 2, NULL, 1, 1),
('摇头丸', 5, 3, 2, NULL, 1, 1),
('大麻', 5, 3, 2, NULL, 1, 1),
('可卡因', 5, 3, 2, NULL, 1, 1),
('鸦片', 5, 3, 2, NULL, 1, 1),
('K粉', 5, 3, 2, NULL, 1, 1),
('麻古', 5, 3, 2, NULL, 1, 1),
('安非他明', 5, 3, 2, NULL, 1, 1),
('芬太尼', 5, 3, 2, NULL, 1, 1),
('制毒', 5, 3, 2, NULL, 1, 1),
('贩毒', 5, 3, 2, NULL, 1, 1),
('运毒', 5, 3, 2, NULL, 1, 1),
('藏毒', 5, 3, 2, NULL, 1, 1),
('毒窝', 5, 3, 2, NULL, 1, 1),
('毒资', 5, 3, 2, NULL, 1, 1),
('吸食', 5, 3, 2, NULL, 1, 1),

-- 其他不当内容类（category = 6）
('垃圾', 6, 1, 1, '*', 1, 1),
('笨蛋', 6, 1, 1, '*', 1, 1),
('白痴', 6, 1, 1, '*', 1, 1),
('蠢货', 6, 1, 1, '*', 1, 1),
('傻逼', 6, 2, 1, '***', 1, 1),
('草泥马', 6, 2, 1, '***', 1, 1),
('妈的', 6, 1, 1, '**', 1, 1),
('他妈的', 6, 2, 1, '***', 1, 1),
('去死', 6, 2, 1, '***', 1, 1),
('滚蛋', 6, 1, 1, '**', 1, 1),
('混蛋', 6, 1, 1, '**', 1, 1),
('王八蛋', 6, 2, 1, '***', 1, 1),
('贱人', 6, 2, 1, '**', 1, 1),
('臭婊子', 6, 2, 1, '***', 1, 1),
('垃圾人', 6, 1, 1, '***', 1, 1),
('脑残', 6, 1, 1, '**', 1, 1),
('智障', 6, 1, 1, '**', 1, 1),
('神经病', 6, 1, 1, '***', 1, 1),
('变态', 6, 1, 1, '**', 1, 1),
('恶心', 6, 1, 1, '**', 1, 1),
('讨厌', 6, 1, 1, '**', 1, 1),
('烦人', 6, 1, 1, '**', 1, 1),
('恶心人', 6, 1, 1, '***', 1, 1),
('死胖子', 6, 1, 1, '***', 1, 1),
('丑八怪', 6, 1, 1, '***', 1, 1),
('人渣', 6, 2, 1, '**', 1, 1),
('败类', 6, 1, 1, '**', 1, 1),
('狗东西', 6, 2, 1, '***', 1, 1),
('畜生', 6, 2, 1, '**', 1, 1),
('禽兽', 6, 2, 1, '**', 1, 1);

-- 插入初始会员等级数据
INSERT INTO `membership_levels` (`level_name`, `level_code`, `level_order`, `description`, `icon_url`, `background_color`, `text_color`, `price_monthly`, `price_yearly`, `trial_days`, `max_groups`, `max_friends`, `max_file_size`, `max_storage_size`, `can_create_group`, `can_upload_file`, `can_video_call`, `can_voice_call`, `can_live_stream`, `ad_free`, `priority_support`, `custom_theme`, `special_badge`, `is_active`) VALUES
('普通用户', 'FREE', 0, '免费用户，享受基本功能', NULL, '#E5E5E5', '#666666', 0.00, 0.00, 0, 5, 50, 5242880, 104857600, 1, 1, 0, 1, 0, 0, 0, 0, 0, 1),
('VIP会员', 'VIP', 1, 'VIP会员，享受更多便利功能', NULL, '#FFD700', '#FFFFFF', 19.90, 199.00, 7, 20, 200, 52428800, 1073741824, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1),
('超级VIP', 'SVIP', 2, '超级VIP会员，享受最高级的服务', NULL, '#FF6B6B', '#FFFFFF', 39.90, 399.00, 15, 50, 500, 104857600, 5368709120, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
('终身会员', 'LIFETIME', 3, '终身会员，一次付费终身享受', NULL, '#8A2BE2', '#FFFFFF', 0.00, 1999.00, 30, 100, 1000, 209715200, 10737418240, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);

-- 插入初始支付方式数据
INSERT INTO `payment_methods` (`method_code`, `method_name`, `method_type`, `icon_url`, `description`, `config_json`, `fee_rate`, `min_amount`, `max_amount`, `is_active`, `sort_order`) VALUES
('WECHAT_PAY', '微信支付', 1, '/icons/wechat-pay.png', '微信支付，支持扫码和小程序支付', NULL, 0.0060, 0.01, 50000.00, 1, 1),
('ALIPAY', '支付宝', 1, '/icons/alipay.png', '支付宝支付，安全快捷', NULL, 0.0060, 0.01, 50000.00, 1, 2),
('UNION_PAY', '中国银联', 2, '/icons/unionpay.png', '中国银联卡支付', NULL, 0.0080, 1.00, 50000.00, 1, 3),
('BANK_CARD', '银行卡支付', 2, '/icons/bank-card.png', '支持各大银行借记卡', NULL, 0.0080, 1.00, 100000.00, 1, 4),
('WALLET_PAY', '余额支付', 3, '/icons/wallet.png', '使用账户余额支付', NULL, 0.0000, 0.01, 10000.00, 1, 5),
('PAYPAL', 'PayPal', 1, '/icons/paypal.png', 'PayPal国际支付', NULL, 0.0299, 1.00, 10000.00, 1, 6);

-- 为所有现有用户创建默认会员记录（普通用户）
INSERT INTO `user_memberships` (`user_id`, `level_id`, `status`, `start_time`, `end_time`, `is_trial`, `auto_renew`, `renew_type`, `total_paid`, `points_earned`, `points_used`) 
SELECT u.id, 1, 1, NOW(), NULL, 0, 0, 1, 0.00, 0, 0 
FROM `users` u 
WHERE NOT EXISTS (SELECT 1 FROM `user_memberships` um WHERE um.user_id = u.id);

-- 为所有现有用户创建默认钱包
INSERT INTO `user_wallets` (`user_id`, `balance`, `frozen_amount`, `total_recharge`, `total_consumption`, `is_locked`) 
SELECT u.id, 0.00, 0.00, 0.00, 0.00, 0 
FROM `users` u 
WHERE NOT EXISTS (SELECT 1 FROM `user_wallets` uw WHERE uw.user_id = u.id);

-- 用户推荐系统表
CREATE TABLE IF NOT EXISTS `user_recommendations` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '推荐ID',
    `user_id` BIGINT NOT NULL COMMENT '被推荐用户ID',
    `recommended_user_id` BIGINT NOT NULL COMMENT '推荐的用户ID',
    `recommendation_type` TINYINT NOT NULL COMMENT '推荐类型：1-通讯录，2-位置，3-兴趣，4-共同好友，5-群组',
    `score` DECIMAL(5,2) DEFAULT 0.00 COMMENT '推荐分数',
    `reason` VARCHAR(255) DEFAULT NULL COMMENT '推荐原因',
    `status` TINYINT DEFAULT 0 COMMENT '状态：0-待处理，1-已接受，2-已拒绝，3-已忽略',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_recommended_user_id` (`recommended_user_id`),
    INDEX `idx_recommendation_type` (`recommendation_type`),
    INDEX `idx_score` (`score`),
    INDEX `idx_status` (`status`),
    INDEX `idx_created_at` (`created_at`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`recommended_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户推荐表';

-- 用户兴趣标签表
CREATE TABLE IF NOT EXISTS `user_interests` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '兴趣ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `interest_category` VARCHAR(50) NOT NULL COMMENT '兴趣分类',
    `interest_tag` VARCHAR(100) NOT NULL COMMENT '兴趣标签',
    `weight` DECIMAL(3,2) DEFAULT 1.00 COMMENT '权重',
    `source` VARCHAR(50) DEFAULT 'USER' COMMENT '来源：USER-用户设置，SYSTEM-系统推断',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_user_interest` (`user_id`, `interest_category`, `interest_tag`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_interest_category` (`interest_category`),
    INDEX `idx_interest_tag` (`interest_tag`),
    INDEX `idx_weight` (`weight`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户兴趣标签表';

-- 用户位置信息表
CREATE TABLE IF NOT EXISTS `user_locations` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '位置ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `latitude` DECIMAL(10,6) NOT NULL COMMENT '纬度',
    `longitude` DECIMAL(10,6) NOT NULL COMMENT '经度',
    `address` VARCHAR(255) DEFAULT NULL COMMENT '地址',
    `city` VARCHAR(100) DEFAULT NULL COMMENT '城市',
    `province` VARCHAR(100) DEFAULT NULL COMMENT '省份',
    `country` VARCHAR(100) DEFAULT NULL COMMENT '国家',
    `is_current` TINYINT DEFAULT 1 COMMENT '是否当前位置',
    `privacy_level` TINYINT DEFAULT 1 COMMENT '隐私级别：0-不显示，1-好友可见，2-公开',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_location` (`latitude`, `longitude`),
    INDEX `idx_city` (`city`),
    INDEX `idx_is_current` (`is_current`),
    INDEX `idx_privacy_level` (`privacy_level`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户位置信息表';

-- 内容推荐表
CREATE TABLE IF NOT EXISTS `content_recommendations` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '推荐ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `content_type` TINYINT NOT NULL COMMENT '内容类型：1-朋友圈，2-贴吧帖子，3-群组，4-用户',
    `content_id` BIGINT NOT NULL COMMENT '内容ID',
    `score` DECIMAL(5,2) DEFAULT 0.00 COMMENT '推荐分数',
    `algorithm` VARCHAR(50) DEFAULT NULL COMMENT '推荐算法',
    `reason` VARCHAR(255) DEFAULT NULL COMMENT '推荐原因',
    `is_shown` TINYINT DEFAULT 0 COMMENT '是否已展示',
    `is_clicked` TINYINT DEFAULT 0 COMMENT '是否已点击',
    `shown_at` DATETIME DEFAULT NULL COMMENT '展示时间',
    `clicked_at` DATETIME DEFAULT NULL COMMENT '点击时间',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_content_type` (`content_type`),
    INDEX `idx_content_id` (`content_id`),
    INDEX `idx_score` (`score`),
    INDEX `idx_is_shown` (`is_shown`),
    INDEX `idx_created_at` (`created_at`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='内容推荐表';

-- 用户签到记录表
CREATE TABLE IF NOT EXISTS `user_check_ins` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '签到ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `check_in_date` DATE NOT NULL COMMENT '签到日期',
    `continuous_days` INT DEFAULT 1 COMMENT '连续签到天数',
    `points_earned` INT DEFAULT 0 COMMENT '获得积分',
    `bonus_type` VARCHAR(50) DEFAULT NULL COMMENT '奖励类型',
    `bonus_amount` DECIMAL(10,2) DEFAULT 0.00 COMMENT '奖励金额',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_user_date` (`user_id`, `check_in_date`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_check_in_date` (`check_in_date`),
    INDEX `idx_continuous_days` (`continuous_days`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户签到记录表';

-- 社交成就表
CREATE TABLE IF NOT EXISTS `social_achievements` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '成就ID',
    `achievement_code` VARCHAR(50) NOT NULL UNIQUE COMMENT '成就代码',
    `achievement_name` VARCHAR(100) NOT NULL COMMENT '成就名称',
    `achievement_description` TEXT DEFAULT NULL COMMENT '成就描述',
    `achievement_icon` VARCHAR(255) DEFAULT NULL COMMENT '成就图标',
    `category` VARCHAR(50) NOT NULL COMMENT '成就分类',
    `type` TINYINT NOT NULL COMMENT '成就类型：1-一次性，2-进阶型，3-日常型',
    `target_value` INT DEFAULT NULL COMMENT '目标值',
    `points_reward` INT DEFAULT 0 COMMENT '积分奖励',
    `exp_reward` INT DEFAULT 0 COMMENT '经验奖励',
    `badge_reward` VARCHAR(255) DEFAULT NULL COMMENT '徽章奖励',
    `is_active` TINYINT DEFAULT 1 COMMENT '是否激活',
    `sort_order` INT DEFAULT 0 COMMENT '排序',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_achievement_code` (`achievement_code`),
    INDEX `idx_category` (`category`),
    INDEX `idx_type` (`type`),
    INDEX `idx_is_active` (`is_active`),
    INDEX `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='社交成就表';

-- 用户成就记录表
CREATE TABLE IF NOT EXISTS `user_achievements` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户成就ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `achievement_id` BIGINT NOT NULL COMMENT '成就ID',
    `current_value` INT DEFAULT 0 COMMENT '当前进度值',
    `target_value` INT NOT NULL COMMENT '目标值',
    `is_completed` TINYINT DEFAULT 0 COMMENT '是否完成',
    `completed_at` DATETIME DEFAULT NULL COMMENT '完成时间',
    `level` INT DEFAULT 1 COMMENT '成就等级（进阶型成就）',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_user_achievement` (`user_id`, `achievement_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_achievement_id` (`achievement_id`),
    INDEX `idx_is_completed` (`is_completed`),
    INDEX `idx_completed_at` (`completed_at`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`achievement_id`) REFERENCES `social_achievements` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户成就记录表';

-- 用户等级经验表
CREATE TABLE IF NOT EXISTS `user_levels` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '等级ID',
    `level` INT NOT NULL UNIQUE COMMENT '等级',
    `level_name` VARCHAR(50) NOT NULL COMMENT '等级名称',
    `min_exp` INT NOT NULL COMMENT '最小经验值',
    `max_exp` INT DEFAULT NULL COMMENT '最大经验值',
    `icon_url` VARCHAR(255) DEFAULT NULL COMMENT '等级图标',
    `privileges` JSON DEFAULT NULL COMMENT '等级特权（JSON格式）',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_level` (`level`),
    INDEX `idx_min_exp` (`min_exp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户等级表';

-- 用户经验记录表
CREATE TABLE IF NOT EXISTS `user_experience` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '经验ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `current_level` INT DEFAULT 1 COMMENT '当前等级',
    `current_exp` INT DEFAULT 0 COMMENT '当前经验值',
    `total_exp` INT DEFAULT 0 COMMENT '累计经验值',
    `last_level_up_time` DATETIME DEFAULT NULL COMMENT '上次升级时间',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_user_id` (`user_id`),
    INDEX `idx_current_level` (`current_level`),
    INDEX `idx_current_exp` (`current_exp`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户经验记录表';

-- 经验获得记录表
CREATE TABLE IF NOT EXISTS `experience_logs` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '经验日志ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `action_type` VARCHAR(50) NOT NULL COMMENT '行为类型',
    `exp_gained` INT NOT NULL COMMENT '获得经验',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '描述',
    `related_id` BIGINT DEFAULT NULL COMMENT '关联ID',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_action_type` (`action_type`),
    INDEX `idx_created_at` (`created_at`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='经验获得记录表';

-- 消息加密记录表
CREATE TABLE IF NOT EXISTS `message_encryption` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '加密ID',
    `message_id` VARCHAR(64) NOT NULL COMMENT '消息ID',
    `encryption_key_id` VARCHAR(64) NOT NULL COMMENT '加密密钥ID',
    `algorithm` VARCHAR(20) DEFAULT 'AES-256' COMMENT '加密算法',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_message_id` (`message_id`),
    INDEX `idx_encryption_key_id` (`encryption_key_id`),
    INDEX `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息加密记录表';

-- 用户密钥表
CREATE TABLE IF NOT EXISTS `user_keys` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '密钥ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `key_id` VARCHAR(64) NOT NULL UNIQUE COMMENT '密钥标识',
    `public_key` TEXT NOT NULL COMMENT '公钥',
    `private_key_encrypted` TEXT NOT NULL COMMENT '加密私钥',
    `key_type` VARCHAR(20) DEFAULT 'RSA-2048' COMMENT '密钥类型',
    `is_active` TINYINT DEFAULT 1 COMMENT '是否激活',
    `expires_at` DATETIME DEFAULT NULL COMMENT '过期时间',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_key_id` (`key_id`),
    INDEX `idx_is_active` (`is_active`),
    INDEX `idx_expires_at` (`expires_at`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户密钥表';

-- 隐私设置表
CREATE TABLE IF NOT EXISTS `privacy_settings` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '隐私设置ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `setting_key` VARCHAR(50) NOT NULL COMMENT '设置键',
    `setting_value` VARCHAR(20) NOT NULL COMMENT '设置值',
    `description` VARCHAR(255) DEFAULT NULL COMMENT '设置描述',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY `uk_user_setting` (`user_id`, `setting_key`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_setting_key` (`setting_key`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='隐私设置表';

-- 骚扰报告表
CREATE TABLE IF NOT EXISTS `harassment_reports` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '报告ID',
    `reporter_id` BIGINT NOT NULL COMMENT '举报人 ID',
    `reported_user_id` BIGINT NOT NULL COMMENT '被举报用户ID',
    `report_type` TINYINT NOT NULL COMMENT '报告类型：1-骚扰消息，2-恶意添加，3-垃圾信息，4-其他',
    `content_type` TINYINT DEFAULT NULL COMMENT '内容类型：1-消息，2-朋友圈，3-贴吧帖子',
    `content_id` BIGINT DEFAULT NULL COMMENT '内容ID',
    `description` TEXT DEFAULT NULL COMMENT '报告描述',
    `evidence_urls` JSON DEFAULT NULL COMMENT '证据截图（JSON数组）',
    `status` TINYINT DEFAULT 0 COMMENT '处理状态：0-待处理，1-处理中，2-已处理，3-驳回',
    `admin_id` BIGINT DEFAULT NULL COMMENT '处理管理员ID',
    `admin_note` TEXT DEFAULT NULL COMMENT '管理员备注',
    `processed_at` DATETIME DEFAULT NULL COMMENT '处理时间',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_reporter_id` (`reporter_id`),
    INDEX `idx_reported_user_id` (`reported_user_id`),
    INDEX `idx_report_type` (`report_type`),
    INDEX `idx_status` (`status`),
    INDEX `idx_created_at` (`created_at`),
    FOREIGN KEY (`reporter_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`reported_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='骚扰报告表';

-- 用户黑名单表
CREATE TABLE IF NOT EXISTS `user_blacklist` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '黑名单ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `blocked_user_id` BIGINT NOT NULL COMMENT '被拉黑用户ID',
    `reason` VARCHAR(255) DEFAULT NULL COMMENT '拉黑原因',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY `uk_user_blocked` (`user_id`, `blocked_user_id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_blocked_user_id` (`blocked_user_id`),
    INDEX `idx_created_at` (`created_at`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`blocked_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户黑名单表';

-- 安全日志表
CREATE TABLE IF NOT EXISTS `security_logs` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    `user_id` BIGINT DEFAULT NULL COMMENT '用户ID',
    `action_type` VARCHAR(50) NOT NULL COMMENT '操作类型',
    `ip_address` VARCHAR(45) DEFAULT NULL COMMENT 'IP地址',
    `user_agent` TEXT DEFAULT NULL COMMENT '用户代理',
    `location` VARCHAR(255) DEFAULT NULL COMMENT '地理位置',
    `device_info` JSON DEFAULT NULL COMMENT '设备信息',
    `result` TINYINT NOT NULL COMMENT '结果：0-失败，1-成功',
    `risk_level` TINYINT DEFAULT 0 COMMENT '风险等级：0-低，1-中，2-高',
    `description` TEXT DEFAULT NULL COMMENT '描述',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_action_type` (`action_type`),
    INDEX `idx_ip_address` (`ip_address`),
    INDEX `idx_result` (`result`),
    INDEX `idx_risk_level` (`risk_level`),
    INDEX `idx_created_at` (`created_at`),
    FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='安全日志表';

-- 插入初始等级数据
INSERT INTO `user_levels` (`level`, `level_name`, `min_exp`, `max_exp`, `icon_url`, `privileges`) VALUES
(1, '新手', 0, 99, '/icons/level1.png', '{"daily_checkin_bonus": 1.0, "message_limit": 100}'),
(2, '初级', 100, 299, '/icons/level2.png', '{"daily_checkin_bonus": 1.2, "message_limit": 200}'),
(3, '中级', 300, 699, '/icons/level3.png', '{"daily_checkin_bonus": 1.5, "message_limit": 500}'),
(4, '高级', 700, 1499, '/icons/level4.png', '{"daily_checkin_bonus": 2.0, "message_limit": 1000}'),
(5, '专家', 1500, 2999, '/icons/level5.png', '{"daily_checkin_bonus": 2.5, "message_limit": 2000}'),
(6, '大师', 3000, 5999, '/icons/level6.png', '{"daily_checkin_bonus": 3.0, "message_limit": 5000}'),
(7, '宗师', 6000, 11999, '/icons/level7.png', '{"daily_checkin_bonus": 4.0, "message_limit": 10000}'),
(8, '传说', 12000, NULL, '/icons/level8.png', '{"daily_checkin_bonus": 5.0, "message_limit": -1}');

-- 插入初始成就数据
INSERT INTO `social_achievements` (`achievement_code`, `achievement_name`, `achievement_description`, `achievement_icon`, `category`, `type`, `target_value`, `points_reward`, `exp_reward`, `badge_reward`, `is_active`, `sort_order`) VALUES
-- 登录成就
('LOGIN_FIRST', '初次登录', '欢迎来到我们的平台！', '/icons/first_login.png', '登录', 1, 1, 50, 10, 'welcome_badge', 1, 1),
('LOGIN_CONTINUOUS_7', '连续登录7天', '连续登录7天，坚持就是胜利！', '/icons/login_7.png', '登录', 1, 7, 100, 50, 'persistence_badge', 1, 2),
('LOGIN_CONTINUOUS_30', '连续登录30天', '月度登录达人！', '/icons/login_30.png', '登录', 1, 30, 500, 200, 'monthly_badge', 1, 3),

-- 社交成就
('FRIENDS_FIRST', '第一个好友', '添加了第一个好友，社交之旅开始！', '/icons/first_friend.png', '社交', 1, 1, 30, 20, 'social_badge', 1, 10),
('FRIENDS_10', '好友满十', '好友数量达刄10人', '/icons/friends_10.png', '社交', 1, 10, 100, 50, 'popular_badge', 1, 11),
('FRIENDS_50', '社交达人', '好友数量达刄50人', '/icons/friends_50.png', '社交', 1, 50, 300, 150, 'social_master_badge', 1, 12),

-- 消息成就
('MESSAGE_FIRST', '第一条消息', '发送了第一条消息', '/icons/first_message.png', '消息', 1, 1, 20, 10, 'messenger_badge', 1, 20),
('MESSAGE_100', '消息小能手', '发送了100条消息', '/icons/message_100.png', '消息', 1, 100, 50, 30, 'active_badge', 1, 21),
('MESSAGE_1000', '消息大使', '发送了1000条消息', '/icons/message_1000.png', '消息', 1, 1000, 200, 100, 'communication_badge', 1, 22),

-- 签到成就
('CHECKIN_FIRST', '首次签到', '完成了第一次签到', '/icons/first_checkin.png', '签到', 1, 1, 20, 10, 'routine_badge', 1, 30),
('CHECKIN_CONTINUOUS_7', '七日签到', '连续签到7天', '/icons/checkin_7.png', '签到', 1, 7, 100, 50, 'dedication_badge', 1, 31),
('CHECKIN_CONTINUOUS_30', '月度签到', '连续签到30天', '/icons/checkin_30.png', '签到', 1, 30, 500, 200, 'commitment_badge', 1, 32),

-- 内容成就
('MOMENT_FIRST', '首次动态', '发布了第一条朋友圈动态', '/icons/first_moment.png', '内容', 1, 1, 30, 20, 'creator_badge', 1, 40),
('MOMENT_LIKED_100', '受欢迎者', '朋友圈获得100个赞', '/icons/liked_100.png', '内容', 1, 100, 200, 100, 'popular_creator_badge', 1, 41),
('FORUM_POST_10', '贴吧活跃者', '发布了10个贴吧帖子', '/icons/forum_active.png', '内容', 1, 10, 150, 80, 'forum_badge', 1, 42);

-- 插入默认隐私设置
INSERT INTO `privacy_settings` (`user_id`, `setting_key`, `setting_value`, `description`) 
SELECT u.id, 'PROFILE_VISIBILITY', 'FRIENDS', '资料可见性' FROM `users` u WHERE NOT EXISTS (SELECT 1 FROM `privacy_settings` ps WHERE ps.user_id = u.id AND ps.setting_key = 'PROFILE_VISIBILITY');

INSERT INTO `privacy_settings` (`user_id`, `setting_key`, `setting_value`, `description`) 
SELECT u.id, 'LOCATION_SHARING', 'FRIENDS', '位置共享' FROM `users` u WHERE NOT EXISTS (SELECT 1 FROM `privacy_settings` ps WHERE ps.user_id = u.id AND ps.setting_key = 'LOCATION_SHARING');

INSERT INTO `privacy_settings` (`user_id`, `setting_key`, `setting_value`, `description`) 
SELECT u.id, 'ONLINE_STATUS', 'FRIENDS', '在线状态显示' FROM `users` u WHERE NOT EXISTS (SELECT 1 FROM `privacy_settings` ps WHERE ps.user_id = u.id AND ps.setting_key = 'ONLINE_STATUS');

INSERT INTO `privacy_settings` (`user_id`, `setting_key`, `setting_value`, `description`) 
SELECT u.id, 'FRIEND_REQUEST', 'EVERYONE', '好友请求接收' FROM `users` u WHERE NOT EXISTS (SELECT 1 FROM `privacy_settings` ps WHERE ps.user_id = u.id AND ps.setting_key = 'FRIEND_REQUEST');

INSERT INTO `privacy_settings` (`user_id`, `setting_key`, `setting_value`, `description`) 
SELECT u.id, 'MESSAGE_FROM_STRANGER', 'DISABLED', '陌生人消息' FROM `users` u WHERE NOT EXISTS (SELECT 1 FROM `privacy_settings` ps WHERE ps.user_id = u.id AND ps.setting_key = 'MESSAGE_FROM_STRANGER');

-- 为所有现有用户初始化经验和成就
INSERT INTO `user_experience` (`user_id`, `current_level`, `current_exp`, `total_exp`) 
SELECT u.id, 1, 0, 0 FROM `users` u WHERE NOT EXISTS (SELECT 1 FROM `user_experience` ue WHERE ue.user_id = u.id);

INSERT INTO `user_achievements` (`user_id`, `achievement_id`, `current_value`, `target_value`, `is_completed`) 
SELECT u.id, sa.id, 0, sa.target_value, 0 
FROM `users` u 
CROSS JOIN `social_achievements` sa 
WHERE sa.is_active = 1 
AND NOT EXISTS (SELECT 1 FROM `user_achievements` ua WHERE ua.user_id = u.id AND ua.achievement_id = sa.id);