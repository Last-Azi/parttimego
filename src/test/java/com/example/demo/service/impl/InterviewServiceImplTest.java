package com.example.demo.service.impl;

import com.example.demo.common.BusinessException;
import com.example.demo.dto.InterviewDTO;
import com.example.demo.entity.Application;
import com.example.demo.entity.Interview;
import com.example.demo.entity.Job;
import com.example.demo.entity.User;
import com.example.demo.enums.ApplicationStatusEnum;
import com.example.demo.enums.InterviewStatusEnum;
import com.example.demo.mapper.ApplicationMapper;
import com.example.demo.mapper.InterviewMapper;
import com.example.demo.mapper.JobMapper;
import com.example.demo.mapper.UserMapper;
import com.example.demo.service.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InterviewServiceImplTest {

    @Mock
    private InterviewMapper interviewMapper;

    @Mock
    private ApplicationMapper applicationMapper;

    @Mock
    private JobMapper jobMapper;

    @Mock
    private UserMapper userMapper;

    @Mock
    private MessageService messageService;

    @InjectMocks
    private InterviewServiceImpl interviewService;

    @Test
    void createMovesApplicationToInterviewAndNotifiesStudent() {
        Application app = new Application();
        app.setId(10L);
        app.setJobId(20L);
        app.setUserId(30L);
        app.setStatus(ApplicationStatusEnum.PENDING.getCode());

        Job job = new Job();
        job.setId(20L);
        job.setUserId(40L);
        job.setTitle("Java Intern");

        User employer = new User();
        employer.setNickname("Tech Co");

        InterviewDTO dto = new InterviewDTO();
        dto.setApplicationId(10L);
        dto.setInterviewTime(LocalDateTime.of(2026, 8, 3, 10, 0));
        dto.setInterviewPlace("Room 101");
        dto.setInterviewType("ONSITE");
        dto.setInterviewContent("Bring resume");

        when(applicationMapper.selectById(10L)).thenReturn(app);
        when(jobMapper.selectById(20L)).thenReturn(job);
        when(userMapper.selectById(40L)).thenReturn(employer);
        doAnswer(invocation -> {
            Interview interview = invocation.getArgument(0);
            interview.setId(50L);
            return 1;
        }).when(interviewMapper).insert(any(Interview.class));

        interviewService.create(40L, dto);

        ArgumentCaptor<Interview> interviewCaptor = ArgumentCaptor.forClass(Interview.class);
        verify(interviewMapper).insert(interviewCaptor.capture());
        Interview inserted = interviewCaptor.getValue();

        assertThat(inserted.getApplicationId()).isEqualTo(10L);
        assertThat(inserted.getJobId()).isEqualTo(20L);
        assertThat(inserted.getEmployerId()).isEqualTo(40L);
        assertThat(inserted.getStudentId()).isEqualTo(30L);
        assertThat(inserted.getStatus()).isEqualTo(InterviewStatusEnum.PENDING.getCode());
        assertThat(app.getStatus()).isEqualTo(ApplicationStatusEnum.INTERVIEW.getCode());
        verify(applicationMapper).updateById(app);
        verify(messageService).send(eq(30L), eq("INTERVIEW"), any(), any(), eq(50L));
    }

    @Test
    void createRejectsApplicationNotOwnedByEmployer() {
        Application app = new Application();
        app.setId(10L);
        app.setJobId(20L);
        app.setStatus(ApplicationStatusEnum.PENDING.getCode());

        Job job = new Job();
        job.setId(20L);
        job.setUserId(99L);

        when(applicationMapper.selectById(10L)).thenReturn(app);
        when(jobMapper.selectById(20L)).thenReturn(job);

        InterviewDTO dto = new InterviewDTO();
        dto.setApplicationId(10L);

        assertThatThrownBy(() -> interviewService.create(40L, dto))
                .isInstanceOf(BusinessException.class);
        verify(interviewMapper, never()).insert(any(Interview.class));
        verify(messageService, never()).send(any(), any(), any(), any(), any());
    }

    @Test
    void cancelAcceptedInterviewRevertsApplicationToViewed() {
        Interview interview = new Interview();
        interview.setId(50L);
        interview.setApplicationId(10L);
        interview.setEmployerId(40L);
        interview.setStatus(InterviewStatusEnum.ACCEPTED.getCode());

        Application app = new Application();
        app.setId(10L);
        app.setStatus(ApplicationStatusEnum.INTERVIEW.getCode());

        when(interviewMapper.selectById(50L)).thenReturn(interview);
        when(applicationMapper.selectById(10L)).thenReturn(app);

        interviewService.cancel(40L, 50L);

        assertThat(interview.getStatus()).isEqualTo(InterviewStatusEnum.CANCELLED.getCode());
        assertThat(app.getStatus()).isEqualTo(ApplicationStatusEnum.VIEWED.getCode());
        verify(interviewMapper).updateById(interview);
        verify(applicationMapper).updateById(app);
    }
}
