package com.example.demo.service;

import com.example.demo.common.PageResult;
import com.example.demo.dto.StatsVO;
import com.example.demo.dto.UserVO;

public interface AdminService {

    StatsVO getStats();

    PageResult<UserVO> listUsers(Integer status, String role, int pageNum, int pageSize);

    void toggleUserStatus(Long userId);
}
