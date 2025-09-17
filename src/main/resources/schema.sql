-- 即时通讯系统数据库设计
-- 创建数据库
CREATE DATABASE IF NOT EXISTS netty_im DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE netty_im;

-- 用户表
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    `email` VARCHAR(100) NOT NULL UNIQUE COMMENT '邮箱',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    `nickname` VARCHAR(50) NOT NULL COMMENT '昵称',
    `avatar` VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `status` TINYINT DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `online_status` TINYINT DEFAULT 0 COMMENT '在线状态：0-离线，1-在线，2-忙碌，3-离开',
    `last_login_time` DATETIME DEFAULT NULL COMMENT '最后登录时间',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX `idx_username` (`username`),
    INDEX `idx_email` (`email`),
    INDEX `idx_status` (`status`)
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

-- 插入测试数据
INSERT INTO `users` (`username`, `email`, `password`, `nickname`, `status`) VALUES
('admin', 'admin@example.com', '$2a$10$k9x8gNKvPEEwwsGhKCfNVOwP5WRE.QZuuNBXYkOHb1lHk8OV8qL3G', '管理员', 1),
('user1', 'user1@example.com', '$2a$10$k9x8gNKvPEEwwsGhKCfNVOwP5WRE.QZuuNBXYkOHb1lHk8OV8qL3G', '用户1', 1),
('user2', 'user2@example.com', '$2a$10$k9x8gNKvPEEwwsGhKCfNVOwP5WRE.QZuuNBXYkOHb1lHk8OV8qL3G', '用户2', 1);