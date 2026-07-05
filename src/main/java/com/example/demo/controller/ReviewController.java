package com.example.demo.controller;

import com.example.demo.common.PageResult;
import com.example.demo.common.R;
import com.example.demo.dto.ReviewDTO;
import com.example.demo.dto.ReviewVO;
import com.example.demo.service.ReviewService;
import com.example.demo.util.UserContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "评价模块")
@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "添加评价（学生/招聘方互评）")
    @PostMapping
    public R<Void> add(@Valid @RequestBody ReviewDTO dto) {
        reviewService.addReview(UserContext.getUserId(), dto);
        return R.ok();
    }

    @Operation(summary = "岗位评价列表")
    @GetMapping("/job/{jobId}")
    public R<PageResult<ReviewVO>> jobReviews(
            @PathVariable Long jobId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(reviewService.getJobReviews(jobId, pageNum, pageSize));
    }

    @Operation(summary = "用户收到的评价")
    @GetMapping("/user/{userId}")
    public R<PageResult<ReviewVO>> userReviews(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {
        return R.ok(reviewService.getUserReviews(userId, pageNum, pageSize));
    }
}
