package com.example.demo.service;

import com.example.demo.common.PageResult;
import com.example.demo.dto.ApplicationVO;

public interface ApplicationService {

    void apply(Long userId, Long jobId);

    void withdraw(Long userId, Long applicationId);

    void updateStatus(Long employerUserId, Long applicationId, Integer status, String remark);

    PageResult<ApplicationVO> myApplications(Long userId, Integer status, int pageNum, int pageSize);

    PageResult<ApplicationVO> jobApplications(Long employerUserId, Long jobId, Integer status, int pageNum, int pageSize);
}
