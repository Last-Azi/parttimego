package com.example.demo.service;

import com.example.demo.common.PageResult;
import com.example.demo.dto.ReviewDTO;
import com.example.demo.dto.ReviewVO;

public interface ReviewService {

    void addReview(Long userId, ReviewDTO dto);

    PageResult<ReviewVO> getJobReviews(Long jobId, int pageNum, int pageSize);

    PageResult<ReviewVO> getUserReviews(Long userId, int pageNum, int pageSize);
}
