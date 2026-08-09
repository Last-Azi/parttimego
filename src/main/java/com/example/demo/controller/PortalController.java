package com.example.demo.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.common.R;
import com.example.demo.dto.HomeStatsVO;
import com.example.demo.dto.PortalJobVO;
import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import com.example.demo.enums.JobStatusEnum;
import com.example.demo.mapper.JobMapper;
import com.example.demo.mapper.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Tag(name = "门户首页")
@RestController
@RequestMapping("/portal")
@RequiredArgsConstructor
public class PortalController {

    private final JobMapper jobMapper;
    private final UserMapper userMapper;
    private final StringRedisTemplate redisTemplate;
    private final com.example.demo.service.RedisService redisService;

    @Operation(summary = "首页统计数据")
    @GetMapping("/home")
    public R<HomeStatsVO> home() {
        HomeStatsVO stats = new HomeStatsVO();

        stats.setJobCount(jobMapper.selectCount(
                new LambdaQueryWrapper<Job>().eq(Job::getStatus, JobStatusEnum.APPROVED.getCode())));
        stats.setEmployerCount(userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getRole, "EMPLOYER")));
        stats.setStudentCount(userMapper.selectCount(
                new LambdaQueryWrapper<User>().eq(User::getRole, "STUDENT")));

        return R.ok(stats);
    }

    @Operation(summary = "最新岗位")
    @GetMapping("/latest-jobs")
    public R<List<PortalJobVO>> latestJobs() {
        List<Job> jobs = jobMapper.selectList(
                new LambdaQueryWrapper<Job>()
                        .eq(Job::getStatus, JobStatusEnum.APPROVED.getCode())
                        .orderByDesc(Job::getCreateTime)
                        .last("LIMIT 12"));

        List<PortalJobVO> voList = jobs.stream().map(this::toVO).collect(Collectors.toList());
        return R.ok(voList);
    }

    @Operation(summary = "岗位分类统计")
    @GetMapping("/categories")
    public R<List<Map<String, Object>>> categories() {
        List<Job> jobs = jobMapper.selectList(
                new LambdaQueryWrapper<Job>()
                        .eq(Job::getStatus, JobStatusEnum.APPROVED.getCode())
                        .select(Job::getCategory));

        Map<String, Long> categoryMap = jobs.stream()
                .collect(Collectors.groupingBy(
                        j -> j.getCategory() != null ? j.getCategory() : "其他",
                        Collectors.counting()));

        List<Map<String, Object>> result = categoryMap.entrySet().stream()
                .map(e -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", e.getKey());
                    map.put("count", e.getValue());
                    return map;
                })
                .collect(Collectors.toList());

        return R.ok(result);
    }

    @Operation(summary = "实时在线人数")
    @GetMapping("/online-count")
    public R<Long> onlineCount() {
        // 从Redis获取真实的在线用户数量
        Long count = redisService.getOnlineUserCount();
        return R.ok(count);
    }

    @Operation(summary = "心跳上报")
    @GetMapping("/heartbeat")
    public R<Void> heartbeat() {
        Long userId = com.example.demo.util.UserContext.getUserId();
        if (userId != null) {
            redisService.userOnline(userId);
        }
        return R.ok();
    }

    @Operation(summary = "搜索热词")
    @GetMapping("/hot-search-words")
    public R<List<String>> hotSearchWords() {
        // 从Redis获取搜索热词，如果不存在则返回默认值
        Set<String> hotWords = redisTemplate.opsForZSet().reverseRange("search:hot:words", 0, 9);
        if (hotWords != null && !hotWords.isEmpty()) {
            return R.ok(new ArrayList<>(hotWords));
        }
        // 默认热词
        List<String> defaultWords = Arrays.asList("家教", "餐饮", "促销", "服务员", "IT技术", "翻译", "摄影", "设计");
        return R.ok(defaultWords);
    }

    @Operation(summary = "智能推荐岗位")
    @GetMapping("/recommend")
    public R<List<PortalJobVO>> recommendJobs() {
        // 推荐逻辑：按浏览量排序，取前6个
        List<Job> jobs = jobMapper.selectList(
                new LambdaQueryWrapper<Job>()
                        .eq(Job::getStatus, JobStatusEnum.APPROVED.getCode())
                        .orderByDesc(Job::getViewCount)
                        .last("LIMIT 6"));

        List<PortalJobVO> voList = jobs.stream().map(this::toVO).collect(Collectors.toList());
        return R.ok(voList);
    }

    private PortalJobVO toVO(Job job) {
        PortalJobVO vo = new PortalJobVO();
        BeanUtil.copyProperties(job, vo);
        User user = userMapper.selectById(job.getUserId());
        if (user != null) {
            vo.setPublisherName(user.getNickname());
            vo.setPublisherAvatar(user.getAvatar());
        }
        return vo;
    }
}
