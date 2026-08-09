package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.BusinessException;
import com.example.demo.common.PageResult;
import com.example.demo.dto.InterviewDTO;
import com.example.demo.dto.InterviewVO;
import com.example.demo.entity.Application;
import com.example.demo.entity.Interview;
import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import com.example.demo.enums.ApplicationStatusEnum;
import com.example.demo.enums.CodeEnum;
import com.example.demo.enums.InterviewStatusEnum;
import com.example.demo.enums.JobStatusEnum;
import com.example.demo.mapper.ApplicationMapper;
import com.example.demo.mapper.InterviewMapper;
import com.example.demo.mapper.JobMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.InterviewService;
import com.example.demo.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterviewServiceImpl implements InterviewService {

    private final InterviewMapper interviewMapper;
    private final ApplicationMapper applicationMapper;
    private final JobMapper jobMapper;
    private final UserMapper userMapper;
    private final MessageService messageService;

    @Override
    @Transactional
    public void create(Long employerUserId, InterviewDTO dto) {
        Application app = applicationMapper.selectById(dto.getApplicationId());
        if (app == null) {
            throw new BusinessException(CodeEnum.NOT_FOUND);
        }

        Job job = jobMapper.selectById(app.getJobId());
        if (job == null || !job.getUserId().equals(employerUserId)) {
            throw new BusinessException(CodeEnum.FORBIDDEN);
        }

        if (app.getStatus() != ApplicationStatusEnum.PENDING.getCode()
                && app.getStatus() != ApplicationStatusEnum.VIEWED.getCode()) {
            throw new BusinessException("当前投递状态无法发送面试邀请");
        }

        Interview interview = new Interview();
        interview.setApplicationId(dto.getApplicationId());
        interview.setJobId(app.getJobId());
        interview.setEmployerId(employerUserId);
        interview.setStudentId(app.getUserId());
        interview.setInterviewTime(dto.getInterviewTime());
        interview.setInterviewPlace(dto.getInterviewPlace());
        interview.setInterviewType(dto.getInterviewType());
        interview.setInterviewContent(dto.getInterviewContent());
        interview.setStatus(InterviewStatusEnum.PENDING.getCode());
        interviewMapper.insert(interview);

        app.setStatus(ApplicationStatusEnum.INTERVIEW.getCode());
        applicationMapper.updateById(app);

        // 发送通知给学生
        User employer = userMapper.selectById(employerUserId);
        Job jobInfo = jobMapper.selectById(app.getJobId());
        String employerName = employer != null ? employer.getNickname() : "未知企业";
        String jobTitle = jobInfo != null ? jobInfo.getTitle() : "未知岗位";
        messageService.send(app.getUserId(), "INTERVIEW",
                "收到面试邀请", employerName + " 邀请您面试「" + jobTitle + "」", interview.getId());
    }

    @Override
    public PageResult<InterviewVO> employerList(Long employerUserId, Long jobId, Integer status, int pageNum, int pageSize) {
        LambdaQueryWrapper<Interview> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Interview::getEmployerId, employerUserId);
        if (jobId != null) {
            wrapper.eq(Interview::getJobId, jobId);
        }
        if (status != null) {
            wrapper.eq(Interview::getStatus, status);
        }
        wrapper.orderByDesc(Interview::getCreateTime);

        Page<Interview> page = interviewMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return toPageResult(page);
    }

    @Override
    public PageResult<InterviewVO> studentList(Long studentUserId, Integer status, int pageNum, int pageSize) {
        LambdaQueryWrapper<Interview> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Interview::getStudentId, studentUserId);
        if (status != null) {
            wrapper.eq(Interview::getStatus, status);
        }
        wrapper.orderByDesc(Interview::getCreateTime);

        Page<Interview> page = interviewMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return toPageResult(page);
    }

    @Override
    public InterviewVO getDetail(Long userId, Long interviewId) {
        Interview interview = interviewMapper.selectById(interviewId);
        if (interview == null) {
            throw new BusinessException(CodeEnum.NOT_FOUND);
        }
        if (!interview.getEmployerId().equals(userId) && !interview.getStudentId().equals(userId)) {
            throw new BusinessException(CodeEnum.FORBIDDEN);
        }
        return toVO(interview);
    }

    @Override
    @Transactional
    public void accept(Long studentUserId, Long interviewId) {
        Interview interview = interviewMapper.selectById(interviewId);
        if (interview == null || !interview.getStudentId().equals(studentUserId)) {
            throw new BusinessException(CodeEnum.NOT_FOUND);
        }
        if (interview.getStatus() != InterviewStatusEnum.PENDING.getCode()) {
            throw new BusinessException("当前状态无法接受面试邀请");
        }

        interview.setStatus(InterviewStatusEnum.ACCEPTED.getCode());
        interviewMapper.updateById(interview);

        // 通知企业
        User student = userMapper.selectById(studentUserId);
        String studentName = student != null ? student.getNickname() : "未知学生";
        messageService.send(interview.getEmployerId(), "INTERVIEW_ACCEPTED",
                "面试邀请已接受", studentName + " 接受了您的面试邀请", interview.getId());
    }

    @Override
    @Transactional
    public void reject(Long studentUserId, Long interviewId, String remark) {
        Interview interview = interviewMapper.selectById(interviewId);
        if (interview == null || !interview.getStudentId().equals(studentUserId)) {
            throw new BusinessException(CodeEnum.NOT_FOUND);
        }
        if (interview.getStatus() != InterviewStatusEnum.PENDING.getCode()) {
            throw new BusinessException("当前状态无法拒绝面试邀请");
        }

        interview.setStatus(InterviewStatusEnum.REJECTED.getCode());
        interview.setStudentRemark(remark);
        interviewMapper.updateById(interview);

        Application app = applicationMapper.selectById(interview.getApplicationId());
        if (app != null) {
            app.setStatus(ApplicationStatusEnum.REJECTED.getCode());
            applicationMapper.updateById(app);
        }

        // 通知企业
        User student = userMapper.selectById(studentUserId);
        Job jobInfo = jobMapper.selectById(interview.getJobId());
        String studentName = student != null ? student.getNickname() : "未知学生";
        String jobTitle = jobInfo != null ? jobInfo.getTitle() : "未知岗位";
        String content = studentName + " 拒绝了您的面试邀请「" + jobTitle + "」";
        if (remark != null && !remark.isEmpty()) {
            content += "\n拒绝理由：" + remark;
        }
        messageService.send(interview.getEmployerId(), "INTERVIEW_REJECTED",
                "面试邀请被拒绝", content, interview.getId());
    }

    @Override
    @Transactional
    public void complete(Long employerUserId, Long interviewId) {
        Interview interview = interviewMapper.selectById(interviewId);
        if (interview == null || !interview.getEmployerId().equals(employerUserId)) {
            throw new BusinessException(CodeEnum.NOT_FOUND);
        }
        if (interview.getStatus() != InterviewStatusEnum.ACCEPTED.getCode()) {
            throw new BusinessException("当前状态无法标记完成");
        }

        interview.setStatus(InterviewStatusEnum.COMPLETED.getCode());
        interviewMapper.updateById(interview);

        // 通知学生面试已完成
        User employer = userMapper.selectById(employerUserId);
        Job jobInfo = jobMapper.selectById(interview.getJobId());
        String employerName = employer != null ? employer.getNickname() : "未知企业";
        String jobTitle = jobInfo != null ? jobInfo.getTitle() : "未知岗位";
        messageService.send(interview.getStudentId(), "INTERVIEW_COMPLETED",
                "面试已完成", employerName + " 的面试「" + jobTitle + "」已结束，请等待录用结果", interview.getId());
    }

    @Override
    @Transactional
    public void cancel(Long employerUserId, Long interviewId) {
        Interview interview = interviewMapper.selectById(interviewId);
        if (interview == null || !interview.getEmployerId().equals(employerUserId)) {
            throw new BusinessException(CodeEnum.NOT_FOUND);
        }
        if (interview.getStatus() != InterviewStatusEnum.PENDING.getCode()
                && interview.getStatus() != InterviewStatusEnum.ACCEPTED.getCode()) {
            throw new BusinessException("当前状态无法取消面试邀请");
        }

        boolean wasAccepted = interview.getStatus() == InterviewStatusEnum.ACCEPTED.getCode();

        interview.setStatus(InterviewStatusEnum.CANCELLED.getCode());
        interviewMapper.updateById(interview);

        if (wasAccepted) {
            Application app = applicationMapper.selectById(interview.getApplicationId());
            if (app != null) {
                app.setStatus(ApplicationStatusEnum.VIEWED.getCode());
                applicationMapper.updateById(app);
            }
        }
    }

    private PageResult<InterviewVO> toPageResult(Page<Interview> page) {
        List<InterviewVO> voList = page.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), voList);
    }

    private InterviewVO toVO(Interview interview) {
        InterviewVO vo = new InterviewVO();
        BeanUtil.copyProperties(interview, vo);

        InterviewStatusEnum statusEnum = InterviewStatusEnum.fromCode(interview.getStatus());
        vo.setStatusText(statusEnum.getDesc());

        Job job = jobMapper.selectById(interview.getJobId());
        if (job != null) {
            vo.setJobTitle(job.getTitle());
        }

        User employer = userMapper.selectById(interview.getEmployerId());
        if (employer != null) {
            vo.setCompanyName(employer.getNickname() != null && !employer.getNickname().isEmpty()
                    ? employer.getNickname() : employer.getUsername());
        }

        User student = userMapper.selectById(interview.getStudentId());
        if (student != null) {
            vo.setStudentName(student.getNickname() != null && !student.getNickname().isEmpty()
                    ? student.getNickname() : student.getUsername());
            vo.setStudentPhone(student.getPhone());
        }

        return vo;
    }
}
