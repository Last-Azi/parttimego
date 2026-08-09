package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.common.BusinessException;
import com.example.demo.entity.Application;
import com.example.demo.entity.Job;
import com.example.demo.entity.Resume;
import com.example.demo.entity.User;
import com.example.demo.enums.ApplicationStatusEnum;
import com.example.demo.enums.JobStatusEnum;
import com.example.demo.mapper.ApplicationMapper;
import com.example.demo.mapper.JobMapper;
import com.example.demo.mapper.ResumeMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.MessageService;
import com.example.demo.service.RedisService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceImplTest {

    @Mock
    private ApplicationMapper applicationMapper;

    @Mock
    private JobMapper jobMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private ResumeMapper resumeMapper;

    @Mock
    private MessageService messageService;

    @Mock
    private RedisService redisService;

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Test
    void applyCreatesPendingApplicationAndNotifiesEmployer() {
        Job job = new Job();
        job.setId(10L);
        job.setUserId(20L);
        job.setTitle("Java Intern");
        job.setStatus(JobStatusEnum.APPROVED.getCode());

        Resume resume = new Resume();
        resume.setId(30L);

        User student = new User();
        student.setNickname("Alice");

        when(jobMapper.selectById(10L)).thenReturn(job);
        when(applicationMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
        when(resumeMapper.selectOne(any(LambdaQueryWrapper.class))).thenReturn(resume);
        when(userMapper.selectById(40L)).thenReturn(student);
        doAnswer(invocation -> {
            Application app = invocation.getArgument(0);
            app.setId(50L);
            return 1;
        }).when(applicationMapper).insert(any(Application.class));

        applicationService.apply(40L, 10L);

        ArgumentCaptor<Application> captor = ArgumentCaptor.forClass(Application.class);
        verify(applicationMapper).insert(captor.capture());
        Application inserted = captor.getValue();

        assertThat(inserted.getJobId()).isEqualTo(10L);
        assertThat(inserted.getUserId()).isEqualTo(40L);
        assertThat(inserted.getResumeId()).isEqualTo(30L);
        assertThat(inserted.getStatus()).isEqualTo(ApplicationStatusEnum.PENDING.getCode());
        verify(messageService).send(eq(20L), eq("APPLICATION"), any(), any(), eq(50L));
        verify(redisService).incrementApplyCount(10L);
    }

    @Test
    void applyRejectsEmployerApplyingToOwnJob() {
        Job job = new Job();
        job.setId(10L);
        job.setUserId(40L);
        job.setStatus(JobStatusEnum.APPROVED.getCode());

        when(jobMapper.selectById(10L)).thenReturn(job);

        assertThatThrownBy(() -> applicationService.apply(40L, 10L))
                .isInstanceOf(BusinessException.class);
        verify(applicationMapper, never()).insert(any(Application.class));
        verify(messageService, never()).send(any(), any(), any(), any(), any());
    }

    @Test
    void applyRejectsDuplicateApplication() {
        Job job = new Job();
        job.setId(10L);
        job.setUserId(20L);
        job.setStatus(JobStatusEnum.APPROVED.getCode());

        when(jobMapper.selectById(10L)).thenReturn(job);
        when(applicationMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

        assertThatThrownBy(() -> applicationService.apply(40L, 10L))
                .isInstanceOf(BusinessException.class);
        verify(applicationMapper, never()).insert(any(Application.class));
        verify(redisService, never()).incrementApplyCount(any());
    }
}
