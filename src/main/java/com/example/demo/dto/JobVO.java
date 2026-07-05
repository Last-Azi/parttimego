package com.example.demo.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class JobVO {

    private Long id;
    private Long userId;
    private String publisherName;
    private String publisherAvatar;
    private String title;
    private String description;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private String salaryType;
    private String city;
    private String address;
    private String category;
    private String workTime;
    private Integer headcount;
    private Integer status;
    private String rejectReason;
    private Long viewCount;
    private LocalDateTime createTime;
}
