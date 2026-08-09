# PartTimeGo Docker 部署指南

## 前置条件

1. 安装 Docker Desktop：https://www.docker.com/products/docker-desktop/
2. 安装 Maven（用于打包后端）

## 快速部署

### Windows

```bash
# 双击运行 deploy.bat
# 或在命令行执行
deploy.bat
```

### Linux/Mac

```bash
# 添加执行权限
chmod +x deploy.sh

# 执行部署
./deploy.sh
```

## 手动部署步骤

### 1. 打包后端

```bash
cd PartTimeGo
mvn clean package -DskipTests
```

### 2. 构建并启动

```bash
docker-compose up -d --build
```

### 3. 查看状态

```bash
docker-compose ps
```

### 4. 查看日志

```bash
# 查看所有日志
docker-compose logs -f

# 查看后端日志
docker-compose logs -f backend

# 查看前端日志
docker-compose logs -f frontend
```

## 访问地址

- 前端页面：http://localhost
- 后端接口：http://localhost:9090
- MySQL：localhost:3306
- Redis：localhost:6379

## 常用命令

```bash
# 启动服务
docker-compose up -d

# 停止服务
docker-compose down

# 重启服务
docker-compose restart

# 查看容器状态
docker-compose ps

# 进入后端容器
docker exec -it parttimego-backend /bin/bash

# 进入 MySQL
docker exec -it parttimego-mysql mysql -uroot -p1234

# 进入 Redis
docker exec -it parttimego-redis redis-cli
```

## 数据持久化

MySQL 和 Redis 数据存储在 Docker Volume 中，即使容器删除数据也不会丢失。

```bash
# 查看 Volume
docker volume ls

# 备份 MySQL 数据
docker exec parttimego-mysql mysqldump -uroot -p1234 parttimego > backup.sql
```

## 常见问题

### 1. 端口被占用

修改 docker-compose.yml 中的端口映射：

```yaml
ports:
  - "8080:80"    # 前端改为 8080
  - "9091:9090"  # 后端改为 9091
```

### 2. MySQL 连接失败

等待 MySQL 完全启动，或检查日志：

```bash
docker-compose logs mysql
```

### 3. 构建失败

清理 Docker 缓存重新构建：

```bash
docker-compose down
docker system prune -a
docker-compose up -d --build
```

## 部署到云服务器

1. 购买云服务器（阿里云/腾讯云）
2. 安装 Docker 和 Docker Compose
3. 上传项目文件
4. 执行 `deploy.sh`
5. 开放安全组端口（80、9090、3306、6379）

## 简历写法

```
项目经历
PartTimeGo 大学生兼职招聘系统
项目地址：http://你的服务器IP
GitHub：https://github.com/你的用户名/parttimego
技术栈：Spring Boot + MyBatis-Plus + Redis + RabbitMQ + Vue3 + Docker
部署方式：Docker Compose 容器化部署
```
