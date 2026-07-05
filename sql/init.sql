-- 创建数据库
CREATE DATABASE IF NOT EXISTS parttimego DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE parttimego;

-- ==================== 用户表 ====================
CREATE TABLE IF NOT EXISTS `user` (
    `id`          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`    VARCHAR(50)  NOT NULL COMMENT '用户名',
    `password`    VARCHAR(255) NOT NULL COMMENT '密码（BCrypt）',
    `role`        VARCHAR(20)  NOT NULL COMMENT '角色：STUDENT/EMPLOYER/ADMIN',
    `nickname`    VARCHAR(50)  DEFAULT NULL COMMENT '昵称',
    `avatar`      VARCHAR(255) DEFAULT NULL COMMENT '头像URL',
    `phone`       VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    `email`       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `status`      TINYINT      NOT NULL DEFAULT 1 COMMENT '状态：0=禁用 1=正常',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=未删除 1=已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 插入默认管理员（密码: admin123，MD5( PartTimeGoadmin123 )）
INSERT INTO `user` (`username`, `password`, `role`, `nickname`) VALUES
('admin', 'cb9ae219cbec2a60d60f1291c5067c05', 'ADMIN', 'Admin');

-- ==================== 兼职岗位表 ====================
CREATE TABLE IF NOT EXISTS `job` (
    `id`          BIGINT        NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`     BIGINT        NOT NULL COMMENT '发布者用户ID',
    `title`       VARCHAR(100)  NOT NULL COMMENT '岗位标题',
    `description` TEXT          DEFAULT NULL COMMENT '岗位描述',
    `salary_min`  DECIMAL(10,2) DEFAULT NULL COMMENT '最低薪资',
    `salary_max`  DECIMAL(10,2) DEFAULT NULL COMMENT '最高薪资',
    `salary_type` VARCHAR(20)   NOT NULL DEFAULT '日结' COMMENT '结算方式：日结/周结/月结/完工结',
    `city`        VARCHAR(50)   DEFAULT NULL COMMENT '工作城市',
    `address`     VARCHAR(255)  DEFAULT NULL COMMENT '工作地址',
    `category`    VARCHAR(50)   DEFAULT NULL COMMENT '岗位分类',
    `work_time`   VARCHAR(100)  DEFAULT NULL COMMENT '工作时间说明',
    `headcount`   INT           DEFAULT 1 COMMENT '招聘人数',
    `status`      TINYINT       NOT NULL DEFAULT 0 COMMENT '状态：0=待审核 1=已发布 2=已下架 3=审核拒绝',
    `reject_reason` VARCHAR(255) DEFAULT NULL COMMENT '拒绝原因',
    `create_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT       NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=未删除 1=已删除',
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`),
    KEY `idx_status` (`status`),
    KEY `idx_city` (`city`),
    KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='兼职岗位表';

-- ==================== 简历表 ====================
CREATE TABLE IF NOT EXISTS `resume` (
    `id`            BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`       BIGINT       NOT NULL COMMENT '所属学生用户ID',
    `real_name`     VARCHAR(50)  DEFAULT NULL COMMENT '真实姓名',
    `gender`        VARCHAR(10)  DEFAULT NULL COMMENT '性别',
    `school`        VARCHAR(100) DEFAULT NULL COMMENT '学校',
    `major`         VARCHAR(100) DEFAULT NULL COMMENT '专业',
    `grade`         VARCHAR(20)  DEFAULT NULL COMMENT '年级',
    `phone`         VARCHAR(20)  DEFAULT NULL COMMENT '联系电话',
    `email`         VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `skills`        VARCHAR(500) DEFAULT NULL COMMENT '技能标签（逗号分隔）',
    `experience`    TEXT         DEFAULT NULL COMMENT '兼职/实践经历',
    `self_intro`    TEXT         DEFAULT NULL COMMENT '自我介绍',
    `expect_city`   VARCHAR(50)  DEFAULT NULL COMMENT '期望工作城市',
    `expect_salary` VARCHAR(50)  DEFAULT NULL COMMENT '期望薪资',
    `create_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`       TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=未删除 1=已删除',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='简历表';

-- ==================== 投递记录表 ====================
CREATE TABLE IF NOT EXISTS `application` (
    `id`          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `job_id`      BIGINT   NOT NULL COMMENT '岗位ID',
    `user_id`     BIGINT   NOT NULL COMMENT '投递学生用户ID',
    `resume_id`   BIGINT   DEFAULT NULL COMMENT '投递时使用的简历ID',
    `status`      TINYINT  NOT NULL DEFAULT 0 COMMENT '状态：0=待处理 1=已查看 2=邀请面试 3=已录用 4=已拒绝',
    `remark`      VARCHAR(500) DEFAULT NULL COMMENT '备注/拒绝原因',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '投递时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `deleted`     TINYINT  NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=未删除 1=已删除',
    PRIMARY KEY (`id`),
    KEY `idx_job_id` (`job_id`),
    KEY `idx_user_id` (`user_id`),
    UNIQUE KEY `uk_job_user` (`job_id`, `user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投递记录表';

-- ==================== 收藏表 ====================
CREATE TABLE IF NOT EXISTS `favorite` (
    `id`          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '主键',
    `user_id`     BIGINT   NOT NULL COMMENT '用户ID',
    `job_id`      BIGINT   NOT NULL COMMENT '岗位ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_job` (`user_id`, `job_id`),
    KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- ==================== 评价表 ====================
CREATE TABLE IF NOT EXISTS `review` (
    `id`              BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `application_id`  BIGINT       NOT NULL COMMENT '投递记录ID',
    `from_user_id`    BIGINT       NOT NULL COMMENT '评价人ID',
    `to_user_id`      BIGINT       NOT NULL COMMENT '被评价人ID',
    `job_id`          BIGINT       NOT NULL COMMENT '关联岗位ID',
    `rating`          TINYINT      NOT NULL DEFAULT 5 COMMENT '评分：1-5',
    `content`         VARCHAR(500) DEFAULT NULL COMMENT '评价内容',
    `create_time`     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评价时间',
    `deleted`         TINYINT      NOT NULL DEFAULT 0 COMMENT '逻辑删除：0=未删除 1=已删除',
    PRIMARY KEY (`id`),
    KEY `idx_application_id` (`application_id`),
    KEY `idx_to_user_id` (`to_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评价表';

-- 文件记录表
CREATE TABLE IF NOT EXISTS `file_record` (
                                             `id` BIGINT NOT NULL AUTO_INCREMENT,
                                             `user_id` BIGINT NOT NULL,
                                             `file_name` VARCHAR(255) NOT NULL,
                                             `file_url` VARCHAR(500) NOT NULL,
                                             `file_type` VARCHAR(20) NOT NULL,
                                             `file_size` BIGINT DEFAULT NULL,
                                             `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                             `deleted` TINYINT NOT NULL DEFAULT 0,
                                             PRIMARY KEY (`id`),
                                             KEY `idx_user_id` (`user_id`),
                                             KEY `idx_file_type` (`file_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 面试邀请表
CREATE TABLE IF NOT EXISTS `interview` (
                                           `id` BIGINT NOT NULL AUTO_INCREMENT,
                                           `application_id` BIGINT NOT NULL,
                                           `job_id` BIGINT NOT NULL,
                                           `employer_id` BIGINT NOT NULL,
                                           `student_id` BIGINT NOT NULL,
                                           `interview_time` DATETIME NOT NULL,
                                           `interview_place` VARCHAR(255) DEFAULT NULL,
                                           `interview_type` VARCHAR(20) NOT NULL DEFAULT '线下',
                                           `interview_content` VARCHAR(500) DEFAULT NULL,
                                           `status` TINYINT NOT NULL DEFAULT 0,
                                           `student_remark` VARCHAR(500) DEFAULT NULL,
                                           `employer_remark` VARCHAR(500) DEFAULT NULL,
                                           `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                           `deleted` TINYINT NOT NULL DEFAULT 0,
                                           PRIMARY KEY (`id`),
                                           KEY `idx_application_id` (`application_id`),
                                           KEY `idx_employer_id` (`employer_id`),
                                           KEY `idx_student_id` (`student_id`),
                                           KEY `idx_job_id` (`job_id`),
                                           KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 给resume表添加附件字段
ALTER TABLE `resume` ADD COLUMN `attachment_url` VARCHAR(500) DEFAULT NULL COMMENT '简历附件URL' AFTER `expect_salary`;
