package com.example.demo.service;

import com.example.demo.common.PageResult;
import com.example.demo.dto.InterviewDTO;
import com.example.demo.dto.InterviewVO;

public interface InterviewService {

    void create(Long employerUserId, InterviewDTO dto);

    PageResult<InterviewVO> employerList(Long employerUserId, Long jobId, Integer status, int pageNum, int pageSize);

    PageResult<InterviewVO> studentList(Long studentUserId, Integer status, int pageNum, int pageSize);

    InterviewVO getDetail(Long userId, Long interviewId);

    void accept(Long studentUserId, Long interviewId);

    void reject(Long studentUserId, Long interviewId, String remark);

    void complete(Long employerUserId, Long interviewId);

    void cancel(Long employerUserId, Long interviewId);
}
