package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InterviewVO {

    private Long id;
    private Long applicationId;
    private Long jobId;
    private String jobTitle;
    private Long employerId;
    private String companyName;
    private Long studentId;
    private String studentName;
    private String studentPhone;
    private LocalDateTime interviewTime;
    private String interviewPlace;
    private String interviewType;
    private String interviewContent;
    private Integer status;
    private String statusText;
    private String studentRemark;
    private String employerRemark;
    private LocalDateTime createTime;
}
