package com.example.demo.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.BusinessException;
import com.example.demo.common.PageResult;
import com.example.demo.dto.ApplicationVO;
import com.example.demo.entity.Application;
import com.example.demo.entity.Job;
import com.example.demo.entity.Resume;
import com.example.demo.entity.User;
import com.example.demo.enums.ApplicationStatusEnum;
import com.example.demo.enums.CodeEnum;
import com.example.demo.enums.JobStatusEnum;
import com.example.demo.mapper.ApplicationMapper;
import com.example.demo.mapper.JobMapper;
import com.example.demo.mapper.ResumeMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.ApplicationService;
import com.example.demo.service.MessageService;
import com.example.demo.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationMapper applicationMapper;
    private final JobMapper jobMapper;
    private final UserMapper userMapper;
    private final ResumeMapper resumeMapper;
    private final MessageService messageService;
    private final RedisService redisService;

    @Override
    public void apply(Long userId, Long jobId) {
        Job job = jobMapper.selectById(jobId);
        if (job == null || job.getStatus() != JobStatusEnum.APPROVED.getCode()) {
            throw new BusinessException("岗位不存在或未发布");
        }
        if (job.getUserId().equals(userId)) {
            throw new BusinessException("不能投递自己发布的岗位");
        }

        Long count = applicationMapper.selectCount(
                new LambdaQueryWrapper<Application>()
                        .eq(Application::getJobId, jobId)
                        .eq(Application::getUserId, userId));
        if (count > 0) {
            throw new BusinessException("您已投递过该岗位");
        }

        Resume resume = resumeMapper.selectOne(
                new LambdaQueryWrapper<Resume>().eq(Resume::getUserId, userId));

        Application app = new Application();
        app.setJobId(jobId);
        app.setUserId(userId);
        app.setResumeId(resume != null ? resume.getId() : null);
        app.setStatus(ApplicationStatusEnum.PENDING.getCode());
        applicationMapper.insert(app);

        // 发送通知给企业
        User student = userMapper.selectById(userId);
        String studentName = student != null ? student.getNickname() : "未知用户";
        messageService.send(job.getUserId(), "APPLICATION",
                "收到新的投递", studentName + " 投递了您的岗位「" + job.getTitle() + "」", app.getId());

        // 增加热门排名分数
        redisService.incrementApplyCount(jobId);
    }

    @Override
    public void withdraw(Long userId, Long applicationId) {
        Application app = applicationMapper.selectById(applicationId);
        if (app == null || !app.getUserId().equals(userId)) {
            throw new BusinessException(CodeEnum.NOT_FOUND);
        }
        if (app.getStatus() != ApplicationStatusEnum.PENDING.getCode()
                && app.getStatus() != ApplicationStatusEnum.VIEWED.getCode()) {
            throw new BusinessException("当前状态无法撤回");
        }
        applicationMapper.deleteById(applicationId);
    }

    @Override
    public void updateStatus(Long employerUserId, Long applicationId, Integer status, String remark) {
        Application app = applicationMapper.selectById(applicationId);
        if (app == null) {
            throw new BusinessException(CodeEnum.NOT_FOUND);
        }
        Job job = jobMapper.selectById(app.getJobId());
        if (job == null || !job.getUserId().equals(employerUserId)) {
            throw new BusinessException(CodeEnum.FORBIDDEN);
        }

        app.setStatus(status);
        app.setRemark(remark);
        applicationMapper.updateById(app);

        // 录用或拒绝时通知学生
        if (status == ApplicationStatusEnum.ACCEPTED.getCode()) {
            User employer = userMapper.selectById(employerUserId);
            String employerName = employer != null ? employer.getNickname() : "未知企业";
            messageService.send(app.getUserId(), "ACCEPTED",
                    "恭喜您被录用", employerName + " 的岗位「" + job.getTitle() + "」已录用您", app.getId());
        } else if (status == ApplicationStatusEnum.REJECTED.getCode()) {
            User employer = userMapper.selectById(employerUserId);
            String employerName = employer != null ? employer.getNickname() : "未知企业";
            String content = employerName + " 的岗位「" + job.getTitle() + "」拒绝了您的投递";
            if (remark != null && !remark.isEmpty()) {
                content += "\n拒绝理由：" + remark;
            }
            messageService.send(app.getUserId(), "REJECTED",
                    "投递被拒绝", content, app.getId());
        }
    }

    @Override
    public PageResult<ApplicationVO> myApplications(Long userId, Integer status, int pageNum, int pageSize) {
        LambdaQueryWrapper<Application> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Application::getUserId, userId);
        if (status != null) {
            wrapper.eq(Application::getStatus, status);
        }
        wrapper.orderByDesc(Application::getCreateTime);

        Page<Application> page = applicationMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return toPageResult(page);
    }

    @Override
    public PageResult<ApplicationVO> jobApplications(Long employerUserId, Long jobId, Integer status, int pageNum, int pageSize) {
        Job job = jobMapper.selectById(jobId);
        if (job == null || !job.getUserId().equals(employerUserId)) {
            throw new BusinessException(CodeEnum.FORBIDDEN);
        }

        LambdaQueryWrapper<Application> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Application::getJobId, jobId);
        if (status != null) {
            wrapper.eq(Application::getStatus, status);
        }
        wrapper.orderByDesc(Application::getCreateTime);

        Page<Application> page = applicationMapper.selectPage(new Page<>(pageNum, pageSize), wrapper);
        return toPageResult(page);
    }

    private PageResult<ApplicationVO> toPageResult(Page<Application> page) {
        List<ApplicationVO> voList = page.getRecords().stream().map(this::toVO).collect(Collectors.toList());
        return new PageResult<>(page.getTotal(), page.getCurrent(), page.getSize(), voList);
    }

    private ApplicationVO toVO(Application app) {
        ApplicationVO vo = new ApplicationVO();
        BeanUtil.copyProperties(app, vo);

        ApplicationStatusEnum statusEnum = ApplicationStatusEnum.fromCode(app.getStatus());
        vo.setStatusText(statusEnum.getDesc());

        Job job = jobMapper.selectById(app.getJobId());
        if (job != null) {
            vo.setJobTitle(job.getTitle());
            vo.setSalaryMin(job.getSalaryMin());
            vo.setSalaryMax(job.getSalaryMax());
            vo.setSalaryType(job.getSalaryType());
            vo.setCity(job.getCity());
            User publisher = userMapper.selectById(job.getUserId());
            if (publisher != null) {
                vo.setCompanyName(publisher.getNickname());
            }
        }

        User student = userMapper.selectById(app.getUserId());
        if (student != null) {
            vo.setStudentName(student.getNickname() != null && !student.getNickname().isEmpty()
                    ? student.getNickname() : student.getUsername());
            vo.setStudentAvatar(student.getAvatar());
        }
        if (app.getResumeId() != null) {
            Resume resume = resumeMapper.selectById(app.getResumeId());
            if (resume != null) {
                vo.setSchool(resume.getSchool());
            }
        }

        return vo;
    }
}
