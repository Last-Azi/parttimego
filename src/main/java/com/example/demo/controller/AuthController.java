package com.example.demo.controller;

import com.example.demo.annotation.OperLog;
import com.example.demo.common.R;
import com.example.demo.dto.LoginDTO;
import com.example.demo.dto.RegisterDTO;
import com.example.demo.dto.UserVO;
import com.example.demo.service.RedisService;
import com.example.demo.service.UserService;
import com.example.demo.util.JwtUtil;
import com.example.demo.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "认证模块")
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    @Operation(summary = "注册")
    @PostMapping("/register")
    @OperLog(module = "认证模块", operation = "用户注册")
    public R<Void> register(@Valid @RequestBody RegisterDTO dto) {
        userService.register(dto);
        return R.ok();
    }

    @Operation(summary = "登录")
    @PostMapping("/login")
    @OperLog(module = "认证模块", operation = "用户登录")
    public R<Map<String, Object>> login(@Valid @RequestBody LoginDTO dto) {
        String token = userService.login(dto);
        Long userId = jwtUtil.getUserId(token);
        UserVO user = userService.getCurrentUser(userId);

        // 将用户标记为在线
        redisService.userOnline(userId);

        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", user);
        return R.ok(result);
    }

    @Operation(summary = "退出登录")
    @PostMapping("/logout")
    public R<Void> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            // 获取用户ID
            Long userId = jwtUtil.getUserId(token);
            // 将用户标记为下线
            redisService.userOffline(userId);
            // 计算 Token 剩余有效期，存入黑名单
            long remaining = jwtUtil.parseToken(token).getExpiration().getTime() - System.currentTimeMillis();
            if (remaining > 0) {
                redisService.blacklistToken(token, remaining);
            }
        }
        return R.ok();
    }

    @Operation(summary = "获取当前用户信息")
    @GetMapping("/me")
    public R<UserVO> me() {
        Long userId = UserContext.getUserId();
        return R.ok(userService.getCurrentUser(userId));
    }

    @Operation(summary = "修改昵称")
    @PutMapping("/nickname")
    public R<Void> updateNickname(@RequestParam String nickname) {
        userService.updateNickname(UserContext.getUserId(), nickname);
        return R.ok();
    }
}
