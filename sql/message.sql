-- 消息通知表
CREATE TABLE IF NOT EXISTS `message` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id` BIGINT NOT NULL COMMENT '接收者ID',
    `type` VARCHAR(50) NOT NULL COMMENT '消息类型：APPLICATION/INTERVIEW/ACCEPTED/REJECTED/SYSTEM',
    `title` VARCHAR(200) NOT NULL COMMENT '消息标题',
    `content` VARCHAR(500) DEFAULT NULL COMMENT '消息内容',
    `related_id` BIGINT DEFAULT NULL COMMENT '关联ID（投递ID/面试ID等）',
    `is_read` TINYINT NOT NULL DEFAULT 0 COMMENT '是否已读：0=未读 1=已读',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_is_read` (`is_read`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息通知表';
