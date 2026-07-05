package com.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    private static final String TOKEN_BLACKLIST = "token:blacklist:";
    private static final String RATE_LIMIT = "rate:limit:";
    private static final String JOB_SEARCH_CACHE = "job:search:";
    private static final long CACHE_EXPIRE_MINUTES = 5;

    // ==================== Token 黑名单 ====================

    public void blacklistToken(String token, long expirationMillis) {
        redisTemplate.opsForValue().set(
                TOKEN_BLACKLIST + token, "1", expirationMillis, TimeUnit.MILLISECONDS);
    }

    public boolean isTokenBlacklisted(String token) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(TOKEN_BLACKLIST + token));
    }

    // ==================== 接口限流 ====================

    /**
     * 滑动窗口限流：指定时间窗口内最大请求数
     * @return true=允许通过, false=被限流
     */
    public boolean isAllowed(String key, int maxRequests, int windowSeconds) {
        String redisKey = RATE_LIMIT + key;
        Long count = redisTemplate.opsForValue().increment(redisKey);
        if (count != null && count == 1) {
            redisTemplate.expire(redisKey, windowSeconds, TimeUnit.SECONDS);
        }
        return count != null && count <= maxRequests;
    }

    // ==================== 搜索缓存 ====================

    /**
     * 获取缓存字符串
     */
    public String getString(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置缓存字符串（带随机过期时间，防止缓存雪崩）
     */
    public void setString(String key, String value) {
        // 基础过期时间 + 随机0-60秒，防止同时过期
        long expireSeconds = CACHE_EXPIRE_MINUTES * 60 + (long) (Math.random() * 60);
        redisTemplate.opsForValue().set(key, value, expireSeconds, TimeUnit.SECONDS);
    }

    /**
     * 清除岗位相关缓存（岗位增删改时调用）
     */
    public void clearJobCache() {
        var keys = redisTemplate.keys(JOB_SEARCH_CACHE + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 生成搜索缓存 key
     */
    public String buildJobSearchKey(String keyword, String city, String category, String salaryType, int pageNum, int pageSize) {
        return JOB_SEARCH_CACHE + (keyword == null ? "" : keyword)
                + ":" + (city == null ? "" : city)
                + ":" + (category == null ? "" : category)
                + ":" + (salaryType == null ? "" : salaryType)
                + ":" + pageNum + ":" + pageSize;
    }

    // ==================== 浏览量统计 ====================

    private static final String JOB_VIEW_COUNT = "job:view:";
    private static final String JOB_HOT_RANK = "job:hot:rank";

    /**
     * 增加岗位浏览量
     */
    public void incrementViewCount(Long jobId) {
        redisTemplate.opsForValue().increment(JOB_VIEW_COUNT + jobId);
        // 同时更新排行榜分数（浏览量作为分数）
        redisTemplate.opsForZSet().incrementScore(JOB_HOT_RANK, String.valueOf(jobId), 1);
    }

    /**
     * 获取岗位浏览量
     */
    public long getViewCount(Long jobId) {
        String val = redisTemplate.opsForValue().get(JOB_VIEW_COUNT + jobId);
        return val != null ? Long.parseLong(val) : 0;
    }

    /**
     * 获取热门岗位排行榜（TOP N）
     */
    public java.util.List<String> getHotJobs(int top) {
        var set = redisTemplate.opsForZSet().reverseRange(JOB_HOT_RANK, 0, top - 1);
        return set != null ? new java.util.ArrayList<>(set) : new java.util.ArrayList<>();
    }

    /**
     * 增加投递数（用于热门排行）
     */
    public void incrementApplyCount(Long jobId) {
        redisTemplate.opsForZSet().incrementScore(JOB_HOT_RANK, String.valueOf(jobId), 3);
    }

    // ==================== 在线用户统计（心跳机制） ====================

    private static final String ONLINE_USERS = "online:users";

    /**
     * 用户上线/心跳：记录当前时间戳
     */
    public void userOnline(Long userId) {
        redisTemplate.opsForHash().put(ONLINE_USERS, String.valueOf(userId), String.valueOf(System.currentTimeMillis()));
    }

    /**
     * 用户下线：移除记录
     */
    public void userOffline(Long userId) {
        redisTemplate.opsForHash().delete(ONLINE_USERS, String.valueOf(userId));
    }

    /**
     * 获取在线用户数量（只统计心跳未过期的）
     */
    public Long getOnlineUserCount() {
        long threshold = System.currentTimeMillis() - 120_000; // 2分钟没心跳视为离线
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(ONLINE_USERS);
        return entries.values().stream()
                .filter(v -> Long.parseLong(v.toString()) > threshold)
                .count();
    }

    /**
     * 清理超时用户（超过2分钟没心跳）
     */
    public int cleanStaleUsers() {
        long threshold = System.currentTimeMillis() - 120_000;
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(ONLINE_USERS);
        int removed = 0;
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            if (Long.parseLong(entry.getValue().toString()) <= threshold) {
                redisTemplate.opsForHash().delete(ONLINE_USERS, entry.getKey());
                removed++;
            }
        }
        return removed;
    }
}
