package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.BusinessException;
import com.example.demo.common.PageResult;
import com.example.demo.dto.EmployerStatsVO;
import com.example.demo.dto.JobDTO;
import com.example.demo.dto.JobQuery;
import com.example.demo.dto.JobVO;
import com.example.demo.entity.Application;
import com.example.demo.entity.Interview;
import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import com.example.demo.enums.CodeEnum;
import com.example.demo.enums.JobStatusEnum;
import com.example.demo.enums.ApplicationStatusEnum;
import com.example.demo.enums.InterviewStatusEnum;
import com.example.demo.mapper.ApplicationMapper;
import com.example.demo.mapper.InterviewMapper;
import com.example.demo.mapper.JobMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.JobService;
import com.example.demo.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService {

    private final JobMapper jobMapper;
    private final UserMapper userMapper;
    private final ApplicationMapper applicationMapper;
    private final InterviewMapper interviewMapper;
    private final RedisService redisService;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @Override
    public void publish(Long userId, JobDTO dto) {
        Job job = new Job();
        BeanUtil.copyProperties(dto, job);
        job.setUserId(userId);
        job.setStatus(JobStatusEnum.APPROVED.getCode());
        jobMapper.insert(job);
        redisService.clearJobCache();
    }

    @Override
    public void update(Long userId, Long jobId, JobDTO dto) {
        Job job = getAndCheckOwner(userId, jobId);
        BeanUtil.copyProperties(dto, job);
        job.setStatus(JobStatusEnum.APPROVED.getCode());
        jobMapper.updateById(job);
        redisService.clearJobCache();
    }

    @Override
    public void offline(Long userId, Long jobId) {
        Job job = getAndCheckOwner(userId, jobId);
        job.setStatus(JobStatusEnum.OFFLINE.getCode());
        jobMapper.updateById(job);
        redisService.clearJobCache();
    }

    @Override
    public void delete(Long userId, Long jobId) {
        getAndCheckOwner(userId, jobId);
        jobMapper.deleteById(jobId);
        redisService.clearJobCache();
    }

    @Override
    public PageResult<JobVO> search(JobQuery query) {
        // 生成缓存 key
        String cacheKey = redisService.buildJobSearchKey(
                query.getKeyword(), query.getCity(), query.getCategory(),
                query.getSalaryType(), query.getPageNum(), query.getPageSize());

        // 尝试从缓存获取
        String cachedJson = redisService.getString(cacheKey);
        if (cachedJson != null) {
            try {
                return objectMapper.readValue(cachedJson, PageResult.class);
            } catch (Exception e) {
                // 反序列化失败，重新查询
            }
        }

        // 缓存未命中，查询数据库
        LambdaQueryWrapper<Job> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Job::getStatus, JobStatusEnum.APPROVED.getCode());

        if (StringUtils.hasText(query.getKeyword())) {
            wrapper.and(w -> w.like(Job::getTitle, query.getKeyword())
                    .or().like(Job::getDescription, query.getKeyword()));
        }
        if (StringUtils.hasText(query.getCity())) {
            wrapper.eq(Job::getCity, query.getCity());
        }
        if (StringUtils.hasText(query.getCategory())) {
            wrapper.eq(Job::getCategory, query.getCategory());
        }
        if (StringUtils.hasText(query.getSalaryType())) {
            wrapper.eq(Job::getSalaryType, query.getSalaryType());
        }

        wrapper.orderByDesc(Job::getCreateTime);

        Page<Job> page = jobMapper.selectPage(
                new Page<>(query.getPageNum(), query.getPageSize()), wrapper);

        PageResult<JobVO> result = toPageResult(page);

        // 写入缓存
        try {
            String json = objectMapper.writeValueAsString(result);
            redisService.setString(cacheKey, json);
        } catch (Exception e) {
            // 缓存写入失败不影响业务
        }

        return result;
    }

    @Override
    public JobVO getDetail(Long jobId) {
        Job job = jobMapper.selectById(jobId);
        if (job == null) {
            throw new BusinessException(CodeEnum.NOT_FOUND);
        }
        // 增加浏览量
        redisService.incrementViewCount(jobId);
        return toVO(job);
    }

    @Override
    public java.util.List<JobVO> getHotJobs(int top) {
        java.util.List<String> jobIds = redisService.getHotJobs(top);
        return jobIds.stream()
                .map(id -> {
                    Job job = jobMapper.selectById(Long.parseLong(id));
                    return job != null ? toVO(job) : null;
                })
                .filter(vo -> vo != null)
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    public PageResult<JobVO> myJobs(Long userId, Integer status, int pageNum, int pageSize) {
        LambdaQueryWrapper<Job> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Job::getUserId, userId);
        if (status != null) {
            wrapper.eq(Job::getStatus, status);
        }
        wrapper.orderByDesc(Job::getCreateTime);

        Page<Job> page = jobMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return toPageResult(page);
    }

    @Override
    public void approve(Long jobId) {
        Job job = jobMapper.selectById(jobId);
        if (job == null) {
            throw new BusinessException(CodeEnum.NOT_FOUND);
        }
        job.setStatus(JobStatusEnum.APPROVED.getCode());
        jobMapper.updateById(job);
        redisService.clearJobCache();
    }

    @Override
    public void reject(Long jobId, String reason) {
        Job job = jobMapper.selectById(jobId);
        if (job == null) {
            throw new BusinessException(CodeEnum.NOT_FOUND);
        }
        job.setStatus(JobStatusEnum.REJECTED.getCode());
        job.setRejectReason(reason);
        jobMapper.updateById(job);
        redisService.clearJobCache();
    }

    @Override
    public PageResult<JobVO> adminList(Integer status, int pageNum, int pageSize) {
        LambdaQueryWrapper<Job> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(Job::getStatus, status);
        }
        wrapper.orderByDesc(Job::getCreateTime);

        Page<Job> page = jobMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return toPageResult(page);
    }

    private Job getAndCheckOwner(Long userId, Long jobId) {
        Job job = jobMapper.selectById(jobId);
        if (job == null) {
            throw new BusinessException(CodeEnum.NOT_FOUND);
        }
        if (!job.getUserId().equals(userId)) {
            throw new BusinessException(CodeEnum.FORBIDDEN);
        }
        return job;
    }

    private PageResult<JobVO> toPageResult(Page<Job> page) {
        List<JobVO> voList = page.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), voList);
    }

    private JobVO toVO(Job job) {
        JobVO vo = new JobVO();
        BeanUtil.copyProperties(job, vo);
        User user = userMapper.selectById(job.getUserId());
        if (user != null) {
            vo.setPublisherName(user.getNickname());
            vo.setPublisherAvatar(user.getAvatar());
        }
        vo.setViewCount(redisService.getViewCount(job.getId()));
        return vo;
    }

    @Override
    public EmployerStatsVO getEmployerStats(Long userId) {
        EmployerStatsVO stats = new EmployerStatsVO();

        stats.setJobCount(jobMapper.selectCount(
                new LambdaQueryWrapper<Job>().eq(Job::getUserId, userId)));

        List<Long> jobIds = jobMapper.selectList(
                new LambdaQueryWrapper<Job>().eq(Job::getUserId, userId).select(Job::getId))
                .stream().map(Job::getId).collect(Collectors.toList());

        if (jobIds.isEmpty()) {
            stats.setApplicationCount(0);
            stats.setInterviewCount(0);
            stats.setPassRate(0);
            return stats;
        }

        stats.setApplicationCount(applicationMapper.selectCount(
                new LambdaQueryWrapper<Application>().in(Application::getJobId, jobIds)));

        stats.setInterviewCount(interviewMapper.selectCount(
                new LambdaQueryWrapper<Interview>().in(Interview::getJobId, jobIds)));

        long totalApps = stats.getApplicationCount();
        long acceptedApps = applicationMapper.selectCount(
                new LambdaQueryWrapper<Application>()
                        .in(Application::getJobId, jobIds)
                        .eq(Application::getStatus, ApplicationStatusEnum.ACCEPTED.getCode()));
        stats.setPassRate(totalApps > 0 ? (double) acceptedApps / totalApps * 100 : 0);

        return stats;
    }
}
