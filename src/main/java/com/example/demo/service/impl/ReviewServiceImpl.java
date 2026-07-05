package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.BusinessException;
import com.example.demo.common.PageResult;
import com.example.demo.dto.ReviewDTO;
import com.example.demo.dto.ReviewVO;
import com.example.demo.entity.Application;
import com.example.demo.entity.Job;
import com.example.demo.entity.Review;
import com.example.demo.entity.User;
import com.example.demo.enums.ApplicationStatusEnum;
import com.example.demo.mapper.ApplicationMapper;
import com.example.demo.mapper.JobMapper;
import com.example.demo.mapper.ReviewMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewMapper reviewMapper;
    private final ApplicationMapper applicationMapper;
    private final JobMapper jobMapper;
    private final UserMapper userMapper;

    @Override
    public void addReview(Long userId, ReviewDTO dto) {
        Application app = applicationMapper.selectById(dto.getApplicationId());
        if (app == null) {
            throw new BusinessException("投递记录不存在");
        }
        if (app.getStatus() != ApplicationStatusEnum.ACCEPTED.getCode()) {
            throw new BusinessException("只有已录用的投递才能评价");
        }

        // 判断是学生评价招聘方，还是招聘方评价学生
        Job job = jobMapper.selectById(app.getJobId());
        Long toUserId;
        if (userId.equals(app.getUserId())) {
            // 学生评价招聘方
            toUserId = job.getUserId();
        } else if (userId.equals(job.getUserId())) {
            // 招聘方评价学生
            toUserId = app.getUserId();
        } else {
            throw new BusinessException("无权评价此投递");
        }

        // 检查是否已评价
        Long count = reviewMapper.selectCount(
                new LambdaQueryWrapper<Review>()
                        .eq(Review::getApplicationId, dto.getApplicationId())
                        .eq(Review::getFromUserId, userId));
        if (count > 0) {
            throw new BusinessException("已评价过该投递");
        }

        Review review = new Review();
        review.setApplicationId(dto.getApplicationId());
        review.setFromUserId(userId);
        review.setToUserId(toUserId);
        review.setJobId(app.getJobId());
        review.setRating(dto.getRating());
        review.setContent(dto.getContent());
        reviewMapper.insert(review);
    }

    @Override
    public PageResult<ReviewVO> getJobReviews(Long jobId, int pageNum, int pageSize) {
        Page<Review> page = reviewMapper.selectPage(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<Review>()
                        .eq(Review::getJobId, jobId)
                        .orderByDesc(Review::getCreateTime));
        return toPageResult(page);
    }

    @Override
    public PageResult<ReviewVO> getUserReviews(Long userId, int pageNum, int pageSize) {
        Page<Review> page = reviewMapper.selectPage(
                new Page<>(pageNum, pageSize),
                new LambdaQueryWrapper<Review>()
                        .eq(Review::getToUserId, userId)
                        .orderByDesc(Review::getCreateTime));
        return toPageResult(page);
    }

    private PageResult<ReviewVO> toPageResult(Page<Review> page) {
        List<ReviewVO> voList = page.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), voList);
    }

    private ReviewVO toVO(Review review) {
        ReviewVO vo = new ReviewVO();
        BeanUtil.copyProperties(review, vo);

        User fromUser = userMapper.selectById(review.getFromUserId());
        if (fromUser != null) vo.setFromUserName(fromUser.getNickname());

        User toUser = userMapper.selectById(review.getToUserId());
        if (toUser != null) vo.setToUserName(toUser.getNickname());

        Job job = jobMapper.selectById(review.getJobId());
        if (job != null) vo.setJobTitle(job.getTitle());

        return vo;
    }
}
