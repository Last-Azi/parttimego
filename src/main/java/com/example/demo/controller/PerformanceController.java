package com.example.demo.controller;

import com.example.demo.common.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

@Tag(name = "性能监控")
@RestController
@RequestMapping("/performance")
public class PerformanceController {

    @Operation(summary = "系统性能指标")
    @GetMapping("/metrics")
    public R<Map<String, Object>> getMetrics() {
        Map<String, Object> metrics = new HashMap<>();

        // JVM 内存信息
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        long heapUsed = memoryBean.getHeapMemoryUsage().getUsed() / 1024 / 1024;
        long heapMax = memoryBean.getHeapMemoryUsage().getMax() / 1024 / 1024;
        metrics.put("heapUsedMB", heapUsed);
        metrics.put("heapMaxMB", heapMax);
        metrics.put("heapUsagePercent", heapMax > 0 ? (heapUsed * 100 / heapMax) : 0);

        // 线程信息
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        metrics.put("threadCount", threadBean.getThreadCount());
        metrics.put("daemonThreadCount", threadBean.getDaemonThreadCount());

        // 运行时间
        long uptime = ManagementFactory.getRuntimeMXBean().getUptime() / 1000 / 60;
        metrics.put("uptimeMinutes", uptime);

        // 处理器数量
        metrics.put("availableProcessors", Runtime.getRuntime().availableProcessors());

        return R.ok(metrics);
    }
}
