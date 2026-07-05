package com.example.demo.service;

import com.example.demo.common.PageResult;
import com.example.demo.dto.EmployerStatsVO;
import com.example.demo.dto.JobDTO;
import com.example.demo.dto.JobQuery;
import com.example.demo.dto.JobVO;

public interface JobService {

    void publish(Long userId, JobDTO dto);

    void update(Long userId, Long jobId, JobDTO dto);

    void offline(Long userId, Long jobId);

    void delete(Long userId, Long jobId);

    PageResult<JobVO> search(JobQuery query);

    JobVO getDetail(Long jobId);

    PageResult<JobVO> myJobs(Long userId, Integer status, int pageNum, int pageSize);

    void approve(Long jobId);

    void reject(Long jobId, String reason);

    PageResult<JobVO> adminList(Integer status, int pageNum, int pageSize);

    EmployerStatsVO getEmployerStats(Long userId);

    java.util.List<JobVO> getHotJobs(int top);
}
