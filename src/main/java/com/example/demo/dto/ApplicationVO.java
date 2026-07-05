package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ApplicationVO {

    private Long id;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String salaryType;
    private String city;
    private Long userId;
    private String studentName;
    private String studentAvatar;
    private String school;
    private Long resumeId;
    private Integer status;
    private String statusText;
    private String remark;
    private LocalDateTime createTime;
}
