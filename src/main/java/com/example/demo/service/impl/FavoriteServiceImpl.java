package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.BusinessException;
import com.example.demo.common.PageResult;
import com.example.demo.dto.JobVO;
import com.example.demo.entity.Favorite;
import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import com.example.demo.mapper.FavoriteMapper;
import com.example.demo.mapper.JobMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteMapper favoriteMapper;
    private final JobMapper jobMapper;
    private final UserMapper userMapper;

    @Override
    public void add(Long userId, Long jobId) {
        Long count = favoriteMapper.selectCount(
                new LambdaQueryWrapper<Favorite>()
                        .eq(Favorite::getUserId, userId)
                        .eq(Favorite::getJobId, jobId));
        if (count > 0) {
            throw new BusinessException("已收藏该岗位");
        }
        Favorite fav = new Favorite();
        fav.setUserId(userId);
        fav.setJobId(jobId);
        favoriteMapper.insert(fav);
    }

    @Override
    public void remove(Long userId, Long jobId) {
        int deleted = favoriteMapper.delete(
                new LambdaQueryWrapper<Favorite>()
                        .eq(Favorite::getUserId, userId)
                        .eq(Favorite::getJobId, jobId));
        if (deleted == 0) {
            throw new BusinessException("未收藏该岗位");
        }
    }

    @Override
    public boolean isFavorite(Long userId, Long jobId) {
        return favoriteMapper.selectCount(
                new LambdaQueryWrapper<Favorite>()
                        .eq(Favorite::getUserId, userId)
                        .eq(Favorite::getJobId, jobId)) > 0;
    }

    @Override
    public PageResult<JobVO> myFavorites(Long userId, int pageNum, int pageSize) {
        Page<Favorite> favPage = favoriteMapper.selectPage(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<Favorite>()
                        .eq(Favorite::getUserId, userId)
                        .orderByDesc(Favorite::getCreateTime));

        List<JobVO> voList = favPage.getRecords().stream().map(fav -> {
            Job job = jobMapper.selectById(fav.getJobId());
            if (job == null) return null;
            JobVO vo = new JobVO();
            BeanUtil.copyProperties(job, vo);
            User publisher = userMapper.selectById(job.getUserId());
            if (publisher != null) {
                vo.setPublisherName(publisher.getNickname());
                vo.setPublisherAvatar(publisher.getAvatar());
            }
            return vo;
        }).collect(Collectors.toList());

        return new PageResult<>(favPage.getTotal(), favPage.getCurrent(), favPage.getSize(), voList);
    }
}
