# PartTimeGo - 大学生兼职招聘系统

一个面向大学生的兼职招聘平台，支持学生投递简历、企业发布岗位、实时聊天、面试管理等完整招聘流程。

## 技术栈

**后端：**
- Spring Boot 3.4.1 + MyBatis-Plus 3.5.9
- MySQL 8.0 + Redis 7 + RabbitMQ 3
- JWT 认证 + WebSocket 实时通信
- 阿里云 OSS 文件存储

**前端：**
- Vue 3.5 + Vite 6.0
- Element Plus 2.9
- Pinia 状态管理
- STOMP.js 即时聊天

**部署：**
- Docker Compose 容器化部署
- Nginx 反向代理

## 功能特性

### 学生端
- 注册/登录，JWT 令牌认证
- 创建和管理个人简历
- 浏览、搜索、收藏岗位
- 投递简历，查看投递状态
- 接收/拒绝面试邀请
- 与企业实时聊天

### 企业端
- 发布、编辑、下架岗位
- 查看投递列表，筛选候选人
- 发送面试邀请，管理面试流程
- 与候选人实时聊天

### 管理员端
- 数据统计仪表盘
- 岗位审核
- 用户管理（禁用/启用）

### 通用功能
- 头像上传（支持裁切）
- 文件上传（简历附件、聊天图片/文件）
- 实时在线人数统计
- 搜索热词推荐
- 请求限流保护（滑动窗口）
- 操作日志记录（AOP）

## 快速开始

### 环境要求

| 工具 | 版本 | 说明 |
|------|------|------|
| Docker Desktop | 最新版 | [下载地址](https://www.docker.com/products/docker-desktop/) |
| JDK | 17+ | [下载地址](https://adoptium.net/) |
| Maven | 3.6+ | [下载地址](https://maven.apache.org/) |

### 运行步骤

```bash
# 1. 克隆仓库
git clone https://github.com/Last-Azi/parttimego.git
cd parttimego

# 2. 构建后端 JAR
mvn package -DskipTests

# 3. 启动所有服务（MySQL + Redis + RabbitMQ + 后端 + 前端）
docker compose up -d
```

等待所有容器启动完成后，访问 **http://localhost** 即可使用。

### 默认账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | 123456 |
| 学生 | test | 123456 |
| 企业 | company | 123456 |

> 以上为示例账号，实际账号请根据数据库中的数据登录。

### 文件上传配置（可选）

如需使用头像上传、文件上传功能，需要配置阿里云 OSS：

```bash
# 创建 .env 文件（不会提交到 GitHub）
echo "OSS_ACCESS_KEY_ID=你的AccessKeyId" > .env
echo "OSS_ACCESS_KEY_SECRET=你的AccessKeySecret" >> .env

# 带环境变量启动
docker compose --env-file .env up -d
```

## 项目结构

```
PartTimeGo/
├── src/main/java/com/example/demo/
│   ├── config/          # 配置类（OSS、WebSocket、MyBatis等）
│   ├── controller/      # 控制器层
│   ├── service/         # 业务逻辑层
│   ├── mapper/          # 数据访问层
│   ├── entity/          # 实体类
│   ├── dto/             # 数据传输对象
│   ├── enums/           # 枚举类
│   ├── interceptor/     # 拦截器（JWT、限流）
│   ├── annotation/      # 自定义注解
│   ├── aspect/          # AOP 切面
│   ├── util/            # 工具类
│   └── common/          # 公共类
├── frontend/
│   ├── src/
│   │   ├── views/       # 页面组件
│   │   ├── api/         # API 接口
│   │   ├── stores/      # Pinia 状态
│   │   ├── router/      # 路由配置
│   │   └── components/  # 公共组件
│   └── nginx.conf       # Nginx 配置
├── sql/
│   └── init.sql         # 数据库初始化脚本
├── docker-compose.yml   # Docker 编排配置
├── Dockerfile           # 后端镜像构建
└── pom.xml              # Maven 依赖配置
```

## 端口说明

| 服务 | 端口 | 说明 |
|------|------|------|
| 前端 | 80 | Nginx 静态文件服务 |
| 后端 | 9090 | Spring Boot API |
| MySQL | 3307 | 数据库 |
| Redis | 6380 | 缓存 |
| RabbitMQ | 5673 | 消息队列 |
| RabbitMQ管理 | 15673 | RabbitMQ 管理界面 |

## 核心设计

- **AOP 操作日志**：通过 `@OperLog` 注解自动记录关键操作
- **Redis 多场景应用**：Token 黑名单、搜索缓存、热门岗位排行、在线用户统计、滑动窗口限流
- **WebSocket 实时通信**：基于 STOMP 协议的即时聊天
- **心跳机制**：前端定时上报，后端定时清理超时用户
- **JWT 认证**：无状态令牌 + Redis 黑名单双重校验

## License

MIT
