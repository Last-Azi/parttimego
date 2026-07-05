package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.BusinessException;
import com.example.demo.common.PageResult;
import com.example.demo.dto.StatsVO;
import com.example.demo.dto.UserVO;
import com.example.demo.entity.Application;
import com.example.demo.entity.Job;
import com.example.demo.entity.Review;
import com.example.demo.entity.User;
import com.example.demo.enums.CodeEnum;
import com.example.demo.mapper.ApplicationMapper;
import com.example.demo.mapper.JobMapper;
import com.example.demo.mapper.ReviewMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserMapper userMapper;
    private final JobMapper jobMapper;
    private final ApplicationMapper applicationMapper;
    private final ReviewMapper reviewMapper;

    @Override
    public StatsVO getStats() {
        StatsVO stats = new StatsVO();
        stats.setUserCount(userMapper.selectCount(null));
        stats.setJobCount(jobMapper.selectCount(null));
        stats.setApplicationCount(applicationMapper.selectCount(null));
        stats.setReviewCount(reviewMapper.selectCount(null));

        // 近7天每日新增岗位
        stats.setDailyJobs(countDaily(Job.class, jobMapper, 7));
        // 近7天每日新增投递
        stats.setDailyApplications(countDaily(Application.class, applicationMapper, 7));
        // 岗位分类统计
        stats.setCategoryStats(countCategories());

        return stats;
    }

    @Override
    public PageResult<UserVO> listUsers(Integer status, String role, int pageNum, int pageSize) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(User::getStatus, status);
        }
        if (StringUtils.hasText(role)) {
            wrapper.eq(User::getRole, role);
        }
        wrapper.orderByDesc(User::getCreateTime);

        Page<User> page = userMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        List<UserVO> voList = page.getRecords().stream().map(u -> {
            UserVO vo = new UserVO();
            BeanUtil.copyProperties(u, vo);
            return vo;
        }).collect(Collectors.toList());

        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), voList);
    }

    @Override
    public void toggleUserStatus(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(CodeEnum.NOT_FOUND);
        }
        user.setStatus(user.getStatus() == 1 ? 0 : 1);
        userMapper.updateById(user);
    }

    private <T> List<StatsVO.DayStat> countDaily(Class<T> clazz, com.baomidou.mybatisplus.core.mapper.BaseMapper<T> mapper, int days) {
        List<StatsVO.DayStat> result = new ArrayList<>();
        LocalDate today = LocalDate.now();
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd");

        // 查询最近N天的数据
        LocalDateTime start = today.minusDays(days - 1).atStartOfDay();
        List<?> records;
        if (clazz == Job.class) {
            records = ((JobMapper) mapper).selectList(
                    new LambdaQueryWrapper<Job>().ge(Job::getCreateTime, start));
        } else {
            records = ((ApplicationMapper) mapper).selectList(
                    new LambdaQueryWrapper<Application>().ge(Application::getCreateTime, start));
        }

        // 按日期分组计数
        Map<String, Long> countMap;
        if (clazz == Job.class) {
            countMap = records.stream()
                    .collect(Collectors.groupingBy(
                            r -> ((Job) r).getCreateTime().toLocalDate().toString(),
                            Collectors.counting()));
        } else {
            countMap = records.stream()
                    .collect(Collectors.groupingBy(
                            r -> ((Application) r).getCreateTime().toLocalDate().toString(),
                            Collectors.counting()));
        }

        for (int i = days - 1; i >= 0; i--) {
            LocalDate date = today.minusDays(i);
            StatsVO.DayStat ds = new StatsVO.DayStat();
            ds.setDate(date.format(fmt));
            ds.setCount(countMap.getOrDefault(date.toString(), 0L));
            result.add(ds);
        }
        return result;
    }

    private List<StatsVO.CategoryStat> countCategories() {
        List<Job> jobs = jobMapper.selectList(
                new LambdaQueryWrapper<Job>().isNotNull(Job::getCategory));
        Map<String, Long> map = jobs.stream()
                .collect(Collectors.groupingBy(Job::getCategory, Collectors.counting()));

        return map.entrySet().stream().map(e -> {
            StatsVO.CategoryStat cs = new StatsVO.CategoryStat();
            cs.setCategory(e.getKey());
            cs.setCount(e.getValue());
            return cs;
        }).collect(Collectors.toList());
    }
}
