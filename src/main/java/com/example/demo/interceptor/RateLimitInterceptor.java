package com.example.demo.interceptor;

import com.example.demo.service.RedisService;
import com.example.demo.util.UserContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private final RedisService redisService;

    /** 每个用户/IP 每分钟最多 180 次请求 */
    private static final int MAX_REQUESTS = 180;
    private static final int WINDOW_SECONDS = 60;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String key = resolveKey(request);
        if (!redisService.isAllowed(key, MAX_REQUESTS, WINDOW_SECONDS)) {
            response.setStatus(429);
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"code\":429,\"msg\":\"请求过于频繁，请稍后再试\"}");
            return false;
        }
        return true;
    }

    private String resolveKey(HttpServletRequest request) {
        // 已登录用户用 userId，未登录用 IP
        Long userId = UserContext.getUserId();
        if (userId != null) {
            return "uid:" + userId;
        }
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return "ip:" + ip;
    }
}
