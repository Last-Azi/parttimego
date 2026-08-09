# 性能优化方案

## 一、数据库优化

### 1.1 索引优化

| 表 | 索引 | 用途 |
|---|------|------|
| job | idx_status, idx_city, idx_category | 岗位搜索筛选 |
| application | uk_job_user | 防止重复投递 |
| message | idx_user_read, idx_user_time | 消息查询优化 |
| review | idx_job_id, idx_to_user_id | 评价查询优化 |

### 1.2 连接池配置 (HikariCP)

```properties
spring.datasource.hikari.maximum-pool-size=20    # 最大连接数
spring.datasource.hikari.minimum-idle=5           # 最小空闲连接
spring.datasource.hikari.idle-timeout=30000       # 空闲超时30秒
spring.datasource.hikari.max-lifetime=1800000     # 连接最大生命周期30分钟
```

## 二、缓存优化

### 2.1 Redis 缓存策略

| 缓存类型 | 过期时间 | 用途 |
|---------|---------|------|
| 搜索结果缓存 | 5分钟 | 减少重复查询 |
| Token 黑名单 | 24小时 | 登出失效 |
| 浏览量统计 | 永不过期 | 岗位热度 |
| 排行榜 | 永不过期 | 热门岗位 |

### 2.2 缓存命中率

- 搜索接口：首次查询数据库，后续从 Redis 返回
- 岗位详情：每次查看增加浏览量

## 三、接口优化

### 3.1 接口限流

- 每个用户/IP 每分钟最多 60 次请求
- 防止恶意刷接口

### 3.2 分页查询

- 所有列表接口支持分页
- 默认每页 10 条，避免一次返回过多数据

## 四、JVM 优化

### 4.1 启动参数

```bash
java -Xms512m -Xmx1024m -jar app.jar
```

### 4.2 监控接口

访问 `/performance/metrics` 查看：
- JVM 内存使用情况
- 线程数量
- 系统运行时间

## 五、性能指标

| 指标 | 目标值 | 当前值 |
|------|-------|-------|
| 接口响应时间 | < 200ms | 待测试 |
| 数据库查询时间 | < 50ms | 待测试 |
| 缓存命中率 | > 80% | 待测试 |
| 并发用户数 | > 100 | 待测试 |
