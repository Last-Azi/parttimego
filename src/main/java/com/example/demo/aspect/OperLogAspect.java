package com.example.demo.aspect;

import com.example.demo.annotation.OperLog;
import com.example.demo.entity.OperLogEntity;
import com.example.demo.mapper.OperLogMapper;
import com.example.demo.util.UserContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class OperLogAspect {

    private final OperLogMapper operLogMapper;
    private final ObjectMapper objectMapper;

    @Around("@annotation(operLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperLog operLog) throws Throwable {
        OperLogEntity logEntity = new OperLogEntity();
        logEntity.setModule(operLog.module());
        logEntity.setOperation(operLog.operation());

        // 获取方法信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        logEntity.setMethod(signature.getDeclaringTypeName() + "." + signature.getName());

        // 获取请求参数
        try {
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                logEntity.setParams(objectMapper.writeValueAsString(args));
            }
        } catch (Exception e) {
            log.warn("获取请求参数失败", e);
        }

        // 获取请求信息
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                logEntity.setIp(request.getRemoteAddr());
            }
        } catch (Exception e) {
            log.warn("获取请求信息失败", e);
        }

        // 获取用户信息
        try {
            Long userId = UserContext.getUserId();
            if (userId != null) {
                logEntity.setUserId(userId);
            }
        } catch (Exception e) {
            // 未登录用户
        }

        // 执行方法
        Object result = null;
        try {
            result = joinPoint.proceed();
            logEntity.setStatus(1);
        } catch (Throwable e) {
            logEntity.setStatus(0);
            logEntity.setErrorMsg(e.getMessage());
            throw e;
        } finally {
            logEntity.setCreateTime(LocalDateTime.now());
            try {
                operLogMapper.insert(logEntity);
            } catch (Exception e) {
                log.error("保存操作日志失败", e);
            }
        }

        return result;
    }
}
