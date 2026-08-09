package com.example.demo.task;

import com.example.demo.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OnlineCleanupTask {

    private final RedisService redisService;

    /**
     * 每2分钟清理一次超时的在线用户
     */
    @Scheduled(fixedRate = 120_000)
    public void cleanStaleUsers() {
        int removed = redisService.cleanStaleUsers();
        if (removed > 0) {
            log.info("清理超时在线用户 {} 个", removed);
        }
    }
}
