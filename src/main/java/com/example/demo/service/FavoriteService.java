package com.example.demo.service;

import com.example.demo.common.PageResult;
import com.example.demo.dto.JobVO;

public interface FavoriteService {

    void add(Long userId, Long jobId);

    void remove(Long userId, Long jobId);

    boolean isFavorite(Long userId, Long jobId);

    PageResult<JobVO> myFavorites(Long userId, int pageNum, int pageSize);
}
